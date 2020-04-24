package nl.bioinf.jp_kcd_wr.image_library.control;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import nl.bioinf.jp_kcd_wr.image_library.Model.*;
import nl.bioinf.jp_kcd_wr.image_library.Model.ResponseErrors.NormalErrorResponse;
import nl.bioinf.jp_kcd_wr.image_library.Model.ResponseErrors.RangeError;


import nl.bioinf.jp_kcd_wr.image_library.hateos_resource.UniqueTags;
import nl.bioinf.jp_kcd_wr.image_library.service.ImageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.HashMap;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Main api Controller for getting image data
 *
 * @author Jouke Profijt, Kim Chau Duong, Wietse Reitsema
 */
@RestController
@RequestMapping("/api")
public class ImageDataController {
    private final ImageDataService imageDataService;

    @Autowired
    public ImageDataController(ImageDataService imageDataService){
        this.imageDataService = imageDataService;
    }

    /**
     * Gets attributes of all images in a given folder path
     * @param path folder path
     * @return json list of image attributes
     *
     * @author Kim Chau Duong
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/metadata/path")
    public ResponseEntity getDataFromPath(@RequestParam String path){
        try {
            if (path.contains("\\")){
                return new ResponseEntity<>("contained backslashes",HttpStatus.BAD_REQUEST);
            }
            List<ImageAttribute> data = imageDataService.getAllDataFromPath(path);
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets all image attribute data of the image with the specified filepath
     * @param path image file path
     * @return image attribute json
     *
     * @author Kim Chau Duong
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/metadata/filepath")
    public ResponseEntity getDataFromFilePath(@RequestParam String path){
        try {
            if (path.contains("\\")){
                return new ResponseEntity<>("contained backslashes",HttpStatus.BAD_REQUEST);
            }
            List<ImageAttribute> data = imageDataService.getDataFromFilePath(path);
            return new ResponseEntity<>(data, HttpStatus.OK);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets all unique tags belonging to ROIs of an image
     * @param id image id
     * @param page current page of the tag list
     * @param size amount of tags show per page
     * @return list of unique image tags
     *
     * @author Kim Chau Duong
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/metadata/image/tags", method = RequestMethod.GET)
    public ResponseEntity getUniqueImageTags(@RequestParam int id,
                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "20") int size){
        try{
            List<String> data = imageDataService.getUniqueImageTags(id, page, size);
            UniqueTags uniqueImageTags = new UniqueTags(data);
            uniqueImageTags.add(linkTo(methodOn(ImageDataController.class).getUniqueImageTags(id, page, size)).withSelfRel());
            uniqueImageTags.add(linkTo(methodOn(ImageDataController.class).getUniqueImageTags(id, page+1, size)).withRel("next"));
            if (page == 0){
                page = 1;
            }
            uniqueImageTags.add(linkTo(methodOn(ImageDataController.class).getUniqueImageTags(id, page-1, size)).withRel("previous"));

            return new ResponseEntity<>(uniqueImageTags, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("Image ID does not exist",HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets all unique tags belonging to ROIs of images residing in the directory
     * @param path directory path
     * @param page current page of the tag list
     * @param size amount of tags show per page
     * @return list of unique folder image tags
     *
     * @author Kim Chau Duong
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/metadata/directory/tags", method = RequestMethod.GET)
    public ResponseEntity getUniqueFolderTags(@RequestParam String path,
                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "size", defaultValue = "20") int size){
        try{
            List<String> data = imageDataService.getUniqueFolderTags(path, page, size);
            UniqueTags uniqueFolderTags = new UniqueTags(data);
            uniqueFolderTags.add(linkTo(methodOn(ImageDataController.class).getUniqueFolderTags(path, page, size)).withSelfRel());
            uniqueFolderTags.add(linkTo(methodOn(ImageDataController.class).getUniqueFolderTags(path, page+1, size)).withRel("next"));
            if (page == 0){
                page = 1;
            }
            uniqueFolderTags.add(linkTo(methodOn(ImageDataController.class).getUniqueFolderTags(path, page-1, size)).withRel("previous"));
            return new ResponseEntity<>(uniqueFolderTags, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>("Path does not exist",HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * gets all available tags for user tag suggestions
     *
     * @return list of tags
     *
     * @author Jouke Profijt
     */
    @CrossOrigin(origins = "*")
    @RequestMapping("/tags/all/")
    public ResponseEntity getAvailableTags() {
        List<String> tags = imageDataService.getAvailableTags();

        return new ResponseEntity(tags, HttpStatus.OK);
    }

