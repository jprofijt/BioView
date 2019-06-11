package nl.bioinf.jp_kcd_wr.image_library.config;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

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
    private File rootSymbolicLink;
    private File thumbnailSymbolicLink;

    @Autowired
    public WebConfig(Environment environment) {
        this.rootLocation = new File (environment.getProperty("library.upload")).toPath();
        this.thumbnailLocation = new File(environment.getProperty("cache-location")).toPath();
        this.rootSymbolicLink = new File("upload/upload/");
        this.thumbnailSymbolicLink = new File("upload/thumbnails");
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


        registry.addResourceHandler("/cache/*", "/cache/**").addResourceLocations("file:upload/thumbnails/");
        registry.addResourceHandler("/files/**", "/files/*").addResourceLocations("file:upload/upload/");

    }


    private void createSymbolicLinks() throws IOException {
        deleteDirectoryStream(rootSymbolicLink.toPath());
        deleteDirectoryStream(thumbnailSymbolicLink.toPath());
        Files.createSymbolicLink(rootSymbolicLink.toPath(), rootLocation);
        Files.createSymbolicLink(thumbnailSymbolicLink.toPath(), thumbnailLocation);
    }

    private void deleteDirectoryStream(Path path) throws IOException {
        Files.walk(Paths.get(path.toString().replace("\\", "/")))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
