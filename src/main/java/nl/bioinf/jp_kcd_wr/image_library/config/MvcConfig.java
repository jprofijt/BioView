package nl.bioinf.jp_kcd_wr.image_library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/succes").setViewName("succes");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");

        registry
                .addResourceHandler("/js/**")
                .addResourceLocations("resources/js/");

        registry
                .addResourceHandler("/css/**")
                .addResourceLocations("/resources/css/");

        registry
                .addResourceHandler("/img/**")
                .addResourceLocations("/resources/img/");
    }

}
