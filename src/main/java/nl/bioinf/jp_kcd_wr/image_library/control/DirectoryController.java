package nl.bioinf.jp_kcd_wr.image_library.control;

import nl.bioinf.jp_kcd_wr.image_library.folder_manager.DirectoryExistsException;
import nl.bioinf.jp_kcd_wr.image_library.folder_manager.FolderHandler;
import nl.bioinf.jp_kcd_wr.image_library.ui_commands.UICommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private final UICommandService uiCommandService;

    @Autowired
    public DirectoryController(FolderHandler folderHandler, UICommandService uiCommandService) {
        this.folderHandler = folderHandler;
        this.uiCommandService = uiCommandService;
    }

    private static final Logger logger = Logger.getLogger(DirectoryController.class.getName());

    /**
     * Mapping for the users to create directories
     * @param directoryName name for the new directory
     * @param currentPath path where the new directory will go
     * @return redirect to current page
     *
     * @author Jouke Profijt, Kim Chau Duong
     */
    @PostMapping("/createfolder")
    public String createFolder(@RequestParam(name="directoryName", required=true) String directoryName, @RequestParam(name="currentPath", required=true) String currentPath, RedirectAttributes redirectAttributes) {
        try {
            logger.log(Level.INFO, "Creating directory");
            folderHandler.createNewFolder(directoryName, currentPath);
            logger.log(Level.INFO, "Folder was created successfully!");
            redirectAttributes.addFlashAttribute("success_messages","[Successfully created " + directoryName + "!]");
        } catch (DirectoryExistsException e) {
            redirectAttributes.addFlashAttribute("error_messages", "["+ directoryName +" already exists!]");
            logger.log(Level.WARNING, "Folder {0} already exist in {1}", new Object[]{directoryName, currentPath});
        }
        return "redirect:/imageview?location=" + currentPath.replace("\\", "/");
    }

    /**
     * Handles folder deleting process
     * @param directory directory that's to be deleted
     * @return confirmation message
     *
     * @author Kim Chau Duong
     */
    @PostMapping("/deletefolder")
    @ResponseBody
    public String deleteFolder(@RequestParam String directory, RedirectAttributes redirectAttributes) throws IOException {
        logger.log(Level.INFO, "Deleting folder...");
        if (uiCommandService.removeFile(directory)){
            return "success";
        } else {
            return "failed";
        }
    }

    /**
     * Handles folder moving process
     * @param currentPath current directory that the user is in and will be redirected to.
     * @param folders list of folders that are to be moved
     * @param destination destination of the moving folders
     * @param redirectAttributes attributes given back to redirected page
     * @return redirect to current page
     *
     * @author Kim Chau Duong
     */
    @PostMapping("/movefolder")
    public String moveFolder(@RequestParam String currentPath, @RequestParam(name = "movedFolders") List<String> folders, @RequestParam(name = "ft_1_active") String destination, RedirectAttributes redirectAttributes) {
        if(null != folders && folders.size() > 0) {
            logger.log(Level.INFO, "Moving folder(s)...");
            for (String folder : folders) {
                uiCommandService.moveFile(folder, destination);
            }
            logger.log(Level.INFO, "Finished moving folder(s)!");
        }
        return "redirect:/imageview?location=" + currentPath.replace("\\", "/");
    }

    /**
     * Handles folder Copying process
     * @param currentPath current directory that the user is in and will be redirected to.
     * @param folders list of folders that are to be copied
     * @param destination destination of the copied folders
     * @param redirectAttributes attributes given back to redirected page
     * @return redirect to current page
     *
     * @author Kim Chau Duong
     */
    @PostMapping("/copyfolder")
    public String copyFolder(@RequestParam String currentPath, @RequestParam(name = "copiedFolders") List<String> folders, @RequestParam(name = "ft_2_active") String destination, RedirectAttributes redirectAttributes) {
        if(null != folders && folders.size() > 0) {
            logger.log(Level.INFO, "Copying folder(s)...");
            for (String folder : folders) {
                uiCommandService.copyFile(folder, destination);
            }
            logger.log(Level.INFO, "Finished copying folder(s)!");
        }
        return "redirect:/imageview?location=" + currentPath.replace("\\", "/");
    }

    /**
     * Handles folder renaming process
     * @param currentPath Current directory that the user is in and will be redirected to.
     * @param directory  Directory that's to be renamed
     * @param newFolderName new name for the directory
     * @param redirectAttributes attributes given back to redirected page
     * @return redirect to current page
     *
     * @author Kim Chau Duong
     */
    @PostMapping("/renamefolder")
    public String renameFolder(@RequestParam String currentPath, @RequestParam(name = "renamedFolder") String directory, @RequestParam(name = "newFolderName") String newFolderName, RedirectAttributes redirectAttributes) {
        logger.log(Level.INFO, "Renaming folder...");
        uiCommandService.renameFile(directory, newFolderName);
        logger.log(Level.INFO, "Finished renaming folder!");
        return "redirect:/imageview?location=" + currentPath.replace("\\", "/");
    }




}

