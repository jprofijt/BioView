package nl.bioinf.jp_kcd_wr.image_library.storage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
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
    public FileSystemStorageService(ImageDataSource imageDataSource) {
        this.imageDataSource = imageDataSource;
        rootLocation = Paths.get("src/main/resources/static/upload/upload");
        this.cacheLocation = Paths.get("src/main/resources/static/upload/thumbnails");

        logger.log(Level.INFO, "Starting FileSystemStorage service using {0} as imageDataSource, and {1} as root location", new Object[] {this.imageDataSource, this.rootLocation});

        checkParameters();
    }

    private Path getVerifiedRootLocation(Environment environment){
        String Location = environment.getProperty("library.upload");
        if (Location == null || Location.isEmpty()){
            throw new IllegalArgumentException("library.upload parameter is empty");
        }
        else{
            return Paths.get(Location);
        }
    }



    private Path getVerifiedThumbnailLocation(Environment environment){
        String Location = environment.getProperty("cache-location");
        if (Location == null || Location.isEmpty()){
            throw new IllegalArgumentException("cache-location parameter is empty");
        }
        else{
            return Paths.get(Location);
        }
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
     */
    private void makeLibraryLocations(){
        File Root = this.rootLocation.toFile();
        File Thumbnails = this.cacheLocation.toFile();
        if (!Root.exists()){
            Root.mkdirs();
            logger.log(Level.WARNING, "Given library location doesn't exist, creating new library location");
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
    public void store(MultipartFile file, File directory) {
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
                String newFilename = getNewName(filename);
                String directoryPath = this.getRootLocation().toString()+"/" + directory +"/";
                Path filePath = Paths.get(directoryPath + newFilename);
                Files.copy(inputStream, filePath, // 'copies' file to upload-dir using the rootLocation and filename
                        StandardCopyOption.REPLACE_EXISTING);                // file of same name in upload-dir will be overwritten

                Image image = createImageData(filename, newFilename, filePath);

                imageDataSource.insertImage(image);
                createThumbnails(new File(directoryPath));

            }
        }
        catch (IOException e) {
            logger.log(Level.WARNING, "File {0} could not be stored", filename);
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    /**
     * Gives the file a new name with a number attached in case it already exists
     * @param filename name of the uploaded file
     * @return the new name
     *
     * @author Kim Chau Duong
     */
    @Override
    public String getNewName(String filename) {
        if (Files.exists(this.rootLocation.resolve(filename))){
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
                } while (Files.exists(this.rootLocation.resolve(filename)));
            }
        }
        return filename;
    }

    /**
     * Creates image object with data to be stored in the database
     * @param origFilename original uploaded file name
     * @param newName new file name
     * @param filePath directory path that the file is stored in
     * @return
     *
     * @author Kim Chau Duong
     */
    @Override
    public Image createImageData(String origFilename, String newName, Path filePath) {
        Image newImage = new Image();
        newImage.setOrigName(origFilename);
        newImage.setNewFilename(newName);
        newImage.setPath(filePath.toString());

        return newImage;
    }

    /**
     * Loads all stored file in that particular directory
     * @param currentFolder directory that contains the stored files
     * @return stream of file paths of the files
     *
     * @author Kim Chau Duong
     */
    @Override
    public Stream<Path> loadAll(String currentFolder) {
        try {
            return walkThroughFiles(currentFolder)
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Storage service could not read stored files");
            throw new StorageException("Failed to read stored files", e);
        }
    }

    /**
     * loads absolute file path
     * @param currentFolder folder to search
     * @return stream of paths
     *
     * @author Jouke Profijt
     */
    @Override
    public Stream<Path> loadAbsolute(String currentFolder) {
        try {
            return walkThroughFiles(currentFolder);
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Storage service could not read stored files");
            throw new StorageException("Failed to read stored files", e);
        }
    }

    private Stream<Path> walkThroughFiles(String currentFolder) throws IOException {
        return Files.walk(this.rootLocation.resolve(currentFolder), 1)
                .filter(path -> !path.equals(this.rootLocation.resolve(currentFolder)) && path.toFile().isFile());

    }

    /**
     * Builds file path from the root location and the provided directory path
     * @param filename name of file
     * @return file path
     *
     * @author Kim Chau Duong
     */
    @Override
    public Path loadImage(String filename) {
        return rootLocation.resolve(filename);
    }

    private Path loadThumbnail(String filename) {return cacheLocation.resolve(filename);}

    /**
     * Loads file as a resource
     * @param filename name of file
     * @param directory directory path of the file
     * @return file resource
     *
     * @author Kim Chau Duong
     */
    @Override
    public Resource loadAsResource(String filename, String directory) {
        try {
            if(directory != null && !directory.isEmpty()) {
                filename = directory + '/' + filename;
            }
            Path file = loadImage(filename);
            return loadResource(new UrlResource(file.toUri()));
        }
        catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Could not read file {0}", filename);
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }


    /**
     * loads resource if resource is accessible
     * @param resource the new resource
     * @return return correct resource
     * @author Kim Chau Duong
     */
    private Resource loadResource(Resource resource) {
        if (resource.exists() || resource.isReadable()) {
            return resource;
        }
        else {
            throw new StorageFileNotFoundException(
                    "Could not read file: " + resource.getFilename());

        }
    }

    /**
     * Separate resource loader for thumbnails
     * @param filename thumbnail filename
     * @return Resource
     * @author Jouke Profijt
     */
    @Override
    public Resource loadThumbnailAsResource(String filename) {
        try {
            Path thumbnail = loadThumbnail(filename);
            return loadResource(new UrlResource(thumbnail.toUri()));
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Could not read file {0}", filename);
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }



    /**
     * Deletes all existing files
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
        logger.log(Level.WARNING, "Deleting ALL library contents");
    }

    /**
     * For each image in existing image library will create a cached image if it doesn't exist and makes a database insert
     * @param Directory Root directory to recursively
     *
     * @author Jouke Profijt
     */
    @Override
    public void processExistingImageLibrary(File Directory) {
        for (File contentDirectory : listDirectories(Directory)){
            processExistingImageLibrary(contentDirectory);
        }

        try {
            IndexImages(Directory);
            createThumbnails(Directory);

        } catch (IOException e) {
            logger.log(Level.WARNING, "Directory {0} not found", Directory.getPath());
        }

    }



    private void processLibrary() {
        File Directory = this.rootLocation.toFile();
        logger.log(Level.INFO, "processing Image library...");
        processExistingImageLibrary(Directory);
        logger.log(Level.INFO, "processing complete!");
    }

    private List<File> listDirectories(File Directory){
        ArrayList<File> contents = new ArrayList<>(Arrays.asList(Directory.listFiles(File::isDirectory)));
        return contents;

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
            anotatedImage.setPath(image.getPath());
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

        int imageId = imageDataSource.getImageIdFromPath(image.getPath());
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

    @Override
    public Path getRootLocation(){
        return this.rootLocation;
    }

    /**
     * Initializes the file storage
     *
     * @author Kim Chau Duong
     */
    @Override
    public void init() {
            makeLibraryLocations();
            processLibrary();
    }
}
