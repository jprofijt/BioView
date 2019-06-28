package nl.bioinf.jp_kcd_wr.image_library.ui_commands;

import nl.bioinf.jp_kcd_wr.image_library.folder_manager.DirectoryExistsException;
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
        this.rootLocation = Paths.get(environment.getProperty("library.sym"));
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
    public boolean removeFile(String source) {
        Path path = getFullPath(source);
        try {
            logger.log(Level.INFO, "Deleting {0}", source);
            if (Files.isDirectory(path)){
                FileUtils.deleteDirectory(path.toFile());
            } else{
                Files.delete(path);
            }

            logger.log(Level.INFO, "Successfully deleted {0}!", source);
            return true;
        } catch (IOException e) {
            logger.log(Level.WARNING, "{0} could not be deleted", source);
            return false;
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
    public boolean moveFile(String source, String destination) {
        Path pathFrom = getFullPath(source);
        Path pathTo = getFullPath(destination).resolve(new File(source).getName());
        if (Files.exists(pathTo)){
            logger.log(Level.WARNING, "{0} already exists!", source);
            return false;
        } else {
            try{
                logger.log(Level.INFO,"Moving {0} from {1} to {2}", new Object[]{new File(source).getName(),source, destination});
                Files.move(pathFrom, pathTo, StandardCopyOption.REPLACE_EXISTING);
                storageService.storeExistingImageLibrary(new File(String.valueOf(getFullPath(destination))));
                return true;
            } catch (IOException e) {
                logger.log(Level.WARNING, "{0} could not be moved", source);
                return false;
            }
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
    public boolean copyFile(String source, String destination) {
        Path pathFrom = getFullPath(source);
        Path pathTo = getFullPath(destination).resolve(new File(source).getName());
        if (Files.exists(pathTo)){
            logger.log(Level.WARNING, "{0} already exists!", source);
            return false;
        } else {
            try{
                logger.log(Level.INFO,"Copying {0} from {1} to {2}", new Object[]{new File(source).getName(),source, destination});
                if (Files.isDirectory(pathFrom)){
                    FileUtils.copyDirectory(pathFrom.toFile(), pathTo.toFile());
                } else {
                    FileUtils.copyFile(pathFrom.toFile(), pathTo.toFile());
                }
                storageService.storeExistingImageLibrary(new File(String.valueOf(getFullPath(destination))));
                return true;
            } catch (IOException e) {
                logger.log(Level.WARNING, "{0} could not be copied", source);
                return false;
            }
        }
    }

    /**
     * Renames directory or file to its provided new name
     * @param source current path with old name
     * @param renamedFileName new name
     */
    @Override
    public boolean renameFile(String source, String renamedFileName) {
        Path oldPath = getFullPath(source);
        Path renamedPath = oldPath.resolveSibling(renamedFileName);
        if (Files.exists(renamedPath)){
            logger.log(Level.WARNING, "{0} already exists!", source);
            return false;
        }
        else {
            try{
                logger.log(Level.INFO, "Renaming {0} to {1} in {2}", new Object[]{new File(source).getName(), renamedFileName, new File(source).getParent()});
                Files.move(oldPath, renamedPath, StandardCopyOption.REPLACE_EXISTING);
                storageService.storeExistingImageLibrary(new File(String.valueOf(renamedPath)));
                return true;
            } catch (IOException e) {
                logger.log(Level.WARNING, "{0} could not be renamed", source);
                return false;
            }
        }
    }

    /**
     * Create a new directory
     * @param directoryName name of the new directory
     * @param currentPath path where directory should be located
     * @throws DirectoryExistsException when directory already exists
     *
     * @author Jouke Profijt
     */
    @Override
    public void createNewFolder(String directoryName, String currentPath) throws DirectoryExistsException {
        Path path = getFullPath(currentPath).resolve(directoryName);
        File newDir = new File(String.valueOf(path));
        try {
            logger.log(Level.INFO, "Creating {0}", newDir);
            Files.createDirectory(newDir.toPath());
            newDir.setWritable(true, false);
            newDir.setReadable(true, false);
            newDir.setExecutable(true, false);

        } catch (IOException e){
            e.printStackTrace();
            logger.log(Level.WARNING, "Could not create {0}", newDir);
            throw new DirectoryExistsException("Directory " + directoryName + " already exists");
        }
    }
}
