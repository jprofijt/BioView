package nl.bioinf.jp_kcd_wr.image_library.folder_manager;

import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import nl.bioinf.jp_kcd_wr.image_library.data_access.mock.ImageDataSourceMock;
import nl.bioinf.jp_kcd_wr.image_library.model.Directory;
import nl.bioinf.jp_kcd_wr.image_library.storage.FileSystemStorageService;
import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

import javax.validation.constraints.AssertTrue;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Folder handler Tests
 * @author Jouke Profijt
 */
class FolderHandlerTest {
    private MockEnvironment environment;
    private ImageDataSource imageDataSource;
    private StorageService storageService;
    private FolderStructureProvider provider;


    @BeforeEach
    public void setUp() throws Exception{

        environment = new MockEnvironment();
        environment.setProperty("library.upload", "test_directory/upload");
        environment.setProperty("cache-location", "test_directory/thumbnails");
        environment.setProperty("library.sym", "test_directory/symupload");
        environment.setProperty("library.sym.thumbnails", "test_directory/symthumbnails");
        imageDataSource = new ImageDataSourceMock();
        storageService = new FileSystemStorageService(imageDataSource, environment);

        new File(environment.getProperty("library.sym") + "/HeadDirectory/test/1").mkdirs();
        new File(environment.getProperty("library.sym") + "/HeadDirectory/test/2").mkdirs();
        provider = new FolderHandler(environment);
    }


    /**
     * Tests Folder finding functionality
     * @author Jouke Profijt
     */
    @Test
    void getNextFolders() {

        List<Directory> nextFolders = provider.getNextFolders("HeadDirectory/test");
        List<String> directoryNames = new ArrayList<>();
        List<String> testNames = new ArrayList<>();
        testNames.add("1");
        testNames.add("2");
        for (Directory directory : nextFolders) {
            directoryNames.add(directory.getName());
        }

        assertEquals(testNames, directoryNames);
    }
}