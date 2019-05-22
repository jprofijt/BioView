package nl.bioinf.jp_kcd_wr.image_library.control;

import nl.bioinf.jp_kcd_wr.image_library.breadcrumbs.BreadcrumbBuilder;
import nl.bioinf.jp_kcd_wr.image_library.filebrowser.DirectoryExistsException;
import nl.bioinf.jp_kcd_wr.image_library.filebrowser.FolderHandler;
import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller that handles storage interaction by users
 *
 * @author Jouke Profijt, Kim Chau Duong
 *
 * @version 1.0
 */
@Controller
public class DirectoryController {
    private final StorageService storageService;
    private final FolderHandler folderHandler;
    private final BreadcrumbBuilder breadcrumbBuilder;

    @Autowired
    public DirectoryController(StorageService storageService, FolderHandler folderHandler, BreadcrumbBuilder breadcrumbBuilder) {
        this.storageService = storageService;
        this.folderHandler = folderHandler;
        this.breadcrumbBuilder = breadcrumbBuilder;
    }

    private static final Logger logger = Logger.getLogger(DirectoryController.class.getName());

    /**
     * Mapping for the users to create directories
     * @param directoryName Name for the new directory
     * @param currentPath Path where the new directory will go
     * @param model request model
     * @return redirect to current page
     *
     * @author Jouke Profijt, Kim Chau Duong
     */
    @PostMapping("/createfolder")
    public String CreateFolder(@RequestParam(name="directoryName", required=true) String directoryName, @RequestParam(name="currentPath", required=true) String currentPath, Model model) {
//        FolderHandler creator = new FolderHandler();
        try {
            folderHandler.createNewFolder(directoryName, currentPath);
        } catch (DirectoryExistsException e) {
            model.addAttribute("error", e.getMessage());
            logger.log(Level.WARNING, "Folder {0} in {1} already exist", new Object[]{directoryName, currentPath});
            return "directory-error";
        }
        model.addAttribute("folders", folderHandler.getNextFolders(currentPath));
        model.addAttribute("currentPath", new File(currentPath.replace("\\", "/")));
        logger.log(Level.INFO, "Folders were created successfully!");
        return "redirect:/imageview?location=" + currentPath.replace("\\", "/");
    }

    /**
     * mapping that lets users create a folder with the data as name
     * @param currentPath path where directory should be located
     * @param model request model
     * @return redirect to current page
     *
     * @author Jouke Profijt
     */
    @PostMapping("/createdatefolder")
    public String createDateFolder(@RequestParam(name="currentPath", required=true) String currentPath, Model model){
        try {
            folderHandler.createDateDirectory(currentPath);
        } catch (DirectoryExistsException e){
            model.addAttribute("error", e.getMessage());
            logger.log(Level.WARNING, "Current date folder in {0} already exists", new Object[]{currentPath});
            return "directory-error";
        }
        model.addAttribute("folders", folderHandler.getNextFolders(currentPath));
        model.addAttribute("currentPath", new File(currentPath.replace("\\", "/")));
        logger.log(Level.INFO, "Successfully created folder in {0}", new Object[]{currentPath});
        return "redirect:/imageview?location=" + currentPath.replace("\\", "/");
    }

    /**
     * Get request that provides all folders, files and the current path
     * @param folder current directory path
     * @param model
     * @return Folders contained in current view
     *
     * @author Jouke Profijt, Kim Chau Duong
     */
    @GetMapping("/nextfolder")
    public String nextFolder(@RequestParam(name="folder", required=false, defaultValue="") String folder, Model model) {
        model.addAttribute("folders", folderHandler.getNextFolders(folder));
        model.addAttribute("currentPath", new File(folder.replace("\\", "/")));
        model.addAttribute("date", LocalDate.now().toString());

        model.addAttribute("files", storageService.loadAll(folder).map(
                path -> MvcUriComponentsBuilder.fromMethodName(DirectoryController.class,
                        "serveFile", path.getFileName().toString(), folder.replace("\\", "/")).build().toString())
                .collect(Collectors.toList()));
        model.addAttribute("breadcrumbs", breadcrumbBuilder.getBreadcrumbs(folder));
        return "folders";
    }

    /**
     * Loads file body
     * @param filename given filename
     * @return file body
     *
     * @author Kim Chau Duong
     */
    /*@GetMapping("/files/{directory}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String directory, @PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename, directory);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename, directory);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/cache/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveThumbnail(@PathVariable String filename) {

        Resource thumbnail = storageService.loadThumbnailAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + thumbnail.getFilename() + "\"").body(thumbnail);

    }*/

}

