package nl.bioinf.jp_kcd_wr.image_library.control;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping(value = "/roletest")
    public String getUser(Authentication authentication, Model model) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("User has authorities: " + userDetails.getAuthorities());
        System.out.println("User has name: " + userDetails.getUsername());
        model.addAttribute("role", userDetails.getAuthorities());

        return "role-test";
    }


}