package nl.bioinf.jp_kcd_wr.image_library.breadcrumbs;

/**
 * Breadcrumb model to build a breadcrumb object
 * Gets used with thymeleaf in html pages
 *
 * @author Kim Chau Duong
 */
public class Breadcrumb {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Breadcrumb(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
