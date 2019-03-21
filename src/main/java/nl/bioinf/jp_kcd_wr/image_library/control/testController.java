package nl.bioinf.jp_kcd_wr.image_library.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class testController {

    @GetMapping("/temp")
    public String redirectToTemp(){
        return "bootstrap-base";
    }

}
