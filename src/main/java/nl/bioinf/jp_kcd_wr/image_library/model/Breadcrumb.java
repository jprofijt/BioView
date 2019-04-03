package nl.bioinf.jp_kcd_wr.image_library.model;

public class Breadcrumb {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Breadcrumb(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
