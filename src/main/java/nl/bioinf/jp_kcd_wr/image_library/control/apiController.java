package nl.bioinf.jp_kcd_wr.image_library.control;

import nl.bioinf.jp_kcd_wr.image_library.folder_manager.FolderHandler;
import nl.bioinf.jp_kcd_wr.image_library.model.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequestMapping("/api")
public class apiController {
    private final FolderHandler folderHandler;

    @Autowired
    public apiController(FolderHandler folderHandler) {
        this.folderHandler = folderHandler;
    }

    @GetMapping("/tags/{image}")
    public String test(@PathVariable("image") int image_id){
        System.out.println(image_id);
        return "";
    }


    @GetMapping("/folder/branch")
    public ResponseEntity<ArrayList<Directory>> getFolderBranches(@RequestParam String parent){
        try {
            if (parent.contains("\\")){
                return new ResponseEntity("contained backslashes",HttpStatus.BAD_REQUEST);
            }
            ArrayList<Directory> data = folderHandler.getNextFolders(parent);
            return new ResponseEntity(data, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
