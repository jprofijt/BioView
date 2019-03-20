package nl.bioinf.jp_kcd_wr.image_library.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

@Service
public class FileSystemStorageService implements StorageService {

    private Path rootLocation;
    private final ImageDataSource imageDataSource;

    private final static Pattern PATTERN = Pattern.compile("(.*?)(?:\\((\\d+)\\))?(\\.[^.]*)?");

    @Autowired
    public FileSystemStorageService(ImageDataSource imageDataSource, Environment environment) {
        this.imageDataSource = imageDataSource;
        this.rootLocation = Paths.get(environment.getProperty("library.upload"));
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
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation) && path.toFile().isFile())
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
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
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
