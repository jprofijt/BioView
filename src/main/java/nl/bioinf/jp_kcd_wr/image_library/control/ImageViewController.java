package nl.bioinf.jp_kcd_wr.image_library.control;

import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Controller that handles image view for the users
 *
 * @author Jouke Profijt
 */
@Controller
public class ImageViewController {
    private static final Logger logger = Logger.getLogger(ImageViewController.class.getName());
    private final StorageService storageService;

    @Autowired
    public ImageViewController(StorageService storageService) {this.storageService = storageService;}

    @GetMapping("/imageview")
    public String getImages(@RequestParam(name="location", required = false, defaultValue = "") String location, Model model) {
        logger.log(Level.INFO, "Creating Image view for images in {0}", location);

        model.addAttribute("Images", storageService.loadAll(location).map(
                path -> MvcUriComponentsBuilder.fromMethodName(DirectoryController.class,
                        "serveFile", path.getFileName().toString(),location).build().toString())
                .collect(Collectors.toList()));

        return "main-page";
    }
}
