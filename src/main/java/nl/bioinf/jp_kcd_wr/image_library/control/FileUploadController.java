package nl.bioinf.jp_kcd_wr.image_library.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.bioinf.jp_kcd_wr.image_library.model.fileUploadForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import nl.bioinf.jp_kcd_wr.image_library.storage.StorageFileNotFoundException;
import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller class that handles file upload requests from the upload page
 *
 * @author Kim Chau Duong, Jouke Profijt
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

    /*
     * Post request that handles file uploads
     *
     * @param file the file to be uploaded
     * @param redirectAttributes
     * @return
     */
    /*@PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam(name="directory", required=false, defaultValue="") File directory,
                                   RedirectAttributes redirectAttributes) {
        String filename = file.getOriginalFilename();
        String fileContentType = file.getContentType();
        if(contentTypes.contains(fileContentType)){
            storageService.store(file, directory);
            logger.log(Level.INFO, "Succesfully uploaded {0} in {1}", new Object[]{filename, directory.toString()});
            redirectAttributes.addFlashAttribute("upload_message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");
        } else {
            logger.log(Level.WARNING, "Incompatible fileupload, file = {0}", new Object[]{filename});
            redirectAttributes.addFlashAttribute("upload_message",
                    file.getOriginalFilename() +
                            " is of an incorrect file type. Please provide an image file with a .png, .jpeg or .tiff extension!");
        }
        return "redirect:/upload";*/
    //}

    /**
     * handles multiple file uploads
     * @param directory directory where uploads are taking place
     * @param uploadForm uploaded form data list
     *
     * @return redirect to main page
     */
    @PostMapping("/multiFileUpload")
    public String save(@RequestParam(name = "directory", required = false, defaultValue = "") File directory, @RequestParam("file") List<MultipartFile> uploadForm, RedirectAttributes redirectAttributes) {
        ArrayList<String> successMessages = new ArrayList<>();
        ArrayList<String> errorMessages = new ArrayList<>();
        if (null != uploadForm && uploadForm.size() > 0) {
            for (MultipartFile multipartFile : uploadForm) {
                String filename = multipartFile.getOriginalFilename();
                String fileContentType = multipartFile.getContentType();
                if (contentTypes.contains(fileContentType)) {
                    storageService.store(multipartFile, directory);
                    logger.log(Level.INFO, "Succesfully uploaded {0} in {1}", new Object[]{filename, directory.toString()});
                    successMessages.add("Successfully uploaded " + filename + "!");
                } else {
                    logger.log(Level.WARNING, "Incompatible fileupload, file = {0}", new Object[]{filename});
                    errorMessages.add("Incompatible fileupload: " + filename);
                }
            }
            redirectAttributes.addFlashAttribute("success_messages",successMessages);
            redirectAttributes.addFlashAttribute("error_messages",errorMessages);
        }
        return "redirect:/imageview?location=" + directory.toString().replace("\\", "/");
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

