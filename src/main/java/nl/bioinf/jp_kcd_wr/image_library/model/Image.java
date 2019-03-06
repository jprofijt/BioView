package nl.bioinf.jp_kcd_wr.image_library.model;

public class Image {
    // id, orig_name, hash_name, path
    private int id;
    private String origName;
    private String hash_name;
    private String path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigName() {
        return origName;
    }

    public void setOrigName(String origName) {
        this.origName = origName;
    }

    public String getHash_name() {
        return hash_name;
    }

    public void setHash_name(String hash_name) {
        this.hash_name = hash_name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Image(int id, String origName, String hash_name, String path) {
        this.id = id;
        this.origName = origName;
        this.hash_name = hash_name;
        this.path = path;
    }

    public Image() {
    }
}
