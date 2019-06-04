/*
 * Copyright (c) 2018 Kim Chau Duong
 * All rights reserved
 */
package nl.bioinf.jp_kcd_wr.image_library.model;

import java.nio.file.Path;

/**
 * Copyright (c) 2019 Kim Chau Duong
 * All rights reserved
 */
public class Directory {
    private String path;
    private String name;
    private String dateModified;

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

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public Directory(String path, String name, String dateModified) {
        this.path = path;
        this.name = name;
        this.dateModified = dateModified;
    }

    public Directory() {
    }
}
