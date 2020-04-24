package nl.bioinf.jp_kcd_wr.image_library.data_access.jdbc;

import nl.bioinf.jp_kcd_wr.image_library.Model.*;
import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that handles all image DAO processes
 *
 * @author Kim Chau Duong, Jouke Profijt
 * @version 1.0
 */
@Component
public class ImageDataSourceJdbc implements ImageDataSource {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private static final Logger logger = Logger.getLogger(ImageDataSourceJdbc.class.getName());

    /**
     * Constructor creates Jdbc template
     * @param namedJdbcTemplate jdbc template that uses custom names to assign values
     * @author Kim Chau Duong
     */
    @Autowired
    public ImageDataSourceJdbc(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        logger.log(Level.INFO, "Instantiated new Jdbc");

    }

    /**
     * Inserts a new image object into the database
     * @param image Image object
     * @author Jouke Profijt
     */
    @Override
    public void insertImage(Image image) {
        if (!checkImageIndex(image)) {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("orig_name", image.getOrigName())
                    .addValue("new_name", image.getNewFilename())
                    .addValue("path", image.getPath().replace("\\", "/"));
            String insertQuery = "INSERT INTO images (orig_name, new_name, path) VALUES (:orig_name, :new_name, :path)";
            namedJdbcTemplate.update(insertQuery, parameters);
        }
    }

    /**
     * Return true if an image has been indexed in the database
     * @param image Image object to check
     * @return boolean
     * @author Jouke Profijt
     */
    private boolean checkImageIndex(Image image) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("path", image.getPath().replace("\\", "/"));
        String query = "SELECT count(*) FROM images WHERE path = :path";

        int result = namedJdbcTemplate.queryForObject(query, parameterSource, Integer.class);

