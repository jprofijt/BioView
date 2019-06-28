package nl.bioinf.jp_kcd_wr.image_library.data_access;

import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import nl.bioinf.jp_kcd_wr.image_library.model.ImageAttribute;

import java.nio.file.Path;
import java.util.List;

/**
 * Interface for Image data sources
 * Handles all DAO processes
 *
 * @author Kim Chau Duong, Jouke Profijt
 * @version 1.0
 */
public interface ImageDataSource {

    /**
     * Inserts a new image object into the database
     * @param image Image object
     * @author Jouke Profijt
     */
    void insertImage(Image image);

    /**
     * gets the id for the image that is located in path
     * @param path image path
     * @return image id
     * @author Jouke Profijt
     */
    int getImageIdFromPath(String path);

    /**
     * inserts image cache location
     * @param imageId id for the image to be cached
     * @param cacheLocation location for cached image
     * @author Jouke Profijt
     */
    void storeThumbnailCacheDataPath(int imageId, Path cacheLocation);

    /**
     * checks if image has been cached yet
     * @param ImageId image id
     * @return boolean if the image is cached
     * @author Jouke Profijt
     */
    boolean checkThumbnailStatus(int ImageId);

    /**
     * gets the cache for an image by their path
     * @param PathToImage Path
     * @return Cache path
     * @author Jouke Profijt
     */
    Path getThumbnailPathFromImagePath(String PathToImage);

    /**
     * Inserts new image attribute data
     * @param imageAttribute object containing all image attributes
     *
     * @author Jouke Profijt, Kim Chau Duong
     */
    void insertImageMetaData(ImageAttribute imageAttribute);
}
