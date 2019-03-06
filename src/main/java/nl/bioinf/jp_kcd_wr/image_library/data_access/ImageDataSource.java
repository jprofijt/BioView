package nl.bioinf.jp_kcd_wr.image_library.data_access;

import nl.bioinf.jp_kcd_wr.image_library.model.Image;

public interface ImageDataSource {
    void insertImage(int id, String origName, String hashName, String path);

    Image getOrigNamebyHashName(String hash);
}
