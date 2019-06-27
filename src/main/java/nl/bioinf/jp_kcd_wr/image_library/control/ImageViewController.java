package nl.bioinf.jp_kcd_wr.image_library.control;

import nl.bioinf.jp_kcd_wr.image_library.breadcrumbs.BreadcrumbBuilder;
import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import nl.bioinf.jp_kcd_wr.image_library.folder_manager.FolderHandler;
import nl.bioinf.jp_kcd_wr.image_library.model.ImageRequest;
import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;
import nl.bioinf.jp_kcd_wr.image_library.ui_commands.UICommandService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
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
    private final UICommandService uiCommandService;

    @Autowired
    public ImageViewController(StorageService storageService, ImageDataSource imageDataSourceJdbc, FolderHandler folderHandler, BreadcrumbBuilder breadcrumbBuilder, UICommandService uiCommandService) {
        this.storageService = storageService;
        this.imageDataSource = imageDataSourceJdbc;
        this.folderHandler = folderHandler;
        this.breadcrumbBuilder = breadcrumbBuilder;
        this.uiCommandService = uiCommandService;
    }

    @GetMapping("/imageview")

    public String getImages(@RequestParam(name="location", required = false, defaultValue = "HeadDirectory") String location, Model model) {
        model.addAttribute("folders", folderHandler.getNextFolders(location));
        model.addAttribute("currentPath", new File(location.replace("\\", "/")));
        model.addAttribute("date", LocalDate.now().toString());

        logger.log(Level.INFO, "Creating Image view for images in {0}", location.replace("\\", "/"));
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
            Path path = storageService.getRootLocation().relativize(image);
            imageRequest.setThumbnail(this.imageDataSource.getThumbnailPathFromImagePath(path.toString()));

            imageRequest.setActual(path);
            imageRequest.setId(this.imageDataSource.getImageIdFromPath(path.toString()));
            imageRequest.setName(image.getFileName());
            long lastModified = image.toFile().lastModified();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            imageRequest.setDate(sdf.format(lastModified));


            cacheLocations.add(imageRequest);
        }
        return cacheLocations;
    }

    /**
     * Handles image moving process
     * @param currentPath current directory that the user is in and will be redirected to
     * @param images list of images to be moved
     * @param destination destination folder
     * @param redirectAttributes  attributes given back to redirected page
     * @return redirect to current page
     *
     * @author Kim Chau Duong
     */
    @PostMapping("/moveimage")
    public String moveImage(@RequestParam String currentPath, @RequestParam(name = "movedImages") List<String> images, @RequestParam(name = "ft_3_active") String destination, RedirectAttributes redirectAttributes) {
        if(null != images && images.size() > 0) {
            ArrayList<String> successMessages = new ArrayList<>();
            ArrayList<String> errorMessages = new ArrayList<>();
            logger.log(Level.INFO, "Moving images(s)...");
            for (String image : images) {
                if (uiCommandService.moveFile(image.replace("\\", "/"), destination)) {
                    successMessages.add("Successfully moved " + new File(image).getName() + " to " + destination + "!");
                } else {
                    errorMessages.add("Could not move " + new File(image).getName() + " to " + destination + "!");
                }
            }
            logger.log(Level.INFO, "Finished moving images(s)!");
            redirectAttributes.addFlashAttribute("success_messages",successMessages);
            redirectAttributes.addFlashAttribute("error_messages",errorMessages);
        }
        return "redirect:/imageview?location=" + currentPath.replace("\\", "/");
    }

    /**
     * Handles image copying process
     * @param currentPath current directory that the user is in and will be redirected to
     * @param images list of images to be copied
     * @param destination destination folder
     * @param redirectAttributes attributes given back to redirected page
     * @return redirect to current page
     *
     * @author Kim Chau Duong
     */
    @PostMapping("/copyimage")
    public String copyFolder(@RequestParam String currentPath, @RequestParam(name = "copiedImages") List<String> images, @RequestParam(name = "ft_4_active") String destination, RedirectAttributes redirectAttributes) {
        if(null != images && images.size() > 0) {
            ArrayList<String> successMessages = new ArrayList<>();
            ArrayList<String> errorMessages = new ArrayList<>();
            logger.log(Level.INFO, "Copying images(s)...");
            for (String image : images) {
                if (uiCommandService.copyFile(image.replace("\\", "/"), destination)){
                    successMessages.add("Successfully copied " + new File(image).getName() + " to " + destination + "!");
                } else {
                    errorMessages.add("Could not copy " + new File(image).getName() + " to " + destination + "!");
                }
            }
            logger.log(Level.INFO, "Finished copying images(s)!");
            redirectAttributes.addFlashAttribute("success_messages",successMessages);
            redirectAttributes.addFlashAttribute("error_messages",errorMessages);
        }
        return "redirect:/imageview?location=" + currentPath.replace("\\", "/");
    }

    /**
     * Handles image deleting process
     * @param image image that's to be deleted
     * @return confirmation message
     *
     * @author Kim Chau Duong
     */
    @PostMapping("/deleteimage")
    @ResponseBody
    public String deleteFolder(@RequestParam String image) throws IOException {
        logger.log(Level.INFO, "Deleting image...");
        if (uiCommandService.removeFile(image.replace("\\", "/"))){
            return "success";
        } else {
            return "failed";
        }
    }
}
