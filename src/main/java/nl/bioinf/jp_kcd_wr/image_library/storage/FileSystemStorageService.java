package nl.bioinf.jp_kcd_wr.image_library.storage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageFileType;
import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import nl.bioinf.jp_kcd_wr.image_library.model.ImageAttribute;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

/**
 * Class that handles the file uploads
 * It contains
 *
 * @author Kim Chau Duong, Jouke Profijt
 * @version 1.0
 */
@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final ImageDataSource imageDataSource;
    private static final Logger logger = Logger.getLogger(FileSystemStorageService.class.getName());

    private final static Pattern PATTERN = Pattern.compile("(.*?)(?:\\((\\d+)\\))?(\\.[^.]*)?");
    private final Path cacheLocation;

    /**
     * Contructor
     */
    @Autowired
    public FileSystemStorageService(ImageDataSource imageDataSource, Environment environment) {
        this.imageDataSource = imageDataSource;
        rootLocation = Paths.get(environment.getProperty("library.sym"));
        this.cacheLocation = Paths.get(environment.getProperty("library.sym.thumbnails"));

        makeLibraryLocations();
        processLibrary();

        File rootDirectory = new File(rootLocation + "/HeadDirectory");
        if (!rootDirectory.exists()){
            rootDirectory.mkdirs();
        }
        logger.log(Level.INFO, "Starting FileSystemStorage service using {0} as imageDataSource, and {1} as root location", new Object[] {this.imageDataSource, this.rootLocation});

        checkParameters();
    }

    /**
     * Simple function that checks storage related parameters for errors.
     *
     *
     * @throws IllegalArgumentException if parameters are incorrect or cause errors
     * @author Jouke Profijt
     */
    private void checkParameters() throws IllegalArgumentException{
        File directory = rootLocation.toFile();
        File thumbnails = cacheLocation.toFile();
        if (directory.exists() && (!directory.canRead() || !directory.canWrite())){
            throw new IllegalArgumentException("Cannot access library.upload location");
        }

        if (thumbnails.exists() && (!thumbnails.canRead() || !thumbnails.canWrite())){
            throw new IllegalArgumentException("Cannot access cache-location directory");
        }


    }


    /**
     * creates new library locations if they don't already exist
     * @auhtor Jouke Profijt
     */
    private void makeLibraryLocations(){
        File Root = this.rootLocation.toFile();
        File Thumbnails = this.cacheLocation.toFile();

        File HeadDirectory = new File(rootLocation.toString() + "/HeadDirectory/");
        if (!Root.exists()){
            Root.mkdirs();
            logger.log(Level.WARNING, "Given library location doesn't exist, creating new library location");
        }
        if (!HeadDirectory.exists()){
            HeadDirectory.mkdirs();
            logger.log(Level.INFO, "New Library creating head directory");
        }

        if (!Thumbnails.exists()){
            Thumbnails.mkdirs();
            logger.log(Level.WARNING, "Given Thumbnail location doesn't exist, creating new thumbnail location");
        }


    }

    /**
     * Stores the file in the directory and the data in database
     * @param file uploaded file
     *
     * @author Kim Chau Duong, Jouke Profijt
     */
    @Override
    public void storeFile(MultipartFile file, File directory) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path directoryPath = this.rootLocation.resolve(directory.toPath());
                String newFilename = getNewName(filename, directoryPath);
                Path filePath = directoryPath.resolve(newFilename);

                storeImage(inputStream, filePath);
                Image image = createImageData(filename, newFilename, directory.toPath().resolve(newFilename));
                imageDataSource.insertImage(image);
                createThumbnails(directoryPath.toFile());

                String sqlDate = "yyyy-MM-dd HH:mm:ss";
                createMetaData(image, LocalDateTime.now().format(DateTimeFormatter.ofPattern(sqlDate)));
            }
        }
        catch (IOException e) {
            logger.log(Level.WARNING, "File {0} could not be stored", filename);
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    /**
     * Stores the actual file in the designated directory
     * @param inputStream image file
     * @param filePath destination directory
     * @throws IOException
     *
     * @author Kim Chau Duong, Jouke Profijt
     */
    private void storeImage(InputStream inputStream, Path filePath) throws IOException {
        Files.copy(inputStream, filePath,
                StandardCopyOption.REPLACE_EXISTING);
        filePath.toFile().setExecutable(true, false);
        filePath.toFile().setReadable(true, false);
        filePath.toFile().setWritable(true, false);
    }

    /**
     * converts filetype to ENUM storage medium
     * @param extension file extention
     * @return return correct enum type
     *
     * @author Jouke Profijt
     */
    private ImageFileType getFileTypeEnum(String extension){
        if (extension.toLowerCase().equals("jpg")){
            return ImageFileType.JPG;
        }
        if (extension.toLowerCase().equals("tiff")){
            return ImageFileType.TIFF;
        }
        else {
            return ImageFileType.PNG;
        }
    }

    /**
     * Creates uploaded image metadata
     * @param image image to generate metadata
     * @param date current date
     *
     * @auhor Jouke Profijt
     */
    private void createMetaData(Image image, String date) {

        String path = image.getPath();
        String name = image.getNewFilename();
        int id = imageDataSource.getImageIdFromPath(path);
        File imageFile = new File(path);
        long size = imageFile.length();
        ImageFileType fileType = getFileTypeEnum(FilenameUtils.getExtension(path));

        imageDataSource.insertImageMetaData(new ImageAttribute(id, name,imageFile.getParent(), path, size, date, fileType));
    }

    /**
     * Creates image attribute data for images in directory
     *
     * @param directory location to be indexed
     *
     * @auhtor Kim Chau Duong
     */
    private void indexImageAttributes(File directory){
        for (File image : directory.listFiles(File::isFile)){
            ImageAttribute attribute = new ImageAttribute();
            File relativeImage = rootLocation.relativize(image.toPath()).toFile();
            attribute.setImageName(image.getName());
            attribute.setPath(relativeImage.getParent());
            attribute.setFilePath(relativeImage.getPath());
            int id = imageDataSource.getImageIdFromPath(relativeImage.getPath());
            attribute.setId(id);
            attribute.setImageSize(image.length());
            attribute.setDateCreated(getDateModified(image));
            attribute.setFileType(getFileTypeEnum(FilenameUtils.getExtension(String.valueOf(image))));
        imageDataSource.insertImageMetaData(attribute);
        }
    }

    /**
     * Modified date indexing
     * @param directory directory to be indexed
     * @return modified date
     *
     * @author Kim Chau Duong
     */
    private String getDateModified(File directory) {
        long lastModified = directory.lastModified();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(lastModified);
    }

    /**
     * Gives the file a new name with a number attached in case it already exists
     * @param filename name of the uploaded file
     * @return the new name
     *
     * @author Kim Chau Duong
     */
    private String getNewName(String filename, Path directoryPath) {
        if (Files.exists(directoryPath.resolve(filename))){
            Matcher m = PATTERN.matcher(filename);
            if (m.matches()){
                String prefix = m.group(1);
                String number = m.group(2);
                String suffix = m.group(3);

                int count = 0;
                if (number != null){
                    count = Integer.parseInt(number);
                }
                do {
                    count++;
                    filename = prefix + "(" + count + ")" + suffix;
                } while (Files.exists(directoryPath.resolve(filename)));
            }
        }
        return filename;
    }

    /**
     * Creates Image object with data to be stored in the database
     * @param origFilename original uploaded file name
     * @param newName new file name
     * @param filePath directory path that the file is stored in
     * @return new Image object
     *
     * @author Kim Chau Duong
     */
    public Image createImageData(String origFilename, String newName, Path filePath) {
        Image newImage = new Image();
        newImage.setOrigName(origFilename);
        newImage.setNewFilename(newName);
        newImage.setPath(filePath.toString());
        return newImage;
    }

    /**
     * loads absolute file paths of image
     * @param currentFolder folder to search
     * @return stream of paths
     *
     * @author Jouke Profijt
     */
    @Override
    public Stream<Path> loadAbsoluteStoredImagePaths(String currentFolder) {
        try {
            return walkThroughFiles(currentFolder);
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Storage service could not read stored files");
            throw new StorageException("Failed to read stored files", e);
        }
    }

    private Stream<Path> walkThroughFiles(String currentFolder) throws IOException {
        return Files.walk(getFullImagePath(currentFolder), 1)
                .filter(path -> !path.equals(getFullImagePath(currentFolder)) && path.toFile().isFile());

    }

    /**
     * Builds file path from the root location and the provided directory path
     * @param filename name of file
     * @return file path
     *
     * @author Kim Chau Duong
     */
    public Path getFullImagePath(String filename) {
        return rootLocation.resolve(filename);
    }

    /**
     * For each image in existing image library will create a cached image if it doesn't exist and makes a database insert
     * @param directory Root directory to recursively
     *
     * @author Jouke Profijt
     */
    @Override
    public void storeExistingImageLibrary(File directory) {
        for (File contentDirectory : listDirectories(directory)){
            storeExistingImageLibrary(contentDirectory);
        }
        processDirectory(directory);


    }

    private void processDirectory(File directory) {
        try {
            IndexImages(directory);
            createThumbnails(directory);
            indexImageAttributes(directory);

        } catch (IOException e) {
            logger.log(Level.WARNING, "Directory {0} not found", directory.getPath());
        }
    }


    private void processLibrary() {
        File Directory = this.rootLocation.toFile();
        logger.log(Level.INFO, "processing Image library...");
        storeExistingImageLibrary(Directory);
        logger.log(Level.INFO, "processing complete!");
    }

    private List<File> listDirectories(File Directory){
        return new ArrayList<>(Arrays.asList(Directory.listFiles(File::isDirectory)));

    }


    /**
     * Creates a new .cache directory with scaled tumbnail images
     * @param Directory Directory for caching
     * @throws IOException
     *
     * @author Jouke Profijt
     */
    private void createThumbnails(File Directory) throws IOException {

        for (File image : Directory.listFiles(File::isFile)) {
            storeImageThumbnail(image);
        }
    }

    /**
     * Indexes existing images into database
     * @param directory directory with images to be indexed
     *
     * @author Jouke Profijt
     */
    private void IndexImages(File directory){

        for (File image : directory.listFiles(File::isFile)) {

            Image anotatedImage = new Image();
            anotatedImage.setPath(String.valueOf(rootLocation.relativize(Paths.get(image.getPath()))));
            anotatedImage.setOrigName(image.getName());
            anotatedImage.setNewFilename(image.getName());
            imageDataSource.insertImage(anotatedImage);
        }
    }

    /**
     * Creates a 200x200 thumbnail of given image and inserts cache location into database
     * @param image image to be processed into cache
     * @throws IOException If image doesn't exist throws IOException
     *
     * @author Jouke Profijt
     */
    private void storeImageThumbnail(File image) throws IOException {
        String fileExtension = getFileExtension(image);
        String cacheImage = image.getName().replace(fileExtension, ".jpg");

        int imageId = imageDataSource.getImageIdFromPath(rootLocation.relativize(image.toPath()).toString());
        File cacheLocation = new File(this.cacheLocation.toString() + "/"+ imageId + ".jpg");

        if (imageDataSource.checkThumbnailStatus(imageId)) {

            BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            img
                    .createGraphics()
                    .drawImage(ImageIO.read(image).getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            ImageIO.write(img, "jpg", cacheLocation);
            imageDataSource.storeThumbnailCacheDataPath(imageId, cacheLocation.toPath());
        }
    }


    /**
     * Small function that determines image file extension
     * @param file file to get extension from
     * @return file extension
     *
     * @author Jouke Profijt
     */
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }
}
