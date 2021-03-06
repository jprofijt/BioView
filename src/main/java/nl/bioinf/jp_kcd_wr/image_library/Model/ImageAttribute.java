/**
 * Copyright (c) 2019 Kim Chau Duong
 * All rights reserved
 */
package nl.bioinf.jp_kcd_wr.image_library.Model;

import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageFileType;

/**
 * Model class for image attributes
 * Used to store image attributes in the database
 *
 * @auhor Kim Chau Duong
 */
public class ImageAttribute {
    private int id;
    private String imageName;
    private String path;
    private String filePath;
    private long imageSize;
    private String dateCreated; //or modified
    private ImageFileType fileType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getImageSize() {
        return imageSize;
    }

    public void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ImageFileType getFileType() {
        return fileType;
    }

    public void setFileType(ImageFileType fileType) {
        this.fileType = fileType;
    }

    public ImageAttribute() {
    }

    public ImageAttribute(int id, String imageName, String path, String filePath, long imageSize, String dateCreated, ImageFileType fileType) {
        this.id = id;
        this.imageName = imageName;
        this.path = path;
        this.filePath = filePath;
        this.imageSize = imageSize;
        this.dateCreated = dateCreated;
        this.fileType = fileType;
    }


}
