package nl.bioinf.jp_kcd_wr.image_library.control;

import nl.bioinf.jp_kcd_wr.image_library.breadcrumbs.BreadcrumbBuilder;
import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import nl.bioinf.jp_kcd_wr.image_library.filebrowser.FolderHandler;
import nl.bioinf.jp_kcd_wr.image_library.model.ImageRequest;
import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.nio.file.Files;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
    private final FolderHandler folderHandler;
    private final BreadcrumbBuilder breadcrumbBuilder;

    @Autowired
    public ImageViewController(StorageService storageService, ImageDataSource imageDataSourceJdbc, FolderHandler folderHandler, BreadcrumbBuilder breadcrumbBuilder) {
        this.storageService = storageService;
        this.imageDataSource = imageDataSourceJdbc;
        this.folderHandler = folderHandler;
        this.breadcrumbBuilder = breadcrumbBuilder;
    }

    @GetMapping("/imageview")

    public String getImages(@RequestParam(name="location", required = false, defaultValue = "HeadDirectory") String location, Model model) {
        model.addAttribute("folders", folderHandler.getNextFolders(location));
        model.addAttribute("currentPath", new File(location.replace("\\", "/")));
        model.addAttribute("date", LocalDate.now().toString());

        logger.log(Level.INFO, "Creating Image view for images in {0}", location);
        List<Path> list = storageService.loadAbsolute(location).collect(Collectors.toList());
        model.addAttribute("Images", loadCaches(list));
        model.addAttribute("cache_path", "../cache/");
        model.addAttribute("location", location);

        model.addAttribute("breadcrumbs", breadcrumbBuilder.getBreadcrumbs(location));

        return "main-page";
    }

    /**
     * Creates imageRequest object list for displaying images in directory
     * @param image_paths
     * @return List of ImageRequest Objects
     *
     */
    private List<ImageRequest> loadCaches(List<Path> image_paths){
        ArrayList<ImageRequest> cacheLocations = new ArrayList<>();
        for (Path image: image_paths) {
            ImageRequest imageRequest = new ImageRequest();

            imageRequest.setThumbnail(this.imageDataSource.getThumbnailPathFromImagePath(image.toString()));
            imageRequest.setActual(
                    Paths.get(FilenameUtils.separatorsToUnix(image.toString()).replace(FilenameUtils.separatorsToUnix(storageService.getRootLocation().toString()) + "/", "")));
            imageRequest.setId(this.imageDataSource.getImageIdFromPath(image.toString()));

            cacheLocations.add(imageRequest);
        }
        return cacheLocations;
    }
}
