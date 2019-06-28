package nl.bioinf.jp_kcd_wr.image_library.breadcrumbs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class DirectoryBreadcrumbBuilderTest {


    @Test
    void getLastBreadcrumbURL() {
        String testPath = "HeadDirectory/test";
        BreadcrumbBuilder breadcrumbBuilder = new DirectoryBreadcrumbBuilder();
        List<Breadcrumb> breadcrumbs = breadcrumbBuilder.getBreadcrumbs(testPath);
        String crumbURL =  breadcrumbs.get(breadcrumbs.size()-1).getUrl();
        assertEquals(crumbURL, "/imageview?location="+testPath);
    }

    @Test
    void noSubDirectoryCrumbs(){
        String testPath = "root";
        BreadcrumbBuilder breadcrumbBuilder = new DirectoryBreadcrumbBuilder();
        List<Breadcrumb> breadcrumbs = breadcrumbBuilder.getBreadcrumbs(testPath);
        String crumbURL =  breadcrumbs.get(breadcrumbs.size()-1).getUrl();
        assertEquals(crumbURL, "/imageview?location="+testPath);
    }

    @DisplayName("Middle breadcrumb names")
    @ParameterizedTest(name = "{0} should have {1} as middle crumb")
    @CsvSource({"root/test/middle/crumb/crumb2, middle", "1/2/3, 2", "a/b/c/d/e/f/g, d"})
    void getMiddleCrumbName(String path, String middle){
        BreadcrumbBuilder breadcrumbBuilder = new DirectoryBreadcrumbBuilder();
        List<Breadcrumb> breadcrumbs = breadcrumbBuilder.getBreadcrumbs(path);
        String middleCrumbName = breadcrumbs.get(breadcrumbs.size()/2).getName();
        assertEquals(middleCrumbName, middle);
    }

    @Test
    void sameFolderNames(){
        String path = "test/test/test/test/test/test/test";
        BreadcrumbBuilder breadcrumbBuilder = new DirectoryBreadcrumbBuilder();
        List<Breadcrumb> breadcrumbs = breadcrumbBuilder.getBreadcrumbs(path);
        String crumbURL =  breadcrumbs.get(breadcrumbs.size()-1).getUrl();
        assertEquals(crumbURL, "/imageview?location="+path);
    }

    @Test
    void nullPath(){
        BreadcrumbBuilder breadcrumbBuilder = new DirectoryBreadcrumbBuilder();
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> breadcrumbBuilder.getBreadcrumbs(null));
        assertEquals(exception.getMessage(), "No directory specified");

    }


}