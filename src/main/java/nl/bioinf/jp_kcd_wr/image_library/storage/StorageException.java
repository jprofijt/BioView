package nl.bioinf.jp_kcd_wr.image_library.storage;

/**
 * Class that handles storage exceptions
 *
 * @author Kim Chau Duong
 * @version 1.0
 */
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
