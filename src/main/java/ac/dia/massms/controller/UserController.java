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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("title", "MASSMS - Users");
        return "users";
    }

    @PostMapping( "/user/new/save")
    public String saveUser(@ModelAttribute("user") User user, RedirectAttributes attributes) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        attributes.addFlashAttribute("success", user.getFirstName() + ' ' + user.getLastName() + " date is successfully updated");
        return "redirect:/";
    }

    @RequestMapping("user/edit/{id}")
    public ModelAndView userEditPage(@PathVariable("id") long id, Model model, Principal principal, HttpSession session) {
        userSession(model, principal, session);
        ModelAndView modelAndView;
        modelAndView = new ModelAndView("edit_user");
        User user = userRepository.getUserById(id);
        modelAndView.addObject("user", user);
        List<Role> listRoles = (List<Role>) roleRepository.findAll();
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("newUser", new User());
        model.addAttribute("title", "Edit " + user.getFirstName()  + " User");
        return modelAndView;
    }

    @PostMapping("/user/edit/update")
    public String updateUser(@ModelAttribute("user") User user, Model model, Principal principal, HttpSession session, RedirectAttributes attributes) {
        userSession(model, principal, session);
        User newUser = userRepository.getUserById(user.getId());
        newUser.setEnabled(user.isEnabled());
        newUser.setUsername(user.getUsername());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setRoles(user.getRoles());
        userRepository.save(newUser);
        attributes.addFlashAttribute("success", newUser.getFirstName() + ' ' + newUser.getLastName() + " date is successfully updated");
        return "redirect:/user";
    }

    @RequestMapping("/user/status/{id}")
    public String updateStatus(@PathVariable("id") long id, RedirectAttributes attributes, Principal principal, Model model, HttpSession session) {
        if(principal == null) { return "redirect:/login"; }
        userSession(model, principal, session);
        User newUser = userRepository.getUserById(id);
        newUser.setEnabled(!newUser.isEnabled());
        userRepository.save(newUser);
        attributes.addFlashAttribute("success", newUser.getFirstName() + ' ' + newUser.getLastName() + " status is successfully updated: Status = " + newUser.isEnabled());

        return "redirect:/user";
    }

    @RequestMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, RedirectAttributes attributes, Principal principal, Model model, HttpSession session) {
        if(principal == null) { return "redirect:/login"; }
        try {
            userRepository.delete(userRepository.getUserById(id));
            attributes.addFlashAttribute("success", "User is successfully deleted.");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Internal Server Problem");
        }

        return "redirect:/user";
    }
}