package nl.bioinf.jp_kcd_wr.image_library.model;

import java.nio.file.Path;

/**
 * Image request object, couples thumbnail locaton and actual location so user can access in image editor
 * @author Jouke Profijt
 */
public class ImageRequest {
    private Path Thumbnail;
    private Path Actual;

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
}
