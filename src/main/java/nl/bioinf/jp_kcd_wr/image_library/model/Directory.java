/*
 * Copyright (c) 2018 Kim Chau Duong
 * All rights reserved
 */
package nl.bioinf.jp_kcd_wr.image_library.model;

/**
 * Copyright (c) 2019 Kim Chau Duong
 * All rights reserved
 */
public class Directory {
    private String path;
    private String name;
    private String dateModified;
    private long size;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
