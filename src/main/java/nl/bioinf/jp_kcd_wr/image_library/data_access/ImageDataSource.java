package nl.bioinf.jp_kcd_wr.image_library.data_access;

import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import nl.bioinf.jp_kcd_wr.image_library.model.ImageAttribute;

import java.nio.file.Path;
import java.util.List;

/**
 * Interface for Image data sources
 *
 * Copyright (c) 2019 Kim Chau Duong
 * All rights reserved
 */
public interface ImageDataSource {
    void insertImage(Image image);

    List<Image> getImagesInDirectory(String directory);

    List<Image> returnAllImages();

    int getImageIdFromPath(String path);

    void storeThumbnailCacheDataPath(int imageId, Path cacheLocation);

    boolean checkThumbnailStatus(int ImageId);

    Path getThumbnailPath(int ImageId);

    Path getThumbnailPathFromImagePath(String PathToImage);

    void insertImageMetaData(ImageAttribute imageAttribute);
}
