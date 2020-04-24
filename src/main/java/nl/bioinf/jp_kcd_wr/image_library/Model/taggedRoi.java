package nl.bioinf.jp_kcd_wr.image_library.Model;

import java.util.List;

public class taggedRoi {
    private int id;
    private List<String> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
