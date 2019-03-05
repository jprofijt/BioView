package nl.bioinf.jp_kcd_wr.image_library.filebrowser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface FolderStructureProvider {

    /**
     * Get the folders containing directorty the user specified
     * @param nextFolder
     * @return
     */
    ArrayList<String> getNextFolders(String nextFolder);

    /**
     * Creates a new directory in th current directory
     * @param directoryName
     */
    void createNewFolder(String directoryName, String currentPath) throws DirectoryExistsException, IOException;

    /**
     * Remove directory if the user has the permissions and if the directory is empty
     * @param pathToDirectory
     */
    void removeFolder(File pathToDirectory);


}
