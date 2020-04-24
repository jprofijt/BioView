package nl.bioinf.jp_kcd_wr.image_library.service;

import nl.bioinf.jp_kcd_wr.image_library.Model.*;

import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ImageMetaDataService implements ImageDataService{
    private final ImageDataSource imageDataSource;

    @Autowired
    public ImageMetaDataService(ImageDataSource imageDataSource){
        this.imageDataSource = imageDataSource;
    }


    @Override
    public List<CompleteRoi> searchRegionOfInterest(HashMap<String, Range> searchRanges, List<String> tags, int page, int size) {
        return imageDataSource.searchRegionOfInterest(searchRanges, tags, page, size);
    }

    @Override
    public List<ImageAttribute> getAllDataFromPath(String path) {
        return imageDataSource.showDataFromPath(path);
    }

    public ImageTags getTaggedImage(int id) {
        return imageDataSource.getTaggedImage(id);
    }

    @Override
    public List<String> getAvailableTags() {
        return imageDataSource.getAvailableTags();
    }

    @Override
    public void addNewTag(int image_id, String tag, String uploader) {
        imageDataSource.insertNewTag(image_id, tag, uploader);
    }

    @Override
    public void addNewState(regionOfInterestState regionOfInterestState, int roiID) {
        imageDataSource.addNewRoiState(regionOfInterestState, roiID);
    }

    @Override
    public List<regionOfInterestState> getRoiStatesOfImage(int image_id) {
        return imageDataSource.getRoiStatesOfImage(image_id);
    }

    @Override
    public List<RoiPoint> getPointsByRoiId(int roiId) {
        return imageDataSource.getPointsForRoi(roiId);
    }

    public List<String> getRoiTags(int roiID) {
        return imageDataSource.getRoiTags(roiID);
    }

    @Override
    public void addNewRoiTag(RoiTag roiTag) {
        imageDataSource.addNewRoiTag(roiTag);
    }

    /**
     * Returns list of image attributes that match a given file path
     * @param path path of file
     * @return list of image attributes that match a given file path
     *
     * @author Kim Chau Duong
     */
    @Override
    public List<ImageAttribute> getDataFromFilePath(String path) {
        return imageDataSource.showDataFromFilePath(path);
    }

    @Override
    public void addNewRoi(regionOfInterestState regionOfInterestState) {
        System.out.println(regionOfInterestState.getId() + " " + regionOfInterestState.getCo2() + " " + regionOfInterestState.getO2() + " " + regionOfInterestState.getPh());
        int roi_id = imageDataSource.addNewRoi(regionOfInterestState.getId());
        addNewState(regionOfInterestState, roi_id);

    }

    @Override
    public void removeRoiTag(RoiTag roiTag) {
        imageDataSource.removeRoiTag(roiTag.getId(), roiTag.getTag());
    }

    /**
     * Calls datasource class method to return a list of unique tags of ROIs of an image
     * @param imageId id of image that contains the ROI tags
     * @param page current unique tag page
     * @param size amount of tags shown per page
     * @return list of unique tags of ROIs of an image
     *
     * @author Kim Chau Duong
     */
    @Override
    public List<String> getUniqueImageTags(int imageId, int page, int size) {
        return imageDataSource.getUniqueImageTags(imageId, page, size);
    }

    /**
     * Calls datasource class method to return a list of unique tags of ROIs of images in a folder
     * @param path Folder path that contains images that contain ROI tags
     * @param page current unique tag page
     * @param size amount of tags shown per page
     * @return list of unique tags of all ROIs of all images in a folder
     *
     * @author Kim Chau Duong
     */
    @Override
    public List<String> getUniqueFolderTags(String path, int page, int size) {
        return imageDataSource.getUniqueFolderTags(path, page, size);
    }

}
