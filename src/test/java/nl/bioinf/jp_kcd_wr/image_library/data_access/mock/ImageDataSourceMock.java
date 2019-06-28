package nl.bioinf.jp_kcd_wr.image_library.data_access.mock;

import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import nl.bioinf.jp_kcd_wr.image_library.model.ImageAttribute;

import java.nio.file.Path;
import java.util.List;


/**
 * Datasource mocking object for testing purposes
 *
 * @author Jouke Profijt
 */
public class ImageDataSourceMock implements ImageDataSource {
    @Override
    public void insertImage(Image image) {

    }

    @Override
    public List<Image> getImagesInDirectory(String directory) {
        return null;
    }

    @Override
    public List<Image> returnAllImages() {
        return null;
    }

    @Override
    public int getImageIdFromPath(String path) {
        return 0;
    }

    @Override
    public void storeThumbnailCacheDataPath(int imageId, Path cacheLocation) {

    }

    @Override
    public boolean checkThumbnailStatus(int ImageId) {
        return false;
    }

    @Override
    public Path getThumbnailPath(int ImageId) {
        return null;
    }

    @Override
    public Path getThumbnailPathFromImagePath(String PathToImage) {
        return null;
    }

    @Override
    public void insertImageMetaData(ImageAttribute imageAttribute) {
    }

}
