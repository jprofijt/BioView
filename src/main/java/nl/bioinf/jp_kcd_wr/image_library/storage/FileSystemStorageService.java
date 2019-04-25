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

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final ImageDataSource imageDataSource;
    private static final Logger logger = Logger.getLogger(FileSystemStorageService.class.getName());

    private final static Pattern PATTERN = Pattern.compile("(.*?)(?:\\((\\d+)\\))?(\\.[^.]*)?");
    private final Path cacheLocation;

    @Autowired
    public FileSystemStorageService(ImageDataSource imageDataSource, Environment environment) {
        this.imageDataSource = imageDataSource;
        this.rootLocation = Paths.get(environment.getProperty("library.upload"));
        this.cacheLocation = Paths.get(environment.getProperty("cache-location"));
        logger.log(Level.INFO, "Starting FileSystemStorage service using {0} as imageDataSource, and {1} as root location", new Object[] {this.imageDataSource, this.rootLocation});
    }

    @Override
    public void store(MultipartFile file) {
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
                Path filePath = this.rootLocation.resolve(newFilename);
                Files.copy(inputStream, filePath, // 'copies' file to upload-dir using the rootLocation and filename
                        StandardCopyOption.REPLACE_EXISTING);                // file of same name in upload-dir will be overwritten

                Image image = createImageData(filename, newFilename, filePath);

                imageDataSource.insertImage(image);

            }
        }
        catch (IOException e) {
            logger.log(Level.WARNING, "File {0} could not be stored", filename);
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

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

    @Override
    public Image createImageData(String origFilename, String hash, Path filePath) {
        Image newImage = new Image();
        newImage.setOrigName(origFilename);
        newImage.setNewFilename(hash);
        newImage.setPath(filePath.toString());

        return newImage;
    }

    @Override
    public Stream<Path> loadAll(String currentFolder) {
        try {
            return Files.walk(this.rootLocation.resolve(currentFolder), 1)
                    .filter(path -> !path.equals(this.rootLocation.resolve(currentFolder)) && path.toFile().isFile())
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Storage service could not read stored files");
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Could not read file {0}", filename);
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
        logger.log(Level.WARNING, "Deleting ALL library contents");
    }

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
        logger.log(Level.INFO, "Completed caching images for library location {0}", Directory.getPath());

    }

    private List<File> listDirectories(File Directory){
        ArrayList<File> contents = new ArrayList<>(Arrays.asList(Directory.listFiles(File::isDirectory)));
        return contents;

    }


    /**
     * Creates a new .cache directory with scaled tumbnail images
     * @param Directory
     * @throws IOException
     */
    private void createThumbnails(File Directory) throws IOException {


        for (File image : Directory.listFiles(File::isFile)) {
            cacheImage(image);
        }
    }

    private void IndexImages(File directory){
        for (File image : directory.listFiles(File::isFile)) {
            Image anotatedImage = new Image();
            anotatedImage.setPath(image.getPath());
            anotatedImage.setOrigName(image.getName());
            anotatedImage.setNewFilename(image.getName());
            imageDataSource.insertImage(anotatedImage);
        }
    }

    private void cacheImage(File image) throws IOException {
        String extenion = getFileExtension(image);
        String cacheImage = image.getName().replace(extenion, ".jpg");

        int imageId = imageDataSource.getImageIdFromPath(image.getPath());
        File cacheLocation = new File(this.cacheLocation.toString() + "/"+ imageId + ".jpg");

        if (imageDataSource.isNotCached(imageId)) {

            BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            img
                    .createGraphics()
                    .drawImage(ImageIO.read(image).getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
            ImageIO.write(img, "jpg", cacheLocation);
            imageDataSource.insertCache(imageId, cacheLocation.toPath());
            if (cacheLocation.isFile()) {
                logger.log(Level.INFO, "Succesfully created cache of {0} in directory: {1}", new Object[]{image.getName(), this.cacheLocation});
            }
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }


    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
            processExistingImageLibrary(rootLocation.toFile());
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Storage system was not able to initialize");
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
