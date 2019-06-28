package nl.bioinf.jp_kcd_wr.image_library.breadcrumbs;

/**
 * Class to create breadcrumbs manually when given a path
 * It contains a method that gets all the breadcrumbs
 *
 * @author Kim Chau Duong
 * @version 1.0
 */

import java.util.List;

public interface BreadcrumbBuilder {

    /**
     * Gathers all created breadcrumbs into a list
     * @param directory current directory
     * @return list of breadcrumbs
     * @throws IllegalArgumentException when a faulty path is given
     */
    List<Breadcrumb> getBreadcrumbs(String directory);
}
