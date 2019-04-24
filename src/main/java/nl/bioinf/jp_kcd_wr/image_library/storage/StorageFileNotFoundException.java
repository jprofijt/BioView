package nl.bioinf.jp_kcd_wr.image_library.storage;

/**
 * Class that handles file not found exceptions
 *
 * @author Kim Chau Duong
 * @version 1.0
 */
public class StorageFileNotFoundException extends StorageException {

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
