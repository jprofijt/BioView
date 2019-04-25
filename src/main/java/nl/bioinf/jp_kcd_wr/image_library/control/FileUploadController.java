package nl.bioinf.jp_kcd_wr.image_library.control;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import nl.bioinf.jp_kcd_wr.image_library.storage.StorageFileNotFoundException;
import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;

/**
 * Controller class that handles file upload requests from the upload page
 *
 * @author Kim Chau Duong
 * @version 1.0
 */
@Controller
public class FileUploadController {
    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/tiff");
    private final StorageService storageService;
    private static final Logger logger = Logger.getLogger(FileUploadController.class.getName());

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Get request that loads the initial page
     *
     * @param model
     * @return
     * @throws IOException
     */
    @GetMapping("/upload")
    public String listUploadedFiles(Model model) throws IOException {

        return "upload-form";
    }

    /**
     * Post request that handles file uploads
     *
     * @param file the file to be uploaded
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        String filename = file.getOriginalFilename();
        String fileContentType = file.getContentType();
        if(contentTypes.contains(fileContentType)){
            storageService.store(file);
            logger.log(Level.INFO, "Succesfully uploaded {0}", new Object[]{filename});
            redirectAttributes.addFlashAttribute("upload_message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");
        } else {
            logger.log(Level.WARNING, "Incompatible fileupload, file = {0}", new Object[]{filename});
            redirectAttributes.addFlashAttribute("upload_message",
                    file.getOriginalFilename() +
                            " is of an incorrect file type. Please provide an image file with a .png, .jpeg or .tiff extension!");
        }
        return "redirect:/upload";
    }

    /**
     * Handles file not found error
     * @param exc
     * @return
     */
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
