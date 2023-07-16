package ac.dia.massms.controller;

import ac.dia.massms.config.UserDetailsServiceImpl;
import ac.dia.massms.model.Role;
import ac.dia.massms.model.User;
import ac.dia.massms.repository.RoleRepository;
import ac.dia.massms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Controller
public class AuthController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Principal principal, Model model) {
        if (principal != null) return "redirect:/";
        model.addAttribute("title", "MASSMS - Login");
        return "sign_in";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "MASSMS - Registration");
        return "sign_up";
    }
    @GetMapping("/manager/register")
    public String registerManager(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "MASSMS Manager Registration");
        return "sign_up_manager";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute("user") User user,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes attributes) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("user", user);
                return "sign_up";
            }
            User userNew = userDetailsService.getByUserName(user.getUsername());
            if(userNew != null){
                model.addAttribute("username", "Email have already registered");
                model.addAttribute("user", user);
                return "sign_up";
            }
            if(user.getFirstName() == null){
                model.addAttribute("firstName", "First name cannot be null");
                model.addAttribute("user", user);
                return "sign_up";
            }
            if(user.getLastName() == null){
                model.addAttribute("lastName", "Last name cannot be null");
                model.addAttribute("user", user);
                return "sign_up";
            }
            if (user.getPassword().length() >= 5 && user.getPassword().length() <= 20) {
                if(user.getPassword().equals(user.getConfirmPassword())){
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
                    user.setEnabled(false);
                    Set<Role> rolesSet = user.getRoles();
                    rolesSet.add(roleRepository.findRoleByName("USER"));
                    user.setRoles(rolesSet);
                    userRepository.save(user);
                    attributes.addFlashAttribute("success", "User is successfully registered.");
                    return "redirect:/login";
                }else{
                    model.addAttribute("password", "Password is not same");
                    model.addAttribute("user", user);
                }
            } else {
                model.addAttribute("password", "Password should have 5-20 characters");
                model.addAttribute("user", user);
            }
            return "sign_up";
        }catch (Exception e){
            model.addAttribute("error", "Server have ran some problems");
            model.addAttribute("user", user);
            System.out.println(e);
        }
        return "sign_up";
    }

    @PostMapping("/do-register-manager")
    public String processRegisterManager(@Valid @ModelAttribute("user") User user,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes attributes) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("user", user);
                return "sign_up_manager";
            }
            User userNew = userDetailsService.getByUserName(user.getUsername());
            if(userNew != null){
                model.addAttribute("username", "Email have already registered");
                model.addAttribute("user", user);
                return "sign_up_manager";
            }
            if(user.getFirstName() == null){
                model.addAttribute("firstName", "First name cannot be null");
                model.addAttribute("user", user);
                return "sign_up_manager";
            }
            if(user.getLastName() == null){
                model.addAttribute("lastName", "Last name cannot be null");
                model.addAttribute("user", user);
                return "sign_up_manager";
            }
            if (user.getPassword().length() >= 5 && user.getPassword().length() <= 20) {
                if(user.getPassword().equals(user.getConfirmPassword())){
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setConfirmPassword(passwordEncoder.encode(user.getPassword()));
                    user.setEnabled(false);
                    Set<Role> rolesSet = user.getRoles();
                    rolesSet.add(roleRepository.findRoleByName("MANAGER"));
                    user.setRoles(rolesSet);
                    userRepository.save(user);
                    attributes.addFlashAttribute("success", "User is successfully registered.");
                    return "redirect:/login";
                }else{
                    model.addAttribute("password", "Password is not same");
                    model.addAttribute("user", user);
                }
            } else {
                model.addAttribute("password", "Password should have 5-20 characters");
                model.addAttribute("user", user);
            }
            return "sign_up_manager";
        }catch (Exception e){
            model.addAttribute("error", "Server have ran some problems");
            model.addAttribute("user", user);
            System.out.println(e);
        }
        return "sign_up_manager";
    }

    @PostMapping("/account/password/update")
    public String updatePassword(@Valid @ModelAttribute("userPassChange") User userPassChange, String current_password, RedirectAttributes attributes, String user_id) {

        User user = userDetailsService.getById(Long.parseLong(user_id));

        if (userPassChange.getPassword() == null || userPassChange.getConfirmPassword() == null || current_password == null || userPassChange.getPassword().isEmpty() || userPassChange.getConfirmPassword().isEmpty() || current_password.isEmpty() || user_id.isEmpty())
            attributes.addFlashAttribute("error", "Password changing field cannot be null");
        else {
            if (!userPassChange.getPassword().equals(userPassChange.getConfirmPassword()))
                attributes.addFlashAttribute("error", "Password and confirm password must me equal or same.");
            else {
                if (passwordEncoder.matches(current_password, user.getPassword())) {
                    attributes.addFlashAttribute("success", "Your password is successfully changed.");
                    user.setConfirmPassword(user.getPassword());
                    user.setPassword(passwordEncoder.encode(userPassChange.getPassword()));
                    userDetailsService.save(user);
                } else
                    attributes.addFlashAttribute("success", "Your password doesn't match.");
            }
        }

        return "redirect:/account";
    }
}
