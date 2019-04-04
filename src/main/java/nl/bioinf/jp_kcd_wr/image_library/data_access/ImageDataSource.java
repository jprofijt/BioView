package nl.bioinf.jp_kcd_wr.image_library.data_access;

import nl.bioinf.jp_kcd_wr.image_library.model.Image;

import java.util.ArrayList;
import java.util.List;

public interface ImageDataSource {
    void insertImage(Image image);

    Image getOrigNamebyHashName(String hash);

    ArrayList<Image> getImagesInDirectory(String directory);

//    List<Image> returnAllImages();
}
