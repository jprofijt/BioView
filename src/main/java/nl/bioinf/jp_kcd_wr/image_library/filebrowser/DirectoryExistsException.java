package nl.bioinf.jp_kcd_wr.image_library.filebrowser;

/**
 * If a directory already exists throw this exception
 *
 * @author Jouke Profijt
 *
 * Exeption is not needed in future when code is corrected.
 */
public class DirectoryExistsException extends Exception {
    public DirectoryExistsException(String message)
    {
        super(message);
    }
}
