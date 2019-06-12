package nl.bioinf.jp_kcd_wr.image_library.ui_commands;

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
    void moveFile(String source, String destination);

    /**
     * copies given source directory or folder from current location to another destination
     * @param source copied file or directory
     * @param destination paste destination
     */
    void copyFile(String source, String destination);

    /**
     * Renames given source to its new name
     * @param source given source to be renamed
     * @param renamedFileName new name for the source
     */
    void renameFile(String source, String renamedFileName);
}
