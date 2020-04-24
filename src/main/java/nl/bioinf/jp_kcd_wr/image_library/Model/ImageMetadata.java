/**
 * Copyright (c) 2019 Kim Chau Duong
 * All rights reserved
 */
package nl.bioinf.jp_kcd_wr.image_library.Model;

import java.util.HashMap;

public class ImageMetadata {
    private String tags; //String or Array or Json
    private String description;
    private String annotation;
    private HashMap keyValuePair; //or Json
    private String RegionOfInterests; //list of ROIs?

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public HashMap getKeyValuePair() {
        return keyValuePair;
    }

    public void setKeyValuePair(HashMap keyValuePair) {
        this.keyValuePair = keyValuePair;
    }

    public String getRegionOfInterests() {
        return RegionOfInterests;
    }

    public void setRegionOfInterests(String regionOfInterests) {
        RegionOfInterests = regionOfInterests;
    }

    public ImageMetadata(String tags, String description, String annotation, HashMap keyValuePair, String regionOfInterests) {

        this.tags = tags;
        this.description = description;
        this.annotation = annotation;
        this.keyValuePair = keyValuePair;
        RegionOfInterests = regionOfInterests;
    }
}
