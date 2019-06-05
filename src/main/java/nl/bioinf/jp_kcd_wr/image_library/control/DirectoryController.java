package nl.bioinf.jp_kcd_wr.image_library.control;

import nl.bioinf.jp_kcd_wr.image_library.folder_manager.DirectoryExistsException;
import nl.bioinf.jp_kcd_wr.image_library.folder_manager.FolderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Array;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


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
        folderHandler.removeFolder(directory);
        return "success";
    }

    @PostMapping("/movefolder")
    public String moveFolder(@RequestParam String currentPath, @RequestParam(name = "movingFolders") List<String> folders, @RequestParam(name = "ft_1_active") String destination, RedirectAttributes redirectAttributes) {
        if(null != folders && folders.size() > 0) {
            logger.log(Level.INFO, "Moving folder(s)...");
            for (String folder : folders) {
                folderHandler.moveFolder(folder, destination);
            }
            logger.log(Level.INFO, "Finished moving folder(s)!");
        }
        return "redirect:/imageview?location=" + currentPath.replace("\\", "/");
    }




}

