package nl.bioinf.jp_kcd_wr.image_library.Model;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Model object for multiple file upload
 *
 * @auhtor Jouke Profijt
 */
public class fileUploadForm {
    private List<MultipartFile> files;

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}
