package nl.bioinf.jp_kcd_wr.image_library.service;

import nl.bioinf.jp_kcd_wr.image_library.Model.*;

import java.util.HashMap;
import java.util.List;

public interface ImageDataService {

    List<CompleteRoi> searchRegionOfInterest(HashMap<String, Range> searchRanges, List<String> tags, int page, int size);


    List<ImageAttribute> getAllDataFromPath(String type);


    ImageTags getTaggedImage(int id);

    List<String> getAvailableTags();

    void addNewTag(int image_id, String tag, String uploader);

    void addNewState(regionOfInterestState regionOfInterestState, int roiId);

    List<regionOfInterestState> getRoiStatesOfImage(int image_id);

    List getPointsByRoiId (int roiId);
    List<String> getRoiTags(int roiID);

    void addNewRoiTag(RoiTag roiTag);

    /**
     * Returns list of image attributes that match a given file path
     * @param path path of file
     * @return list of image attributes that match a given file path
     *
     * @author Kim Chau Duong
     */
    List<ImageAttribute> getDataFromFilePath(String path);

    void addNewRoi(regionOfInterestState regionOfInterestState);

    void removeRoiTag(RoiTag roiTag);

    /**
     * Calls datasource class method to return a list of unique tags of ROIs of an image
     * @param imageId id of image that contains the ROI tags
     * @param page current unique tag page
     * @param size amount of tags shown per page
     * @return a list of unique tags of ROIs of an image
     *
     * @author Kim Chau Duong
     */
    List<String> getUniqueImageTags(int imageId, int page, int size);

    /**
     * Calls datasource class method to return a list of unique tags of ROIs of images in a folder
     * @param path Folder path that contains images that contain ROI tags
     * @param page current unique tag page
     * @param size amount of tags shown per page
     * @return list of unique tags of all ROIs of all images in a folder
     *
     * @author Kim Chau Duong
     */
    List<String> getUniqueFolderTags(String path, int page, int size);
}
