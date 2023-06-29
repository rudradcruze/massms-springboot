package ac.dia.massms.config;

import ac.dia.massms.model.Role;
import ac.dia.massms.model.User;
import ac.dia.massms.repository.RoleRepository;
import ac.dia.massms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository serviceUser;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository repository;

    @RequestMapping("/newUser")
    public String showNewUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        List<Role> listRoles = (List<Role>) repository.findAll();
        model.addAttribute("listRoles", listRoles);
        return "new_user";
    }

    @RequestMapping(value = "/saveUser", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        serviceUser.save(user);
        return "redirect:/";
    }
}
