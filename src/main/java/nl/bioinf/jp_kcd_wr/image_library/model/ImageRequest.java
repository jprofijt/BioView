package nl.bioinf.jp_kcd_wr.image_library.model;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

/**
 * Image request object, couples thumbnail locaton and actual location so user can access in image editor
 * @author Jouke Profijt
 */
public class ImageRequest {
    private Path Thumbnail;
    private Path Actual;
    private int id;
    private Path name;
    private String date;

    public Path getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(Path thumbnail) {
        Thumbnail = thumbnail;
    }

    public Path getActual() {
        return Actual;
    }

    public void setActual(Path actual) {
        Actual = actual;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Path getName() {
        return name;
    }

    public void setName(Path name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
