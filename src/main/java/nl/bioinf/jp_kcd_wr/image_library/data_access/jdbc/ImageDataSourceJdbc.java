package nl.bioinf.jp_kcd_wr.image_library.data_access.jdbc;

import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ImageDataSourceJdbc implements ImageDataSource {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;


    @Autowired
    public ImageDataSourceJdbc(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public void insertImage(int id, String origName, String hashName, String path) {
        String insertQuery = "INSERT INTO images (orig_name, hash_name, path) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertQuery, origName, hashName, path);
    }

    @Override
    public Image getOrigNamebyHashName(String hash) {
        return null;
    }
}
