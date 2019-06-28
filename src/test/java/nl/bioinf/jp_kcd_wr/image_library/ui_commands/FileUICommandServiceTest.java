package nl.bioinf.jp_kcd_wr.image_library.ui_commands;

import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import nl.bioinf.jp_kcd_wr.image_library.data_access.mock.ImageDataSourceMock;
import nl.bioinf.jp_kcd_wr.image_library.folder_manager.DirectoryExistsException;
import nl.bioinf.jp_kcd_wr.image_library.storage.FileSystemStorageService;
import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI Commands tests class
 * Mainly tests commands with fake folders
 *
 * @author Kim Chau Duong, Jouke Profijt
 */
class FileUICommandServiceTest {
    private FileUICommandService uiCommandService;
    private MockEnvironment environment;
    private ImageDataSource imageDataSource;
    private FileSystemStorageService storageService;

    @SuppressWarnings("unchecked")

    @BeforeEach
    void setUp() throws Exception{

        environment = new MockEnvironment();
        environment.setProperty("library.upload", "test_directory/upload");
        environment.setProperty("cache-location", "test_directory/thumbnails");
        environment.setProperty("library.sym", "test_directory/symupload");
        environment.setProperty("library.sym.thumbnails", "test_directory/symthumbnails");

        imageDataSource = new ImageDataSourceMock();
        storageService = new FileSystemStorageService(imageDataSource, environment);
        uiCommandService = new FileUICommandService(environment, storageService);
    }
    /**
     * Tests if directory creation functionality works
     * @throws DirectoryExistsException If the directory exists throws error
     * @throws IOException IF there are other problems like permissions
     * @author Jouke Profijt
     */

    @Test
    void createNewFolder() throws DirectoryExistsException {
        File testDir = new File(environment.getProperty("library.sym") + "/HeadDirectory/MadeAtestDir");
        testDir.delete();
        uiCommandService.createNewFolder("MadeAtestDir", "HeadDirectory");
        assertTrue(testDir.isDirectory());
    }

    /**
     * Checks If a folder exists an exception is thrown
     * @throws DirectoryExistsException If the directory exists throws error
     * @throws IOException IF there are other problems like permissions
     * @author Jouke Profijt
     */
    @Test
    void createNewFolderThatExists() throws DirectoryExistsException, IOException {
        File testDir = new File(environment.getProperty("library.sym") + "/HeadDirectory/existingDirectory");
        testDir.delete();

        uiCommandService.createNewFolder("existingDirectory", "HeadDirectory");

        assertThrows(DirectoryExistsException.class,
                () -> uiCommandService.createNewFolder(
                        "existingDirectory",
                        "HeadDirectory"));
    }

    /**
     * Checks if folder removing works
     * This test only works assuming the folder creation bit also succeeded
     * @throws DirectoryExistsException
     *
     * @author Kim Chau Duong
     */
    @Test
    void removeFolder() throws DirectoryExistsException {
        uiCommandService.createNewFolder("testfoldertest", "HeadDirectory");
        assertTrue(uiCommandService.removeFile("HeadDirectory/testfoldertest"));
    }

    /**
     * Tests subfolder creation and subfolder removal
     * @throws DirectoryExistsException
     *
     * @author Kim Chau Duong
     */
    @Test
    void removeSubfolder() throws DirectoryExistsException {
        uiCommandService.createNewFolder("unexisting", "HeadDirectory");
        uiCommandService.createNewFolder("testfoldertest", "HeadDirectory/unexisting/");
        assertTrue(uiCommandService.removeFile("HeadDirectory/unexisting/testfoldertest"));
        uiCommandService.removeFile("HeadDirectory/unexisting");
    }

    /**
     * Test folder moving from one directory to another
     * Test only works if directory creation and removal already work
     *
     * @author Kim Chau Duong
     */
    @Test
    void moveFile() throws DirectoryExistsException {
        uiCommandService.createNewFolder("fake-folder", "HeadDirectory");
        uiCommandService.createNewFolder("unexisting", "HeadDirectory/fake-folder");
        String source = "HeadDirectory/fake-folder/unexisting";
        String destination = "HeadDirectory";
        assertTrue(uiCommandService.moveFile(source, destination));
        uiCommandService.removeFile("HeadDirectory/fake-folder");
        uiCommandService.removeFile("HeadDirectory/unexisting");
    }

    /**
     * Checks if file has been copied properly
     * Only works if file creation and removal are already functional
     * @throws DirectoryExistsException
     *
     * @author Kim Chau Duong
     */
    @Test
    void copyFile() throws DirectoryExistsException {
        uiCommandService.createNewFolder("fake-folder", "HeadDirectory");
        uiCommandService.createNewFolder("unexisting", "HeadDirectory/fake-folder");
        String source = "HeadDirectory/fake-folder/unexisting";
        String destination = "HeadDirectory";
        assertTrue(uiCommandService.copyFile(source, destination));
        uiCommandService.removeFile("HeadDirectory/fake-folder/unexisting");
        uiCommandService.removeFile("HeadDirectory/fake-folder");
        uiCommandService.removeFile("HeadDirectory/unexisting");

    }

    /**
     * Checks if file can be renamed properly
     * @throws DirectoryExistsException
     *
     * @author Kim Chau Duong
     */
    @Test
    void renameFile() throws DirectoryExistsException {
        uiCommandService.createNewFolder("fake-folder", "HeadDirectory");
        uiCommandService.createNewFolder("unexisting", "HeadDirectory/fake-folder");
        String source = "HeadDirectory/fake-folder/unexisting";
        String renamed = "existing-folder";
        assertTrue(uiCommandService.renameFile(source, renamed));
        uiCommandService.removeFile("HeadDirectory/fake-folder/existing-folder");
        uiCommandService.removeFile("HeadDirectory/fake-folder");
    }

    /**
     * Checks if false input such as a false directory would trigger an exception or not
     * Exceptions get caught in try/catch in the main method
     *
     * @author Kim Chau Duong
     */
    @Test
    void moveUnexistingFile(){
        String bogusFile = "HeadDirectory/fake/path/here/fake.jpg";
        String destination = "HeadDirectory";
        assertFalse(uiCommandService.moveFile(bogusFile, destination));
    }
}