package nl.bioinf.jp_kcd_wr.image_library.data_access;

import nl.bioinf.jp_kcd_wr.image_library.model.Image;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019 Kim Chau Duong
 * All rights reserved
 */
public interface ImageDataSource {
    void insertImage(Image image);

    Image getOrigNamebyHashName(String hash);

    List<Image> getImagesInDirectory(String directory);

    List<Image> returnAllImages();

    int getImageIdFromPath(String path);

    void insertCache(int imageId, Path cacheLocation);

    boolean isCached(int ImageId);
}
