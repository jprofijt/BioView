package nl.bioinf.jp_kcd_wr.image_library.control;

import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

@Controller
public class ImageViewController {
    private static final Logger logger = Logger.getLogger(ImageViewController.class.getName());
    private final StorageService storageService;

    @Autowired
    public ImageViewController(StorageService storageService) {this.storageService = storageService;}

    @GetMapping("/imageview")
    public String getImageLocations(Model model) throws IOException {
        ArrayList<Image> images = new ArrayList<Image>();
        for(int i = 1 ; i <= 3; i++) {
            Image image = new Image();
            image.setId(1);
            image.setOrigName("Test.png");
            image.setNewFilename("dummy-200x200.png");
            image.setPath("images/dummy-200x200.png");
            images.add(image);
        }
        model.addAttribute("images", images);

        return "main-page";
    }
}
