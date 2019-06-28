/*
 * Copyright (c) 2018 Kim Chau Duong
 * All rights reserved
 */
package nl.bioinf.jp_kcd_wr.image_library.model;

/**
 * Directory object model
 * Object gets used to retrieve data on the html page with thymeleaf
 *
 * @author Kim Chau Duong
 */
public class Directory {
    private String path;
    private String name;
    private String dateModified;
    private long size;

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getDateModified() {
        return dateModified;
    }

    public long getSize() {
        return size;
    }

    public Directory(String path, String name, String dateModified, long size) {
        this.path = path;
        this.name = name;
        this.dateModified = dateModified;
        this.size = size;
    }

    public Directory() {
    }

}
