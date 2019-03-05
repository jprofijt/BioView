package nl.bioinf.jp_kcd_wr.image_library.filebrowser;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FolderStructureController {
    @GetMapping("/nextfolder")
    public String NextFolder(@RequestParam(name="folder", required=false, defaultValue="testdata") String folder, Model model) {
        FolderHandler folderFinder = new FolderHandler();
        model.addAttribute("folders", folderFinder.getNextFolders(folder));
        model.addAttribute("currentPath", folder);
        return "folders";
    }



}

