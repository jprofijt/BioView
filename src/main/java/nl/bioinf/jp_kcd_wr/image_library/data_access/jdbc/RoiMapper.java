package nl.bioinf.jp_kcd_wr.image_library.data_access.jdbc;

import nl.bioinf.jp_kcd_wr.image_library.Model.RoiPoint;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoiMapper implements RowMapper {

    @Override
    public RoiPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
//        int roiId = rs.getInt("roi_id");
        int xPos = rs.getInt("x_pos");
        int yPos = rs.getInt("y_pos");
        RoiPoint roiPoint = new RoiPoint();
        roiPoint.setX(xPos);
        roiPoint.setY(yPos);
        return roiPoint;
    }
}
