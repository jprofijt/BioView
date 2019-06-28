package nl.bioinf.jp_kcd_wr.image_library.data_access.jdbc;

import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageFileType;
import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import nl.bioinf.jp_kcd_wr.image_library.model.ImageAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
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
}
