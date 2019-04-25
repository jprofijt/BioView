package nl.bioinf.jp_kcd_wr.image_library.data_access;

import nl.bioinf.jp_kcd_wr.image_library.model.Image;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface for the image data source
 * @author Jouke Profijt, Kim Chau Duong
 */
public interface ImageDataSource {
    void insertImage(Image image);

    Image getOrigNamebyHashName(String hash);

    List<Image> getImagesInDirectory(String directory);

    List<Image> returnAllImages();

    int getImageIdFromPath(String path);

    void insertCache(int imageId, Path cacheLocation);

    boolean isNotCached(int ImageId);

    Path getCache(int ImageId);
}
