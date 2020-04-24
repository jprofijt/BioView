package nl.bioinf.jp_kcd_wr.image_library.data_access.jdbc;

import nl.bioinf.jp_kcd_wr.image_library.Model.ImageAttribute;
import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageFileType;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;


public class ImageDataRowMapper implements RowMapper<ImageAttribute> {

    @Override
    public ImageAttribute mapRow(ResultSet result, int rowNumber) throws SQLException {
        ImageAttribute imageAttribute = new ImageAttribute();
        imageAttribute.setImageName(result.getString("name"));
        imageAttribute.setPath(result.getString("path"));
        imageAttribute.setDateCreated(result.getString("date"));
        imageAttribute.setFileType(ImageFileType.valueOf(result.getString("type")));
        imageAttribute.setImageSize(result.getLong("size"));

        return imageAttribute;
    }
}
