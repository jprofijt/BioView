package nl.bioinf.jp_kcd_wr.image_library.folder_manager;

import nl.bioinf.jp_kcd_wr.image_library.model.Directory;

import java.io.IOException;
import java.util.ArrayList;

/**
 * interface for directory interactions
 *
 * @author Jouke Profijt
 */
public interface FolderStructureProvider {

    /**
     * Get the folders containing directorty the user specified
     * @param nextFolder directory to search
     * @return arraylist of directories
     */
    ArrayList<Directory> getNextFolders(String nextFolder);

    /**
     * Creates a new directory in th current directory
     * @param directoryName name of new directory
     */
    void createNewFolder(String directoryName, String currentPath) throws DirectoryExistsException, IOException;

    /**
     * Remove directory if the user has the permissions and if the directory is empty
     * @param pathToDirectory path to directory that needs to be removed
     */
    void removeFolder(String pathToDirectory);

    /**
     * Moves directory from current location to newly assigned destination
     * @param directory directory that's to be moved
     */
    void moveFolder(String directory, String destination);
}
