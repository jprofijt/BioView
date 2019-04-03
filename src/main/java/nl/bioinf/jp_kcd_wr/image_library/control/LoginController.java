package nl.bioinf.jp_kcd_wr.image_library.control;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

public class LoginController {
    @GetMapping(value={"/login", "/"})
    public ModelAndView loginController(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {

            /* The user is logged in :) */
            return new ModelAndView("forward:/temp");
        }
        else {
            return new ModelAndView("/login");
        }
    }

}
