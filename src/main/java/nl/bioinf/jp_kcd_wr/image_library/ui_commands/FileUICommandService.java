package nl.bioinf.jp_kcd_wr.image_library.ui_commands;

import nl.bioinf.jp_kcd_wr.image_library.storage.FileSystemStorageService;
import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;
import org.apache.commons.io.FileUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FileUICommandService implements UICommandService {
    private final Path rootLocation;
    private final StorageService storageService;
    private static final Logger logger = Logger.getLogger(FileSystemStorageService.class.getName());

    public FileUICommandService(Environment environment, StorageService storageService) {
        rootLocation = Paths.get("upload/upload");
        this.storageService = storageService;
        logger.log(Level.INFO, "Starting FileCommandService service using {0} as root location", this.rootLocation);
    }


    /**
     * Retrieves the full path, that includes the rootlocation and the given source path string
     * @param path a relative path
     * @return full directory path
     */
    private Path getFullPath(String path) { return this.rootLocation.resolve(path);}


    /**
     * Removes selected source directory or file
     * @param source the to-be deleted directory or file
     *
     * @author Kim Chau Duong
     */
    @Override
    public void removeFile(String source) {
        Path path = getFullPath(source);
        try {
            logger.log(Level.INFO, "Deleting directory: {0}", source);
            FileUtils.deleteDirectory(path.toFile());
            logger.log(Level.INFO, "Successfully deleted {0}!", source);
        } catch (IOException e) {
            logger.log(Level.WARNING, "{0} could not be deleted", source);
        }

    }

    /**
     * Moves directory or file from current location to newly assigned destination
     * @param source moving directory
     * @param destination paste destination
     *
     * @author Kim Chau Duong
     */
    @Override
    public void moveFile(String source, String destination) {
        Path pathFrom = getFullPath(source);
        Path pathTo = getFullPath(destination).resolve(new File(source).getName());
        try{
            logger.log(Level.INFO,"Moving directory from {0} to {1}", new Object[]{source, destination});
            Files.move(pathFrom, pathTo, StandardCopyOption.REPLACE_EXISTING);
            storageService.processExistingImageLibrary(new File(String.valueOf(getFullPath(destination))));

        } catch (IOException e) {
            logger.log(Level.WARNING, "{0} could not be moved", source);
        }
    }

    /**
     * Copies directory or file from current location to another destination
     * @param source copied directory
     * @param destination paste destination
     *
     * @author Kim Chau duong
     */
    @Override
    public void copyFile(String source, String destination) {
        Path pathFrom = getFullPath(source);
        Path pathTo = getFullPath(destination).resolve(new File(source).getName());
        try{
            logger.log(Level.INFO,"Copying directory from {0} to {1}", new Object[]{source, destination});
            FileUtils.copyDirectory(pathFrom.toFile(), pathTo.toFile());
            storageService.processExistingImageLibrary(new File(String.valueOf(getFullPath(destination))));
        } catch (IOException e) {
            logger.log(Level.WARNING, "{0} could not be copied", source);
        }
    }

    /**
     * Renames directory or file to its provided new name
     * @param source current path with old name
     * @param renamedFileName new name
     */
    @Override
    public void renameFile(String source, String renamedFileName) {
        Path oldPath = getFullPath(source);
        Path renamedPath = oldPath.resolveSibling(renamedFileName);
        try{
            logger.log(Level.INFO, "Renaming {0} to {1} in {2}", new Object[]{new File(source).getName(), renamedFileName, new File(source).getParent()});
            Files.move(oldPath, renamedPath, StandardCopyOption.REPLACE_EXISTING);
            storageService.processExistingImageLibrary(new File(String.valueOf(renamedPath)));
        } catch (IOException e) {
            logger.log(Level.WARNING, "{0} could not be renamed", source);
        }

    }
}
