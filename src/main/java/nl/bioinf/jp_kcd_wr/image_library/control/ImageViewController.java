package nl.bioinf.jp_kcd_wr.image_library.control;

import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class ImageViewController {
    private static final Logger logger = Logger.getLogger(ImageViewController.class.getName());
    private final StorageService storageService;

    @Autowired
    public ImageViewController(StorageService storageService) {this.storageService = storageService;}

    @GetMapping("/imageview")
    public String getImages(@RequestParam(name="location", required = false, defaultValue = "Coccidia/.cache") String location, Model model) {
        model.addAttribute("Images", storageService.loadAll(location).map(
                path -> MvcUriComponentsBuilder.fromMethodName(DirectoryController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "main-page";
    }
}
