package nl.bioinf.jp_kcd_wr.image_library.control;

import nl.bioinf.jp_kcd_wr.image_library.folder_manager.FolderHandler;
import nl.bioinf.jp_kcd_wr.image_library.Model.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * REST Controller for APIs responding to ajax requests
 *
 * @author Jouke Profijt, Kim Chau Duong
 */
@RestController
@RequestMapping("/api")
public class apiController {
    private final FolderHandler folderHandler;

    @Autowired
    public apiController(FolderHandler folderHandler) {
        this.folderHandler = folderHandler;
    }

    /**
     * Looks up all the subdirectories present in a directory
     * @param parent parent directory that contains subdirectories
     * @return a list os subdirectories of the given directory
     *
     * @author Kim Chau Duong
     */
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
