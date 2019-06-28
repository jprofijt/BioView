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
     *
     * @author Jouke Profijt, Kim Chau Duong
     */
    ArrayList<Directory> getNextFolders(String nextFolder);



}
