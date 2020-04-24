package nl.bioinf.jp_kcd_wr.image_library.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Copyright (c) 2019 Kim Chau Duong
 * All rights reserved
 */
public interface StorageService {

    /**
     * Stores the file in the directory and the data in database
     * @param file uploaded file
     *
     * @author Kim Chau Duong, Jouke Profijt
     */
    void storeFile(MultipartFile file, File directory);

    /**
     * loads absolute file paths of image
     * @param currentFolder folder to search
     * @return stream of paths
     *
     * @author Jouke Profijt
     */
    Stream<Path> loadAbsoluteStoredImagePaths(String currentFolder);

    /**
     * For each image in existing image library will create a cached image if it doesn't exist and makes a database insert
     * @param Directory
     *
     * @author Jouke Profijt
     */
    void storeExistingImageLibrary(File Directory);

}
