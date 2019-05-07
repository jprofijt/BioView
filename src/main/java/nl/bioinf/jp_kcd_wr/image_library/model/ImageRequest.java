package nl.bioinf.jp_kcd_wr.image_library.model;

import java.nio.file.Path;

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
