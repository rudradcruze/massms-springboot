package ac.dia.massms.controller;

import ac.dia.massms.model.Mass;
import ac.dia.massms.model.MassMember;
import ac.dia.massms.model.Role;
import ac.dia.massms.model.User;
import ac.dia.massms.repository.UserRepository;
import ac.dia.massms.service.MassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class MassController {
    @Autowired
    private MassService massService;

    @Autowired
    private UserRepository userRepository;

    private void messMethod(Model model, Principal principal, HttpSession session) {
        if (principal != null) {
            User user = userRepository.getUserByUsername(principal.getName());
            session.setAttribute("user", user);
            model.addAttribute("newMass", new Mass());
        } else {
            session.removeAttribute("user");
        }
    }

    @GetMapping("/mass")
    public String showMass(Model model, Principal principal, HttpSession session) {
        if(principal == null) { return "redirect:/login"; }
        List<Mass> massList = null;
        User user = userRepository.getUserByUsername(principal.getName());
        Set<Role> roleList = user.getRoles();

        for (Role role : roleList) {
            if (Objects.equals(role.getName(), "ADMIN") || Objects.equals(role.getName(), "EDITOR") || Objects.equals(role.getName(), "CREATOR")) {
                massList = massService.listAll();
                break;
            } else if (Objects.equals(role.getName(), "MANAGER")) {
                massList = massService.getManagerMasses(principal.getName());
                break;
            }
        }

        model.addAttribute("massList", massList);
        model.addAttribute("title", "MASSMS - Mass");
        messMethod(model, principal, session);
        return "masses";
    }

    @PostMapping("/mass/new/save")
    public String saveMass(@ModelAttribute("newMass") Mass newMass, RedirectAttributes attributes, Principal principal, Model model, HttpSession session) {

        if(principal == null) { return "redirect:/login"; }
        messMethod(model, principal, session);
        if (!massService.checkExistsUrl(newMass.getUrl())) {
            newMass.setApproved(false);
            User user = userRepository.getUserByUsername(principal.getName());
            newMass.setUser(user);
            massService.save(newMass);
            attributes.addFlashAttribute("success", "Mass is successfully created, Please wait for admin approval");
        }
        else {
            attributes.addFlashAttribute("error", "This url is already exist! Please put another url");
        }
        return "redirect:/mass";
    }

    @RequestMapping("/mass/edit/{id}")
    public ModelAndView newMassEditPage(@PathVariable("id") long id, Model model, Principal principal, HttpSession session) {
        Mass mass = massService.getById(id);
        ModelAndView modelAndView;
        if (principal != null) {
            List<Mass> massList = null;
            if (Objects.equals(principal.getName(), mass.getUser().getUsername())) {
                modelAndView = new ModelAndView("edit_mass");
                modelAndView.addObject("mass", mass);
                model.addAttribute("title", "Edit " + mass.getName() + " Mass");
            }
            else {
                modelAndView = new ModelAndView("masses");
                User user = userRepository.getUserByUsername(principal.getName());
                Set<Role> roleList = user.getRoles();

                for (Role role : roleList) {
                    if (Objects.equals(role.getName(), "ADMIN") || Objects.equals(role.getName(), "EDITOR") || Objects.equals(role.getName(), "CREATOR")) {
                        massList = massService.listAll();
                        break;
                    } else if (Objects.equals(role.getName(), "MANAGER")) {
                        massList = massService.getManagerMasses(principal.getName());
                        break;
                    }
                }
            }
            messMethod(model, principal, session);
//            model.addAttribute("newMass", new Mass());
            model.addAttribute("massList", massList);
        }
        else {
            modelAndView = new ModelAndView("index");
            session.removeAttribute("user");
            model.addAttribute("title", "MASSMS - Home");
            List<Mass> massList = massService.listAll();
            model.addAttribute("massList", massList);
        }
        return modelAndView;
    }

    @PostMapping("mass/edit/update")
    public String updateMass(@ModelAttribute("mass") Mass mass,
                             RedirectAttributes attributes,
                             Principal principal,
                             Model model,
                             HttpSession session) {

        if(principal == null) { return "redirect:/login"; }
        messMethod(model, principal, session);
        Mass editMass = massService.getByUrl(mass.getUrl());

        if(editMass == null) {
            massService.update(mass);
            attributes.addFlashAttribute("success", mass.getName() + " Mass is successfully updated");
            return "redirect:/mass";
        }
        else {
            if (editMass.getId() == mass.getId()) {
                massService.update(mass);
                attributes.addFlashAttribute("success", mass.getName() + " Mass is successfully updated");
                return "redirect:/mass";
            } else {
                model.addAttribute("error", "This url is already exist! Please put another url");
                model.addAttribute("mass", mass);
                return "edit_mass";
            }
        }
    }

    @RequestMapping("/mass/status/{id}")
    public String updateStatus(@PathVariable("id") long id, RedirectAttributes attributes, Principal principal, Model model, HttpSession session) {
        if(principal == null) { return "redirect:/login"; }
        messMethod(model, principal, session);
        Mass newMass = massService.getById(id);
        newMass.setApproved(!newMass.isApproved());
        massService.save(newMass);
        attributes.addFlashAttribute("success", "Mass status is successfully updated: Status = " + newMass.isApproved());

        return "redirect:/mass";
    }

    @RequestMapping("/mass/delete/{id}")
    public String deleteMass(@PathVariable("id") long id, RedirectAttributes attributes, Principal principal) {
        if(principal == null) { return "redirect:/login"; }
        attributes.addFlashAttribute("success", massService.getById(id).getName() + " is successfully deleted.");
        massService.delete(id);
        return "redirect:/mass";
    }
}
