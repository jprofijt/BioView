/*
 * Copyright (c) 2018 Kim Chau Duong
 * All rights reserved
 */
package nl.bioinf.jp_kcd_wr.image_library.model;

import java.nio.file.Path;

/**
 * This class is
 * It contains
 *
 * @author Kim Chau Duong
 * @version 1.0
 */
public class Directory {
    private Path path;
    private String name;

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Directory(Path path, String name) {
        this.path = path;
        this.name = name;
    }

    public Directory() {
    }
}
