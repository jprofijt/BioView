package nl.bioinf.jp_kcd_wr.image_library.data_access.jdbc;


import nl.bioinf.jp_kcd_wr.image_library.model.Directory;
import org.springframework.jdbc.core.RowMapper;

import java.nio.file.FileSystems;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DirectoryRowMapper implements RowMapper so directories in database can be mapped to directory object
 * @author Jouke Profijt
 */
public class DirectoryRowMapper implements RowMapper {

    @Override
    public Directory mapRow(ResultSet rs, int rowNum) throws SQLException {
        Directory directory = new Directory();

        directory.setPath(FileSystems.getDefault().getPath(rs.getString("path")));
        directory.setName(rs.getString("name"));
        return directory;
    }

}
