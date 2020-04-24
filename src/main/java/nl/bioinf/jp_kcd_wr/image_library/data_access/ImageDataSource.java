package nl.bioinf.jp_kcd_wr.image_library.data_access;

import nl.bioinf.jp_kcd_wr.image_library.Model.*;

import java.nio.file.Path;
import java.util.HashMap;
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

    /**
     * Retrieves image attributes of all images in a given folder path
     * @param path folder location
     * @return ImageAttribute list
     *
     * @author Kim Chau Duong
     */
    List<ImageAttribute> showDataFromPath(String path);

    ImageTags getTaggedImage(int id);

    List<String> getAvailableTags();

    void insertNewTag(int Image, String tag, String user);

    void addNewRoiState(regionOfInterestState regionOfInterestState, int roiId);

    int addNewRoi(int imageId);

    List<regionOfInterestState> getRoiStatesOfImage(int image_id);


    List getPointsForRoi(int roi_id);

    List<String> getRoiTags(int roiID);

    void addNewRoiTag(RoiTag roiTag);

    /**
     * Retrieved image attributes of file with given path
     * @param path file path of the image
     * @return List of ImageAttribute
     *
     * @author Kim Chau Duong
     */
    List<ImageAttribute> showDataFromFilePath(String path);


    void removeRoiTag(int id, String tag);


    List<CompleteRoi> searchRegionOfInterest(HashMap<String, Range> searchRanges, List<String> tags, int page, int size);

    /**
     * Retrieves all unique tags from all ROIs of an image
     * @param id image id
     * @return List of tags
     *
     * @author Kim Chau Duong
     */
    List<String> getUniqueImageTags(int id, int page, int size);

    /**
     * Retrieves all unique tags from all ROIs of all images residing in the given directory
     * @param path path to directory
     * @return List of tags
     *
     * @author Kim Chau Duong
     */
    List<String> getUniqueFolderTags(String path, int page, int size);

}
