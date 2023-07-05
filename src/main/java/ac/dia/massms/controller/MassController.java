package ac.dia.massms.controller;

import ac.dia.massms.model.Mass;
import ac.dia.massms.model.User;
import ac.dia.massms.repository.UserRepository;
import ac.dia.massms.service.MassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class MassController {
    @Autowired
    private MassService massService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/mass")
    public String showMass(Model model) {
        List<Mass> massList = massService.listAll();
        model.addAttribute("massList", massList);
        return "masses";
    }

    @GetMapping("/mass/new")
    public String newMassPage(Model model, Principal principal) {
        if(principal == null) { return "redirect:/login"; }
        model.addAttribute("newMass", new Mass());
        return "new_mass";
    }

    @PostMapping("/mass/new/save")
    public String saveMass(@ModelAttribute("newMass") Mass newMass, RedirectAttributes attributes, Principal principal, Model model) {

        if(principal == null) { return "redirect:/login"; }
        if (!massService.checkExistsUrl(newMass.getUrl())) {
            newMass.setApproved(false);
            User user = userRepository.getUserByUsername(principal.getName());
            newMass.setUser(user);
            massService.save(newMass);
            attributes.addFlashAttribute("success", "Mass is successfully created, Please wait for admin approval");
            return "redirect:/mass";
        }
        else {
            model.addAttribute("newMass", newMass);
            model.addAttribute("error", "This url is already exist! Please put another url");
            return "new_mass";
        }
    }

    @RequestMapping("/mass/status/{id}")
    public String updateStatus(@PathVariable("id") long id, RedirectAttributes attributes, Principal principal) {
        if(principal == null) { return "redirect:/login"; }
        Mass newMass = massService.getById(id);
        newMass.setApproved(!newMass.isApproved());
        massService.save(newMass);
        attributes.addFlashAttribute("success", "Mass status is successfully updated: Status = " + newMass.isApproved());

        return "redirect:/mass";
    }

}