package nl.bioinf.jp_kcd_wr.image_library.Model;

/**
 * Image Model
 * Stored image data such as the image id, old image name preupload, new name post upload and image path
 * Used to store information in the database
 *
 * @author Kim Chau Duong
 */
public class Image {
    private int id;
    private String origName;
    private String NewFilename;
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

    public String getNewFilename() {
        return NewFilename;
    }

    public void setNewFilename(String newFilename) {
        this.NewFilename = newFilename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Image(int id, String origName, String NewFilename, String path) {
        this.id = id;
        this.origName = origName;
        this.NewFilename = NewFilename;
        this.path = path;
    }

    public Image() {
    }
}
