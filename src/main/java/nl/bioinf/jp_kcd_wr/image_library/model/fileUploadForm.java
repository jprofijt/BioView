package nl.bioinf.jp_kcd_wr.image_library.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class fileUploadForm {
    private List<MultipartFile> files;

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}
