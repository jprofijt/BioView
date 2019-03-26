package nl.bioinf.jp_kcd_wr.image_library.control;

import nl.bioinf.jp_kcd_wr.image_library.filebrowser.DirectoryExistsException;
import nl.bioinf.jp_kcd_wr.image_library.filebrowser.FolderHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;


@Controller
public class DirectoryController {
    private static final Logger logger = Logger.getLogger(DirectoryController.class.getName());
    @PostMapping("/createfolder")
    public String CreateFolder(@RequestParam(name="directoryName", required=true) String directoryName, @RequestParam(name="currentPath", required=true) String currentPath, Model model) {
        FolderHandler creator = new FolderHandler();
        try {
            creator.createNewFolder(directoryName, currentPath);
        } catch (DirectoryExistsException e) {
            model.addAttribute("error", e.getMessage());
            logger.log(Level.WARNING, "{0} already exist", new Object[]{directoryName});
            return "directory-error";
        }
        model.addAttribute("folders", creator.getNextFolders(currentPath));
        model.addAttribute("currentPath", currentPath);
        logger.log(Level.INFO, "Folders were created successfully!");
        return "folders";
    }

    @PostMapping("/createdatefolder")
    public String createDateFolder(@RequestParam(name="currentPath", required=true) String currentPath, Model model){
        FolderHandler dateCreator = new FolderHandler();
        try {
            dateCreator.createDateDirectory(currentPath);
        } catch (DirectoryExistsException e){
            model.addAttribute("error", e.getMessage());
            logger.log(Level.WARNING, "{0} already exists");
            return "directory-error";
        }
        model.addAttribute("folders", dateCreator.getNextFolders(currentPath));
        model.addAttribute("currentPath", currentPath);
        logger.log(Level.INFO, "Successfully created {0}", new Object[]{dateCreator.getNextFolders(currentPath)});
        return "folders";
    }

    @GetMapping("/nextfolder")
    public String NextFolder(@RequestParam(name="folder", required=false, defaultValue="testdata") String folder, Model model) {
        FolderHandler folderFinder = new FolderHandler();
        model.addAttribute("folders", folderFinder.getNextFolders(folder));
        model.addAttribute("currentPath", folder);
        model.addAttribute("date", LocalDate.now().toString());
        return "folders";
    }

}

