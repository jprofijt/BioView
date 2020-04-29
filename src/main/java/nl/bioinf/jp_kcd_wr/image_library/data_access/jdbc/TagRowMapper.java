package nl.bioinf.jp_kcd_wr.image_library.data_access.jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagRowMapper implements RowMapper {
    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {


        return rs.getString("image_tag");
    }
}
