package nl.bioinf.jp_kcd_wr.image_library.control;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class UserController {
    Logger logger = Logger.getLogger("nl.bioinf");

    @GetMapping(value = "/roletest")
    public String getUser(Authentication authentication, Model model) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("role", userDetails.getAuthorities());
        logger.log(Level.INFO,userDetails.getUsername() + "has authority: "+userDetails.getAuthorities());

        return "role-test";
    }


}