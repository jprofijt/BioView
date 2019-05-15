package nl.bioinf.jp_kcd_wr.image_library.storage;

import nl.bioinf.jp_kcd_wr.image_library.data_access.ImageDataSource;
import nl.bioinf.jp_kcd_wr.image_library.data_access.jdbc.ImageDataSourceJdbc;
import nl.bioinf.jp_kcd_wr.image_library.data_access.mock.ImageDataSourceMock;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testing class for Storage service
 * @author Jouke Profijt
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FileSystemStorageServiceTest {
    private FileSystemStorageService storageService;
    private ImageDataSource imageDataSource;
    private MockEnvironment environment;

    @SuppressWarnings("unchecked")

    @BeforeAll
    public void setUp() throws Exception{

        environment = new MockEnvironment();
        environment.setProperty("library.upload", "test_directory/upload");
        environment.setProperty("cache-location", "test_directory/thumbnails");
        imageDataSource = new ImageDataSourceMock();
        storageService = new FileSystemStorageService(imageDataSource, environment);
    }

    private MultipartFile getImageFromUrl(String url){
        try {
            URL img = new URL(url);
            BufferedImage image = ImageIO.read(img);
            File outfile = new File("test-img.png");
            ImageIO.write(image, "png", outfile);

            FileInputStream inputStream = new FileInputStream(outfile);
            return new MockMultipartFile("test-img", outfile.getName(), "png", IOUtils.toByteArray(inputStream));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Tests if system has storage capability
     *
     * @author Jouke Profijt
     */
    @Test
    void storejpg() {
        storageService.store(getImageFromUrl("https://image.shutterstock.com/z/stock-photo--d-illustration-of-t-cells-or-cancer-cells-433526728.jpg"));
        Path testimage = Paths.get(environment.getProperty("library.upload") + "/test-img.png");

        assertTrue(testimage.toFile().isFile());

        testimage.toFile().delete();
    }

    /**
     * simple test to find root location
     *
     * @author Jouke Profijt
     */
    @Test
    void getRootLocationSunny() {
        Path rootLocation = Paths.get("test_directory/upload");
        assertEquals(storageService.getRootLocation(), rootLocation);
    }

    /**
     * Function for testing different location parameters
     * @param rootLocation root location path
     * @param thumbnailLocation thumbnail location path
     *
     * @author Jouke Profijt
     */
    private void testDirectoryLocations(String rootLocation, String thumbnailLocation){
        NamedParameterJdbcTemplate jdbcTemplateMock = Mockito.mock(NamedParameterJdbcTemplate.class);
        environment.setProperty("library.upload", rootLocation);
        environment.setProperty("cache-location", thumbnailLocation);
        imageDataSource = new ImageDataSourceJdbc(jdbcTemplateMock, environment);
        storageService = new FileSystemStorageService(imageDataSource, environment);

    }

    /**
     * test for incorrect property parameters
     *
     * @author Jouke Profijt
     */
    @Test
    void rootLocationShouldThrowException(){
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> testDirectoryLocations("", "test_directory/thumbnails"));
        assertEquals(exception.getMessage(), "library.upload parameter is empty");

        Throwable exception_thumbnails = assertThrows(IllegalArgumentException.class, () -> testDirectoryLocations("test_directory/thumbnails", ""));
        assertEquals(exception_thumbnails.getMessage(), "cache-location parameter is empty");
    }
}