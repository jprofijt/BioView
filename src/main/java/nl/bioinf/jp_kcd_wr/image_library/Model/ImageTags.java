package nl.bioinf.jp_kcd_wr.image_library.Model;

import java.util.List;

public class ImageTags {
    private int id;
    private List<String> tags;

    public ImageTags(int id, List<String> tags) {
        this.id = id;
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public int getId() {
        return id;
    }
}
