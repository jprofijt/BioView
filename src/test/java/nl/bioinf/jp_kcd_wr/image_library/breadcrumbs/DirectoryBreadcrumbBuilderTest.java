package nl.bioinf.jp_kcd_wr.image_library.breadcrumbs;

import nl.bioinf.jp_kcd_wr.image_library.model.Breadcrumb;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnitPlatform.class)
class DirectoryBreadcrumbBuilderTest {

    @Autowired
    private BreadcrumbBuilder breadcrumbBuilder;

    @Test
    void getBreadcrumbs() {
        List<Breadcrumb> breadcrumbs = breadcrumbBuilder.getBreadcrumbs("");
    }
}