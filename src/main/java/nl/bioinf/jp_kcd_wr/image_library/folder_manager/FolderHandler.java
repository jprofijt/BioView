package nl.bioinf.jp_kcd_wr.image_library.folder_manager;

import nl.bioinf.jp_kcd_wr.image_library.model.Directory;
import nl.bioinf.jp_kcd_wr.image_library.storage.FileSystemStorageService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that implements folderStructureProvider
 * Handles folder interaction by users
 *
 * @author Jouke Profijt, Kim Chau Duong
 */
@Service
public class FolderHandler implements FolderStructureProvider {
    private final Path rootLocation;
    private static final Logger logger = Logger.getLogger(FileSystemStorageService.class.getName());


    @Autowired
    public FolderHandler(Environment environment){
        this.rootLocation = Paths.get(environment.getProperty("library.upload"));
        logger.log(Level.INFO, "Starting FileSystemStorage service using {0} as root location", new Object[] {this.rootLocation});
    }

    /**
     * Gets the path without the root location
     * @param directory full directory path
     * @return the relative path
     *
     * @author Kim Chau Duong
     */
    private Path getRelativePath(String directory){
        return rootLocation.relativize(Paths.get(directory));
    }

    /**
     * Retrieves the full path, that includes the rootlocation and the given directory string
     * @param directory a relative path
     * @return full directory path
     */
    private Path getFullPath(String directory) { return this.rootLocation.resolve(directory);}

    /**
     * Creates directory object
     * @param directory directory path
     * @return directory object
     *
     * @author Kim Chau Duong
     */
    private Directory createDirectoryObject(File directory){
        String relativeDirectory = getRelativePath(directory.getPath()).toString();
        String directoryName = directory.getName();
        String dateModified = getDateModified(directory);

        return new Directory(relativeDirectory, directoryName, dateModified);
    }

    /**
     * Gets the 'last modified' date of a directory'
     * @param directory
     * @return date String in yyyy-MM-dd HH:mm:ss
     */
    private String getDateModified(File directory) {
        long lastModified = directory.lastModified();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(lastModified);
    }

    /**
     * Gathers all folders present in the current directory
     * @param nextFolders current directory path
     * @return list of directory objects
     *
     * @author Kim Chau duong, Jouke Profijt
     */
    @Override
    public ArrayList<Directory> getNextFolders(String nextFolders){
        File[] directories = new File(String.valueOf(getFullPath(nextFolders))).listFiles(File::isDirectory);

        ArrayList<Directory> directoryList = new ArrayList<>();
        if(directories != null){
            for (File directory : directories){
                directoryList.add(createDirectoryObject(directory));
            }
        }
        return directoryList;
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
        String path = getFullPath(currentPath) + File.separator + directoryName;
        File newDir = new File(path);
        try {
            Files.createDirectory(newDir.toPath());
            newDir.setWritable(true, false);
            newDir.setReadable(true, false);
            newDir.setExecutable(true, false);

        } catch (IOException e){
            e.printStackTrace();
            throw new DirectoryExistsException("Directory " + directoryName + " already exists");
        }

        }


    /**
     * Removes selected folder
     * @param directory the to-be deleted directory
     *
     * @author Kim Chau Duong
     */
    @Override
    public void removeFolder(String directory) {
        Path path = getFullPath(directory);
        try {
            logger.log(Level.INFO, "Deleting directory: {0}", directory);
            FileUtils.deleteDirectory(path.toFile());
            logger.log(Level.INFO, "Successfully deleted {0}!", directory);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Directory {0} could not be deleted", directory);
        }

    }

    /**
     * Moves folder from current location to newly assigned destination
     * @param directory moving directory
     * @param destination paste destination
     *
     * @author Kim Chau Duong
     */
    @Override
    public void moveFolder(String directory, String destination) {
        Path directoryFrom = getFullPath(directory);
        Path directoryTo = getFullPath(destination).resolve(new File(directory).getName());
        try{
            logger.log(Level.INFO,"Moving directory from {0} to {1}", new Object[]{directory, destination});
            Files.move(directoryFrom, directoryTo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.log(Level.WARNING, "File {0} could not be moved", directory);
        }
    }

    /**
     * Copies folder from current location to another destination
     * @param directory copied directory
     * @param destination paste destination
     *
     * @author Kim Chau duong
     */
    @Override
    public void copyFolder(String directory, String destination) {
        Path directoryFrom = getFullPath(directory);
        Path directoryTo = getFullPath(destination).resolve(new File(directory).getName());
        try{
            logger.log(Level.INFO,"Copying directory from {0} to {1}", new Object[]{directory, destination});
            Files.copy(directoryFrom, directoryTo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.log(Level.WARNING, "File {0} could not be copied", directory);
        }
    }



}