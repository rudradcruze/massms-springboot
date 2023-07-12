package ac.dia.massms.controller;

import ac.dia.massms.config.UserDetailsServiceImpl;
import ac.dia.massms.model.User;
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

@Controller
public class AuthController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Principal principal, Model model) {
        if (principal != null) return "redirect:/";
        model.addAttribute("title", "MASSMS - Login");
        return "sign_in";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("title", "MASSMS - Register");
        return "sign_up";
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
}