        return result >= 1;
    }

    /**
     * gets the id for the image that is located in path
     * @param path image path
     * @return image id
     * @author Jouke Profijt
     */
    @Override
    public int getImageIdFromPath(String path) {
        String query = "SELECT id from images where path = :path";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("path", path.replace("\\", "/"));
        Integer ImageId = namedJdbcTemplate.queryForObject(query, parameter, Integer.class);
        return ImageId;
    }

    /**
     * inserts image cache location
     * @param imageId id for the image to be cached
     * @param cacheLocation location for cached image
     * @author Jouke Profijt
     */
    @Override
    public void storeThumbnailCacheDataPath(int imageId, Path cacheLocation) {
        if (checkThumbnailStatus(imageId) && cacheLocation.toFile().isFile()) {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("image_id", imageId)
                    .addValue("cache_path", cacheLocation.toString());
            String insertQuery = "INSERT INTO cache (image_id, cache_path) VALUES (:image_id, :cache_path)";
            namedJdbcTemplate.update(insertQuery, parameterSource);
        }
    }

    /**
     * checks if image has been cached yet
     * @param ImageId image id
     * @return boolean if the image is cached
     * @author Jouke Profijt
     */
    @Override
    public boolean checkThumbnailStatus(int ImageId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("image_id", ImageId);
        String query = "SELECT count(*) FROM cache WHERE image_id = :image_id";

        int result = namedJdbcTemplate.queryForObject(query, parameterSource, Integer.class);

        return result < 1;
    }

    /**
     * gets the cache for an image by their path
     * @param PathToImage Path
     * @return Cache path
     * @author Jouke Profijt
     */
    @Override
    public Path getThumbnailPathFromImagePath(String PathToImage) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("image_path", PathToImage.replace("\\", "/"));
        String query = "SELECT cache.cache_path from cache INNER JOIN images i on cache.image_id = i.id WHERE i.path = :image_path";
        String result = namedJdbcTemplate.queryForObject(query, parameterSource, String.class);
        return Paths.get(result).getFileName();
    }

    /**
     * Inserts new image attribute data
     * @param imageAttribute object containing all image attributes
     *
     * @author Jouke Profijt, Kim Chau Duong
     */
    @Override
    public void insertImageMetaData(ImageAttribute imageAttribute) {
        if (!checkImageAttributeIndex(imageAttribute)){
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", imageAttribute.getId())
                    .addValue("name", imageAttribute.getImageName())
                    .addValue("path", imageAttribute.getPath().replace("\\", "/"))
                    .addValue("filepath", imageAttribute.getFilePath().replace("\\", "/"))
                    .addValue("date", imageAttribute.getDateCreated())
                    .addValue("size", imageAttribute.getImageSize())
                    .addValue("type", imageAttribute.getFileType().toString());
            String query = "insert into image_attributes ( id, name, path, filepath, date, size, type) values (:id, :name, :path, :filepath, :date, :size, :type)";

            namedJdbcTemplate.update(query, parameterSource);
        }
    }

    /**
     * Checks if image attribute is contained in the database
     * @param imageAttribute Image Attribute object to check for
     * @return True if db contains attributes
     *
     * @author Jouke Profijt
     */
    private boolean checkImageAttributeIndex(ImageAttribute imageAttribute) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("filepath", imageAttribute.getFilePath().replace("\\", "/"));
        String query = "SELECT count(*) FROM image_attributes WHERE filepath = :filepath";
        int result = namedJdbcTemplate.queryForObject(query, parameterSource, Integer.class);

        return result >= 1;
    }
    /**
     * Retrieves image attributes of all images in a given folder path
     * @param path folder location
     * @return ImageAttribute list
     *
     * @author Kim Chau Duong
     */
    @Override
    public List<ImageAttribute> showDataFromPath(String path) {
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("path", path.replace("\\", "/"));
        String query = "SELECT * FROM image_attributes where path = :path;";
        return namedJdbcTemplate.query(query, parameter, new ImageDataRowMapper());
    }

    @Override
    public ImageTags getTaggedImage(int id) {
        String query = "SELECT image_tag FROM image_tags WHERE image_id like :id";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("id", id);
        List tags = namedJdbcTemplate.query(query, parameter, new TagRowMapper());
        return new ImageTags(id, tags);

    }

    @Override
    public List<String> getAvailableTags() {
        String query = "SELECT tag FROM tags";
        return namedJdbcTemplate.query(query, new RowMapper<String>(){
            public String mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString(1);
            }});
    }

    @Override
    public List<RoiPoint> getPointsForRoi(int id){
//        String query = "SELECT x_pos, y_pos from roi_points, image_roi join roi_points on roi_points.roi_id=image_roi.id join image_roi on image_roi.image_id=image_meta.id where image_id= :id ;";
        String query = "SELECT x_pos, y_pos, roi_id from roi_points where roi_id= :id;";
        String superQuery = "SELECT points.id, points.x_pos, points.y_pos, imgroi.id, meta.id from roi_points points, image_roi imgroi, images_meta meta join image_roi on image_roi.image_id=meta.id join roi_points on roi_points.roi_id=image_roi.id where meta.id=1 group by points.id, points.x_pos, points.y_pos, imgroi.id, meta.id order by imgroi.id";
        SqlParameterSource parameter = new MapSqlParameterSource().addValue("id", id);
        List<RoiPoint> results = namedJdbcTemplate.query(query, parameter, new RoiMapper());
        System.out.println("results = " + results);
        return namedJdbcTemplate.query(query, parameter, new RoiMapper());
    }

    /**
     * Inserts new tag to image
     * @param Image image_id
     * @param tag   the tag to be added
     * @param user  user that uploaded tag
     *
     * @author Jouke Profijt
     */
    @Override
    public void insertNewTag(int Image, String tag, String user) {
        String exsistsQuery = "SELECT count(*) from tags where tag like :tag";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("tag", tag)
                .addValue("image", Image)
                .addValue("user", user);
        String insertNewTagQuery = "insert into tags(tag) value (:tag)";
        String addTagToImageQuery = "insert into image_tags(image_id, image_tag) values (:image, :tag)";

        int count = namedJdbcTemplate.queryForObject(exsistsQuery, parameterSource, Integer.class);

        if (count < 1) {
            namedJdbcTemplate.update(insertNewTagQuery, parameterSource);
        }

        namedJdbcTemplate.update(addTagToImageQuery, parameterSource);
    }

    /**
     * Adds a new state to region of interest
     * @param regionOfInterestState
     *
     * @author Jouke Profijt
     */
    @Override
    public void addNewRoiState(regionOfInterestState regionOfInterestState, int roiId) {

        String updateQuery = "INSERT INTO ROI_STATE(roi_id, ph, T, o2, co2) values (:roi_id, :ph, :T, :o2, :co2)";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("roi_id", roiId)
                .addValue("ph", regionOfInterestState.getPh())
                .addValue("T", regionOfInterestState.getTemp())
                .addValue("o2", regionOfInterestState.getO2())
                .addValue("co2", regionOfInterestState.getCo2());
        namedJdbcTemplate.update(updateQuery, parameterSource);

    }

    @Override
    public int addNewRoi(int imageId) {
        String sizeQuery = "SELECT count(*) from image_roi";
        SqlParameterSource parameterSource = new MapSqlParameterSource();

        int newID = namedJdbcTemplate.queryForObject(sizeQuery, parameterSource, Integer.class) + 1;
        System.out.println(imageId);

        String newRoiQuery = "INSERT INTO image_roi(roi_id, image_id) values (:roi_id, :image_id)";

        parameterSource = new MapSqlParameterSource()
                .addValue("roi_id", newID)
                .addValue("image_id", imageId);

        namedJdbcTemplate.update(newRoiQuery, parameterSource);
        return newID;
    }

    @Override
    public List<regionOfInterestState> getRoiStatesOfImage(int image_id) {

        String imageQuery = "select image_roi.roi_id, ROI_STATE.ph, ROI_STATE.T, ROI_STATE.o2, ROI_STATE.co2 from ROI_STATE inner join image_roi on ROI_STATE.roi_id=image_roi.roi_id where image_roi.image_id=:image_id";


        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("image_id", image_id);
        return namedJdbcTemplate.query(imageQuery, parameterSource, new RoiStateMapper());
    }

    @Override
    public List<String> getRoiTags(int roiID) {
        String roiQuery = "SELECT tag FROM ROI_TAGS where roi_id=:roi";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("roi", roiID);


        return namedJdbcTemplate.query(roiQuery, parameterSource, (rs, rowNum) -> rs.getString(1));
    }

    @Override
    public void addNewRoiTag(RoiTag roiTag) {
        String updateQuery = "INSERT INTO ROI_TAGS (roi_id, tag) values (:id, :tag)";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", roiTag.getId())
                .addValue("tag", roiTag.getTag());

        namedJdbcTemplate.update(updateQuery, parameterSource);

    }

    /**
     * Retrieved image attributes of file with given path
     * @param path file path of the image
     * @return List of ImageAttribute
     *
     * @author Kim Chau Duong
     */
    @Override
    public List<ImageAttribute> showDataFromFilePath(String path) {
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("filepath", path.replace("\\", "/"));
        String query = "SELECT name, path, date, type, size FROM image_attributes where filepath = :filepath;";
        return namedJdbcTemplate.query(query, parameter, new ImageDataRowMapper());
    }

    @Override
    public void removeRoiTag(int id, String tag) {
        String deleteStatement = "DELETE FROM ROI_TAGS WHERE roi_id=:roi_id AND tag=:tag;";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("roi_id", id)
                .addValue("tag", tag);

        namedJdbcTemplate.update(deleteStatement, parameterSource);
    }

    /**
     * Searches region of interests by specific inputs
     * @param searchRanges Hashmap of ph,temp,co2,and o2 ranges
     * @param tags the tags the roi has to contain
     * @return
     */
    @Override
    public List<CompleteRoi> searchRegionOfInterest(HashMap<String, Range> searchRanges, List<String> tags, int page, int size) {
        String searchTagStatement = "select distinct roi_id from roi_tags";
        if (!tags.isEmpty()) {
            searchTagStatement = "select distinct roi_id from roi_tags where tag in (:tags);";
        }

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("tags", tags);

        List<Integer> relevantRois = namedJdbcTemplate.queryForList(searchTagStatement, parameterSource, Integer.class);

        List<CompleteRoi> TaggedRoiStates = new ArrayList<>();

        if (relevantRois.isEmpty()) {
            return TaggedRoiStates;
        }


        String searchStateStatement = "select * from roi_state where ph >= :phMin and ph <= :phMax and T >= :tempMin and T <= :tempMax and o2 >= :o2Min and o2 <= :o2Max and co2 >= :co2Min and co2 <= :co2Max and roi_id in (:ids) ORDER BY roi_id limit :page, :size";

        SqlParameterSource SearchParameterSource = new MapSqlParameterSource()
                .addValue("phMin", searchRanges.get("ph").getMinimum())
                .addValue("phMax", searchRanges.get("ph").getMaximum())
                .addValue("tempMin", searchRanges.get("temp").getMinimum())
                .addValue("tempMax", searchRanges.get("temp").getMaximum())
                .addValue("o2Min", (int) searchRanges.get("O2").getMinimum())
                .addValue("o2Max", (int) searchRanges.get("O2").getMaximum())
                .addValue("co2Min", (int) searchRanges.get("CO2").getMinimum())
                .addValue("co2Max", (int) searchRanges.get("CO2").getMaximum())
                .addValue("ids", relevantRois)
                .addValue("page", page*size)
                .addValue("size", size);

        List<regionOfInterestState> RoiStates = namedJdbcTemplate.query(searchStateStatement, SearchParameterSource, new RoiStateMapper());



        for (regionOfInterestState state: RoiStates) {
            System.out.println(state.getId());
            int stateId = state.getId();

            TaggedRoiStates.add(new CompleteRoi(getRoiTags(stateId), state, getImageWithRoi(stateId)));
        }


        return TaggedRoiStates;
    }

    private int getImageWithRoi(int stateId) {
        String  imageQuery = "select image_id from image_roi where roi_id = :state_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("state_id", stateId);


        return namedJdbcTemplate.queryForObject(imageQuery, parameterSource, Integer.class);
    }

    /**
     * Retrieves all unique tags from all ROIs of an image
     * @param id image id
     * @return List of tags
     *
     * @author Kim Chau Duong
     */
    @Override
    public List<String> getUniqueImageTags(int id, int page, int size){
        String tagQuery = "SELECT DISTINCT tag FROM roi_tags " +
                "INNER JOIN image_roi ON roi_tags.roi_id = image_roi.roi_id " +
                "WHERE image_roi.image_id = :image_id " +
                "LIMIT :page_offset, :page_limit";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("image_id", id)
                .addValue("page_offset", page*size)
                .addValue("page_limit", size);
        return namedJdbcTemplate.query(tagQuery, parameterSource, (rs, rowNum) -> rs.getString(1));
    }

    /**
     * Retrieves all unique tags from all ROIs of all images residing in the given directory
     * @param path path to directory
     * @return List of tags
     *
     * @author Kim Chau Duong
     */
    @Override
    public List<String> getUniqueFolderTags(String path, int page, int size){
        String tagQuery = "SELECT DISTINCT tag FROM roi_tags " +
                "INNER JOIN image_roi ON roi_tags.roi_id = image_roi.roi_id " +
                "INNER join image_attributes ON image_roi.image_id = image_attributes.id " +
                "WHERE image_attributes.path = :path " +
                "LIMIT :page_offset, :page_limit";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("path", path)
                .addValue("page_offset", page*size)
                .addValue("page_limit", size);
        return namedJdbcTemplate.query(tagQuery, parameterSource, (rs, rowNum) -> rs.getString(1));
    }
}
