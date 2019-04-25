package nl.bioinf.jp_kcd_wr.image_library.data_access.jdbc;

import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that handles all DAO processes
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
     * @param namedJdbcTemplate
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
        if (!isIndexed(image)) {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("orig_name", image.getOrigName())
                    .addValue("new_name", image.getNewFilename())
                    .addValue("path", image.getPath());
            String insertQuery = "INSERT INTO images (orig_name, new_name, path) VALUES (:orig_name, :new_name, :path)";
            logger.log(Level.INFO, "Inserting new image into image database from path: {0}", image.getPath());
            namedJdbcTemplate.update(insertQuery, parameters);
        }
    }

    /**
     * Return true if an image has been indexed in the database
     * @param image Image object to check
     * @return boolean
     * @author Jouke Profijt
     */
    private boolean isIndexed(Image image) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("path", image.getPath());
        String query = "SELECT count(*) FROM images WHERE path = :path";

        int result = namedJdbcTemplate.queryForObject(query, parameterSource, Integer.class);

        return result >= 1;

    }

    @Override
    public Image getOrigNamebyHashName(String hash) {
        return null;
    }

    /**
     * gets all images in the given directory
     * @param directory path to directory
     * @return
     * @author Jouke Profijt
     */
    @Override
    public List<Image> getImagesInDirectory(String directory) {
        String sql = "SELECT * FROM images WHERE path = :path";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("path", directory);
        logger.log(Level.INFO, "Querying all images in {0}", directory);
        return namedJdbcTemplate.query(sql, parameter, new ImageRowMapper());
    }

    /**
     * returns all images stored in database
     * @return List of image objects
     * @author Jouke Profijt
     */
    @Override
    public List<Image> returnAllImages() {
        String query = "SELECT * FROM images";

        return namedJdbcTemplate.query(query, new ImageRowMapper());
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
                .addValue("path", path);
        return namedJdbcTemplate.queryForObject(query, parameter, Integer.class);
    }

    /**
     * inserts image cache location
     * @param imageId id for the image to be cached
     * @param cacheLocation location for cached image
     * @author Jouke Profijt
     */
    @Override
    public void insertCache(int imageId, Path cacheLocation) {
        if (isNotCached(imageId) && cacheLocation.toFile().isFile()) {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("image_id", imageId)
                    .addValue("cache_path", cacheLocation.toString());
            String insertQuery = "INSERT INTO cache (image_id, cache_path) VALUES (:image_id, :cache_path)";
            logger.log(Level.INFO, "Inserting indexing cache for image with id: {0}, in {1}", new Object[]{imageId, cacheLocation});
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
    public boolean isNotCached(int ImageId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("image_id", ImageId);
        String query = "SELECT count(*) FROM cache WHERE image_id = :image_id";

        int result = namedJdbcTemplate.queryForObject(query, parameterSource, Integer.class);




        return result < 1;
    }

    /**
     * gets cache path for image id
     * @param ImageId image id
     * @return cache path
     * @author Jouke Profijt
     */
    @Override
    public Path getCache(int ImageId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("image_id", ImageId);
        String query = "SELECT path FROM cache WHERE image_id = :image_id";

        return namedJdbcTemplate.queryForObject(query, parameterSource, Path.class);
    }

    @Override
    public String getCacheFromImagePath(String PathToImage) {
        System.out.println(PathToImage);
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("image_path", PathToImage);
        String query = "SELECT cache.cache_path from cache INNER JOIN images i on cache.image_id = i.id WHERE i.path = :image_path";

        return namedJdbcTemplate.queryForObject(query, parameterSource, String.class);
    }
}
