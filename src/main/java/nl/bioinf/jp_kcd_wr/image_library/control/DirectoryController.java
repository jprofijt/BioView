package nl.bioinf.jp_kcd_wr.image_library.control;

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


@Controller
public class DirectoryController {
    private final StorageService storageService;
    private final FolderHandler folderHandler;

    @Autowired
    public DirectoryController(StorageService storageService, FolderHandler folderHandler) {
        this.storageService = storageService;
        this.folderHandler = folderHandler;
    }

    private static final Logger logger = Logger.getLogger(DirectoryController.class.getName());
    @PostMapping("/createfolder")
    public String CreateFolder(@RequestParam(name="directoryName", required=true) String directoryName, @RequestParam(name="currentPath", required=true) String currentPath, Model model) {
//        FolderHandler creator = new FolderHandler();
        try {
            folderHandler.createNewFolder(directoryName, currentPath);
        } catch (DirectoryExistsException e) {
            model.addAttribute("error", e.getMessage());
            logger.log(Level.WARNING, "{0} already exist", new Object[]{directoryName});
            return "directory-error";
        }
        model.addAttribute("folders", folderHandler.getNextFolders(currentPath));
        model.addAttribute("currentPath", new File(currentPath));
        logger.log(Level.INFO, "Folders were created successfully!");
        return "folders";
    }

    @PostMapping("/createdatefolder")
    public String createDateFolder(@RequestParam(name="currentPath", required=true) String currentPath, Model model){
        try {
            folderHandler.createDateDirectory(currentPath);
        } catch (DirectoryExistsException e){
            model.addAttribute("error", e.getMessage());
            logger.log(Level.WARNING, "{0} already exists", new Object[]{currentPath});
            return "directory-error";
        }
        model.addAttribute("folders", folderHandler.getNextFolders(currentPath));
        model.addAttribute("currentPath", new File(currentPath));
        logger.log(Level.INFO, "Successfully created {0}", new Object[]{currentPath});
        return "folders";
    }

    @GetMapping("/nextfolder")
    public String nextFolder(@RequestParam(name="folder", required=false, defaultValue="") String folder, Model model) {
        model.addAttribute("folders", folderHandler.getNextFolders(folder));
        model.addAttribute("currentPath", new File(folder));
        model.addAttribute("date", LocalDate.now().toString());

        model.addAttribute("files", storageService.loadAll(folder).map(
                path -> MvcUriComponentsBuilder.fromMethodName(DirectoryController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        return "folders";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}

