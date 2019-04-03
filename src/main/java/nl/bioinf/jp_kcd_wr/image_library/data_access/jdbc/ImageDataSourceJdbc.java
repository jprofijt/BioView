package nl.bioinf.jp_kcd_wr.image_library.data_access.jdbc;

import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImageDataSourceJdbc implements ImageDataSource {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;


    @Autowired
    public ImageDataSourceJdbc(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public void insertImage(Image image) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("orig_name", image.getOrigName())
                .addValue("new_name", image.getNewFilename())
                .addValue("path", image.getPath());
        String insertQuery = "INSERT INTO images (orig_name, new_name, path) VALUES (:orig_name, :new_name, :path)";
        namedJdbcTemplate.update(insertQuery, parameters);
    }

    @Override
    public Image getOrigNamebyHashName(String hash) {
        return null;
    }

    @Override
    public ArrayList<Image> getImagesInDirectory(String directory) {
        String sql = "SELECT * FROM images where path = ?";
        return null;
    }

    @Override
    public List<Image> returnAllImages() {
        return null;
    }
}
