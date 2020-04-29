package nl.bioinf.jp_kcd_wr.image_library.data_access.jdbc;

import nl.bioinf.jp_kcd_wr.image_library.Model.Image;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ImageRowMapper is a class that implements a RowMapper for the Image object so we can get the info from the database.
 * @author Jouke Profijt
 */
public class ImageRowMapper implements RowMapper<Image> {
    @Override
    public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
        Image image = new Image();

        image.setId(rs.getInt("id"));
        image.setNewFilename(rs.getString("new_name"));
        image.setOrigName(rs.getString("orig_name"));
        image.setPath(rs.getString("path"));

        return image;
    }
}
