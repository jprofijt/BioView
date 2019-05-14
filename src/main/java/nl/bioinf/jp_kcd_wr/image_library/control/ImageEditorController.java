package nl.bioinf.jp_kcd_wr.image_library.control;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ImageEditorController {

    @GetMapping("/image-editor")
    public String redirection(){
        return "redirect/main-page";
    }

    @PostMapping("/image-editor")
    public String openImage(@RequestParam(name="imagePath") String image, Model model){
        model.addAttribute("imagepath", "files/"+image);
        return "image-editor";
    }


}
