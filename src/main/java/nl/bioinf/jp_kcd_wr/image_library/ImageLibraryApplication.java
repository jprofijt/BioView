package nl.bioinf.jp_kcd_wr.image_library;

import nl.bioinf.jp_kcd_wr.image_library.storage.StorageProperties;
import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class ImageLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageLibraryApplication.class, args);
    }
    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver, SpringSecurityDialect sec) {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(sec); // Enable use of "sec"
        return templateEngine;
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
//            storageService.deleteAll(); //Deletes all files in 'upload-dir' folder on Spring run. Remove once no longer needed
            storageService.init();
        };
    }

}