    /**
     * gets all tags for an image
     * @param image image id
     * @return tagged image with correct status
     *
     * @author Jouke Profijt
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/tags/image/")
    public ResponseEntity getImageTags(@RequestParam("image") int image) {

        ImageTags taggedImage = imageDataService.getTaggedImage(image);

        if (taggedImage.getTags().isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity(taggedImage, HttpStatus.OK);
    }

    /**
     * adds a new tag to a image
     * @param newTag tag to be added
     * @return sucess string with correct status
     *
     * @author Jouke Profijt
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/tags/image/", headers = "Content-Type=application/json")
    public ResponseEntity appendNewTagToImage(@RequestBody newTag newTag) {
        imageDataService.addNewTag(newTag.getId(), newTag.getTag(), newTag.getUser());
        return new ResponseEntity(new String[] {"Sucess, Inserted new tag"}, HttpStatus.ACCEPTED);

    }


    /**
     * Add a new region of interest with key value pairs
     * @param regionOfInterestState region of interest
     * @return success string
     *
     * @author Jouke Profijt
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/roi/state/", headers = "Content-Type=application/json")
    public ResponseEntity addNewStateToROI(@RequestBody regionOfInterestState regionOfInterestState){
        imageDataService.addNewRoi(regionOfInterestState);
        return new ResponseEntity(new String[] {"Added new state to Region of interest, true"}, HttpStatus.ACCEPTED);

    }

    /**
     * ability get region of interest for a image
     * @param image image id
     * @return region of interest states that belong to the image
     *
     * @author Jouke Profijt
     */
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/roi/state/")
    public ResponseEntity getRoiStates(@RequestParam("image") int image){
        List<regionOfInterestState> states = imageDataService.getRoiStatesOfImage(image);
        return new ResponseEntity(states, HttpStatus.ACCEPTED);

    }

