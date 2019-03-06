package nl.bioinf.jp_kcd_wr.image_library.filebrowser;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class FolderCreationController {
    @PostMapping("/createfolder")
    public String CreateFolder(@RequestParam(name="directoryName", required=true) String directoryName, @RequestParam(name="currentPath", required=true) String currentPath, Model model) {
        FolderHandler creator = new FolderHandler();
        try {
            creator.createNewFolder(directoryName, currentPath);
        } catch (DirectoryExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "directory-error";
        }
        model.addAttribute("folders", creator.getNextFolders(currentPath));
        model.addAttribute("currentPath", currentPath);
        return "folders";
    }

    @PostMapping("/createdatefolder")
    public String createDateFolder(@RequestParam(name="currentPath", required=true) String currentPath, Model model){
        FolderHandler dateCreator = new FolderHandler();
        try {
            dateCreator.createDateDirectory(currentPath);
        } catch (DirectoryExistsException e){
            model.addAttribute("error", e.getMessage());
            return "directory-error";
        }
        model.addAttribute("folders", dateCreator.getNextFolders(currentPath));
        model.addAttribute("currentPath", currentPath);
        return "folders";
    }

    }

