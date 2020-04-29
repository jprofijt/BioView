/**
 * Copyright (c) 2019 Kim Chau Duong
 * All rights reserved
 */
package nl.bioinf.jp_kcd_wr.image_library.Model;

import java.util.List;

public class RegionOfInterest {
//    private int x1;
//    private int y1;
//    private int x2;
//    private int y2;
    private String tags;

    public List<RoiPoint> getPointsList() {
        return pointsList;
    }

    public void setPointsList(List<RoiPoint> pointsList) {
        this.pointsList = pointsList;
    }

    private List<RoiPoint> pointsList;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }



    public RegionOfInterest() {
//        this.tags = tags;
    }
}

