package ac.dia.massms.controller;

import ac.dia.massms.config.UserDetailsServiceImpl;
import ac.dia.massms.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @GetMapping("/")
    public String home(Model model,
                       HttpSession session,
                       Principal principal) {
        if (principal != null) {
            User user = userDetailsService.getByUserName(principal.getName());
            session.setAttribute("user", user);
        } else {
            session.removeAttribute("user");
        }
        model.addAttribute("title", "MASSMS - Home");
        return "index";
    }
}
