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

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/user")
    public String showAllUsers(Model model) {
        List<User> userList = (List<User>) userRepository.findAll();
        model.addAttribute("userList", userList);
        return "users";
    }

    @GetMapping("/user/new")
    public String showNewUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        List<Role> listRoles = (List<Role>) roleRepository.findAll();
        model.addAttribute("listRoles", listRoles);
        return "new_user";
    }

    @PostMapping( "/user/new/save")
    public String saveUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        userRepository.save(user);
        return "redirect:/";
    }

    @RequestMapping("user/edit/{id}")
    public ModelAndView userEditPage(@PathVariable("id") long id, Model model) {
        ModelAndView modelAndView;
        modelAndView = new ModelAndView("edit_user");
        modelAndView.addObject(userRepository.getUserById(id));
        List<Role> listRoles = (List<Role>) roleRepository.findAll();
        model.addAttribute("listRoles", listRoles);
        return modelAndView;
    }

    @PostMapping("/user/edit/update")
    public String updateUser(@ModelAttribute("user") User user) {
        User newUser = userRepository.getUserById(user.getId());
        newUser.setEnabled(user.isEnabled());
        newUser.setUsername(user.getUsername());
        newUser.setRoles(user.getRoles());
        userRepository.save(newUser);

        return "redirect:/user";
    }
}
