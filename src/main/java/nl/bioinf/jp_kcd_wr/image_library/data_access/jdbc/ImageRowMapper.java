package nl.bioinf.jp_kcd_wr.image_library.data_access.jdbc;

import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ImageRowMapper implements RowMapper<Image> {
    @Override
    public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
        Image image = new Image();

        image.setId(rs.getInt("id"));
        image.setNewFilename("NewFilename");
        image.setOrigName("origName");
        image.setPath("path");

        return image;
    }
}