    /**
     * gets all tags of a Region of interest
     * @param roiID region of interest id
     * @return array with roi tags and http status
     *
     * @author Jouke Profijt
     */
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/roi/tags/get/")
    public ResponseEntity getRoiTags(@RequestParam("roi") int roiID){
        taggedRoi taggedRoi = new taggedRoi();
        List<String> tags;
        tags = imageDataService.getRoiTags(roiID);

        taggedRoi.setId(roiID);
        taggedRoi.setTags(tags);

        return new ResponseEntity(taggedRoi, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/image/roi/")
    public ResponseEntity getPointsByRoiId(@RequestParam("roi-id") int roiId) {
        List<RoiPoint> states = imageDataService.getPointsByRoiId(roiId);
        return new ResponseEntity(states, HttpStatus.ACCEPTED);
    }

    /**
     * Adds new tags to Region of interest
     * @param roiTag roiTag object with roi id and tag
     * @return Correct response
     *
     * @author Jouke Profijt
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/roi/tags/add", headers = "Content-Type=application/json")
    public ResponseEntity addNewRoiTag(@RequestBody RoiTag roiTag) {
        List<String> existingTags = imageDataService.getRoiTags(roiTag.getId());
        if (existingTags.contains(roiTag.getTag())) {
            return new ResponseEntity(new String[]{"Roi already contains tag"}, HttpStatus.CONFLICT);
        } else {
            imageDataService.addNewRoiTag(roiTag);
            return new ResponseEntity(new String[]{"Added new tag to Region of interest, true"}, HttpStatus.ACCEPTED);
        }
    }

    /**
     * Removes region of interest tag
     * @param roiTag tag with roi id to be removed
     * @return OK response when done
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/roi/tags/delete/", headers = "Content-Type=application/json")
    public ResponseEntity removeRoiTag(@RequestBody RoiTag roiTag) {
        imageDataService.removeRoiTag(roiTag);
        String responseString = "Removed tag " + roiTag.getTag() + " from image " + roiTag.getId();

        return new ResponseEntity(responseString, HttpStatus.OK);
    }

    String[] empty;
    /**
     * Removes region of interest tag
     * @return OK response when done
     * @author Jouke Profijt
     */
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(value = "/roi/search/")
    public ResponseEntity<List<CompleteRoi>> searchRoi(@RequestParam(value = "phMin", defaultValue = "0") double phMin,
                                                       @RequestParam(value = "phMax",  defaultValue = "14") double phMax,
                                                       @RequestParam(value = "temperatureMin",  defaultValue = "-273.15") double temperatureMin,
                                                       @RequestParam(value = "temperatureMax",  defaultValue = "1000") double temperatureMax,
                                                       @RequestParam(value = "O2Min",  defaultValue = "0") int O2Min,@RequestParam(value = "O2Max", defaultValue = "100") int O2Max,
                                                       @RequestParam(value = "CO2Min",  defaultValue = "0") int CO2Min,@RequestParam(value = "CO2Max", defaultValue = "100") int CO2Max,
                                                       @RequestParam(value = "tags", defaultValue = "") List<String> tags, @RequestParam(value = "page", defaultValue= "0") int page,
                                                       @RequestParam(value = "size", defaultValue= "10") int size) {
        HashMap<String, Range> searchRanges = new HashMap<>();

        if (phMin > phMax) {
            return new ResponseEntity(new RangeError("Ph minimum larger than maximum", HttpStatus.BAD_REQUEST, phMin, phMax), HttpStatus.BAD_REQUEST);
        }
        if (temperatureMin > temperatureMax) {
            return new ResponseEntity(new RangeError("temperature minimum larger than maximum", HttpStatus.BAD_REQUEST, temperatureMin, temperatureMax), HttpStatus.BAD_REQUEST);
        }
        if (O2Min > O2Max) {
            return new ResponseEntity(new RangeError("O2 minimum larger than maximum", HttpStatus.BAD_REQUEST, O2Min, O2Max), HttpStatus.BAD_REQUEST);
        }
        if (CO2Min > CO2Max) {
            return new ResponseEntity(new RangeError("CO2 minimum larger than maximum", HttpStatus.BAD_REQUEST, CO2Min, CO2Max), HttpStatus.BAD_REQUEST);
        }
        if (phMin < 0 || phMax < 0 || phMin > 14 || phMax > 14) {
            return new ResponseEntity(new RangeError("Ph must be between 0 and 14", HttpStatus.BAD_REQUEST, phMin, phMax), HttpStatus.BAD_REQUEST);
        }
        if (temperatureMin < -273.15 || temperatureMax < -273.15) {
            return new ResponseEntity(new RangeError("Temperature cant be smaller than the absolute zero temperature", HttpStatus.BAD_REQUEST, temperatureMin, temperatureMax), HttpStatus.BAD_REQUEST);
        }
        if (O2Min < 0 || O2Max < 0 || O2Min > 100 || O2Max > 100) {
            return new ResponseEntity(new RangeError("O2 is stored as a percentage cant be smaller than 0 or larger than 100", HttpStatus.BAD_REQUEST, O2Min, O2Max), HttpStatus.BAD_REQUEST);
        }
        if (CO2Min < 0 || CO2Max < 0 || CO2Min > 100 || CO2Max > 100) {
            return new ResponseEntity(new RangeError("CO2 is stored as a percentage cant be smaller than 0 or larger than 100", HttpStatus.BAD_REQUEST, CO2Min, CO2Max), HttpStatus.BAD_REQUEST);
        }


        searchRanges.put("ph", new Range(phMin, phMax));
        searchRanges.put("temp", new Range(temperatureMin, temperatureMax));
        searchRanges.put("O2", new Range(O2Min, O2Max));
        searchRanges.put("CO2", new Range(CO2Min, CO2Max));


        List<CompleteRoi> results = imageDataService.searchRegionOfInterest(searchRanges, tags, page, size);

        System.out.println(results);
        if (results.isEmpty()) {

            return new ResponseEntity(new NormalErrorResponse("No results found", HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);
        }


        return new ResponseEntity(results, HttpStatus.OK);
    }
}




