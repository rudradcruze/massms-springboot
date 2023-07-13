package ac.dia.massms.controller;

import ac.dia.massms.config.UserDetailsServiceImpl;
import ac.dia.massms.model.Mass;
import ac.dia.massms.model.MassMember;
import ac.dia.massms.model.User;
import ac.dia.massms.service.MassMemberService;
import ac.dia.massms.service.MassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MassService massService;

    @Autowired
    private MassMemberService massMemberService;

    @GetMapping("/")
    public String home(Model model,
                       HttpSession session,
                       Principal principal) {
        if (principal != null) {
            User user = userDetailsService.getByUserName(principal.getName());
            session.setAttribute("user", user);
            MassMember massMember = new MassMember();
            model.addAttribute("massMember", massMember);
        } else {
            session.removeAttribute("user");
        }

        model.addAttribute("title", "MASSMS - Home");
        List<Mass> massList = massService.listAll();
        model.addAttribute("massList", massList);

        return "index";
    }
}
