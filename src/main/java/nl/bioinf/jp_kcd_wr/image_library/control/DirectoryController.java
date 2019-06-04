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
    private final FolderHandler folderHandler;

    @Autowired
    public DirectoryController(FolderHandler folderHandler) {
        this.folderHandler = folderHandler;
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
    public String createFolder(@RequestParam(name="directoryName", required=true) String directoryName, @RequestParam(name="currentPath", required=true) String currentPath, Model model) {
        try {
            folderHandler.createNewFolder(directoryName, currentPath);
        } catch (DirectoryExistsException e) {
            model.addAttribute("error", e.getMessage());
            logger.log(Level.WARNING, "Folder {0} in {1} already exist", new Object[]{directoryName, currentPath});
            return "directory-error";
        }
//        model.addAttribute("folders", folderHandler.getNextFolders(currentPath));
//        model.addAttribute("currentPath", new File(currentPath.replace("\\", "/")));
        logger.log(Level.INFO, "Folders were created successfully!");
        return "redirect:/imageview?location=" + currentPath.replace("\\", "/");
    }

    @PostMapping("/deletefolder")
    @ResponseBody
    public String deleteFolder(@RequestParam String directory) {
        System.out.println("help");
        folderHandler.removeFolder(directory);
        return "success";
    }




}

