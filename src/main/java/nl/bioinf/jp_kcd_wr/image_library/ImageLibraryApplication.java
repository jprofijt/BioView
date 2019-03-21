package nl.bioinf.jp_kcd_wr.image_library;

import nl.bioinf.jp_kcd_wr.image_library.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ImageLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageLibraryApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
//            storageService.deleteAll(); //Deletes all files in 'upload-dir' folder on Spring run. Remove once no longer needed
            storageService.init();
        };
    }

}
