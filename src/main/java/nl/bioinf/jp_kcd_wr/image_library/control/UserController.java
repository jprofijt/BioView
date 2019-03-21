package nl.bioinf.jp_kcd_wr.image_library.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    Logger logger = LoggerFactory.getLogger(LoggingController.class);

    @GetMapping(value = "/roletest")
    public String getUser(Authentication authentication, Model model) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("role", userDetails.getAuthorities());
        logger.trace(userDetails.getUsername() + "has authority: "+userDetails.getAuthorities());

        return "role-test";
    }


}