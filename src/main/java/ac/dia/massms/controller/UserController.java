package ac.dia.massms.controller;

import ac.dia.massms.model.Role;
import ac.dia.massms.model.User;
import ac.dia.massms.repository.RoleRepository;
import ac.dia.massms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    private void userSession(Model model, Principal principal, HttpSession session) {
        if (principal != null) {
            User user = userRepository.getUserByUsername(principal.getName());
            session.setAttribute("user", user);
            model.addAttribute("newUser", new User());
        } else {
            session.removeAttribute("user");
        }
    }

    @GetMapping("/user")
    public String showAllUsers(Model model, Principal principal, HttpSession session) {
        userSession(model, principal, session);
        User user = new User();
        model.addAttribute("user", user);
        List<Role> listRoles = (List<Role>) roleRepository.findAll();
        model.addAttribute("listRoles", listRoles);
        List<User> userList = (List<User>) userRepository.findAll();
        model.addAttribute("userList", userList);
        return "users";
    }

    @GetMapping("/user/new")
    public String showNewUserForm(Model model, Principal principal, HttpSession session) {
        userSession(model, principal, session);
        User user = new User();
        model.addAttribute("user", user);
        List<Role> listRoles = (List<Role>) roleRepository.findAll();
        model.addAttribute("listRoles", listRoles);
        return "new_user";
    }

    @PostMapping( "/user/new/save")
    public String saveUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/";
    }

    @RequestMapping("user/edit/{id}")
    public ModelAndView userEditPage(@PathVariable("id") long id, Model model, Principal principal, HttpSession session) {
        userSession(model, principal, session);
        ModelAndView modelAndView;
        modelAndView = new ModelAndView("edit_user_2");
        modelAndView.addObject(userRepository.getUserById(id));
        List<Role> listRoles = (List<Role>) roleRepository.findAll();
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("newUser", new User());
        return modelAndView;
    }

    @PostMapping("/user/edit/update")
    public String updateUser(@ModelAttribute("user") User user, Model model, Principal principal, HttpSession session) {
        userSession(model, principal, session);
        User newUser = userRepository.getUserById(user.getId());
        newUser.setEnabled(user.isEnabled());
        newUser.setUsername(user.getUsername());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setRoles(user.getRoles());
        userRepository.save(newUser);

        return "redirect:/user";
    }
}