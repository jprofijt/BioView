package nl.bioinf.jp_kcd_wr.image_library.storage;

import nl.bioinf.jp_kcd_wr.image_library.model.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Copyright (c) 2019 Kim Chau Duong
 * All rights reserved
 */
public interface StorageService {

    void init();

    void store(MultipartFile file);

    String getNewName(String origFilename);

    Image createImageData(String origFilename, String hash, Path filePath);

//    Stream<Path> loadAll();

    Stream<Path> loadAll(String currentFolder);

    Stream<Path> loadAbsolute(String currentFolder);

    Path load(String filename);

    Resource loadAsResource(String filename, String directory);

    void deleteAll();

    void processExistingImageLibrary(File Directory);

    Path getRootLocation();

}
