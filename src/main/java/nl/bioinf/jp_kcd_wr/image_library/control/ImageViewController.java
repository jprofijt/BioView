package nl.bioinf.jp_kcd_wr.image_library.control;

import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import nl.bioinf.jp_kcd_wr.image_library.model.ImageRequest;
import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
    private final ImageDataSource imageDataSource;

    @Autowired
    public ImageViewController(StorageService storageService, ImageDataSource imageDataSourceJdbc) {
        this.storageService = storageService;
        this.imageDataSource = imageDataSourceJdbc;
    }

    @GetMapping("/imageview")
    public String getImages(@RequestParam(name="location", required = false, defaultValue = "") String location, Model model) {
        logger.log(Level.INFO, "Creating Image view for images in {0}", location);
        List<Path> list = storageService.loadAbsolute(location).collect(Collectors.toList());
        model.addAttribute("Images", loadCaches(list));
        model.addAttribute("cache_path", "../cache/");
        model.addAttribute("location", location);

        return "main-page";
    }

    /**
     * Creates imageRequest object list for displaying images in directory
     * @param image_paths
     * @return List of ImageRequest Objects
     */
    private List<ImageRequest> loadCaches(List<Path> image_paths){
        ArrayList<ImageRequest> cacheLocations = new ArrayList<>();
        for (Path image: image_paths) {
            ImageRequest imageRequest = new ImageRequest();

            imageRequest.setThumbnail(this.imageDataSource.getCacheFromImagePath(image.toString()));
            imageRequest.setActual(
                    Paths.get(image.toString().replace(storageService.getRootLocation().toString() + "/", "")));
            System.out.println("image = " + imageRequest.getActual());

            cacheLocations.add(imageRequest);
        }
        return cacheLocations;
    }
}
