package nl.bioinf.jp_kcd_wr.image_library.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *This webconfig sets all the paths for the resources so the webapp can get the necessary items from it.
 *
 * @author Wietse Reitsma, Jouke Profijt
 */

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
    private Path rootLocation;
    private Path thumbnailLocation;
    private Path rootSymbolicLink;
    private Path thumbnailSymbolicLink;

    @Autowired
    public WebConfig(Environment environment) {
        this.rootLocation = Paths.get(environment.getProperty("library.upload"));
        this.thumbnailLocation = Paths.get(environment.getProperty("cache-location"));
        this.rootSymbolicLink = Paths.get("src/main/resources/static/upload/upload");
        this.thumbnailSymbolicLink = Paths.get("src/main/resources/static/upload/thumbnails");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        try {
            createSymbolicLinks();
        } catch (IOException e) {
            e.printStackTrace();
        }

        registry.addResourceHandler(
                "/webjars/**",
                "/images/**",
                "/css/**",
                "/js/**"
                )
                .addResourceLocations(
                        "classpath:/webjars/",
                        "classpath:/static/images/",
                        "classpath:/static/css/",
                        "classpath:/static/js/");


        registry.addResourceHandler("/cache/**").addResourceLocations("classpath:/static/upload/thumbnails/");
        registry.addResourceHandler("/files/**").addResourceLocations("classpath:/static/upload/upload/");

    }


    private void createSymbolicLinks() throws IOException {
        rootSymbolicLink.toFile().delete();
        thumbnailSymbolicLink.toFile().delete();
        Files.createSymbolicLink(rootSymbolicLink, rootLocation);
        Files.createSymbolicLink(thumbnailSymbolicLink, thumbnailLocation);
    }
}
