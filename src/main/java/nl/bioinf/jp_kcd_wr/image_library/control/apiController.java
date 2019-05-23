package nl.bioinf.jp_kcd_wr.image_library.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
public class apiController {

    @GetMapping("/tags/{image}")
    public String test(@PathVariable("image") int image_id){
        System.out.println(image_id);
        return "";
    }



}
