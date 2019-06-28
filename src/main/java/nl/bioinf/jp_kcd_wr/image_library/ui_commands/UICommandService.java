package nl.bioinf.jp_kcd_wr.image_library.ui_commands;

import nl.bioinf.jp_kcd_wr.image_library.folder_manager.DirectoryExistsException;

/**
 * Interface for file commands
 *
 * @author Kim Chau Duong
 */
public interface UICommandService {

    /**
     * Removes directory or folder
     * @param source path to directory or folder that needs to be removed
     */
    boolean removeFile(String source);

    /**
     * Moves or copies directory from current location to newly assigned destination
     * @param source directory that's to be moved
     */
    boolean moveFile(String source, String destination);

    /**
     * copies given source directory or folder from current location to another destination
     * @param source copied file or directory
     * @param destination paste destination
     */
    boolean copyFile(String source, String destination);

    /**
     * Renames given source to its new name
     * @param source given source to be renamed
     * @param renamedFileName new name for the source
     */
    boolean renameFile(String source, String renamedFileName);

    /**
     * Creates a new directory in th current directory
     * @param directoryName name of new directory
     *
     * @author Jouke Profijt
     */
    void createNewFolder(String directoryName, String currentPath) throws DirectoryExistsException;
}
