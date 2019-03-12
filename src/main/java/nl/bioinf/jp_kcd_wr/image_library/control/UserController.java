package nl.bioinf.jp_kcd_wr.image_library.control;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String getUserName(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("User has authorities: " + userDetails.getAuthorities());
        System.out.println("User has email: " + userDetails.getUsername());

        return "username";
    }
}