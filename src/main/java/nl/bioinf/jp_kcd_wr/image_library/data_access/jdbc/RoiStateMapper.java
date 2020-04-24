package nl.bioinf.jp_kcd_wr.image_library.data_access.jdbc;

import nl.bioinf.jp_kcd_wr.image_library.Model.regionOfInterestState;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoiStateMapper implements RowMapper<regionOfInterestState> {
    @Override
    public regionOfInterestState mapRow(ResultSet rs, int rowNum) throws SQLException {


        regionOfInterestState regionOfInterestState = new regionOfInterestState();

        regionOfInterestState.setId(rs.getInt("roi_id"));
        regionOfInterestState.setPh(rs.getDouble("ph"));
        regionOfInterestState.setTemp(rs.getDouble("T"));
        regionOfInterestState.setO2(rs.getInt("o2"));
        regionOfInterestState.setCo2(rs.getInt("co2"));

        return regionOfInterestState;
    }
}
