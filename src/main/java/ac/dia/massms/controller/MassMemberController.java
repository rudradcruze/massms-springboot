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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class MassMemberController {
    @Autowired
    private MassMemberService massMemberService;

    @Autowired
    private MassService massService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @RequestMapping("mass/{url}")
    public String listAllMassMember(@PathVariable("url") String url, Model model, HttpSession session) {
        Mass mass = massService.getByUrl(url);
        model.addAttribute("massMemberList", mass.getMessMemberList());
        model.addAttribute("title", mass.getName() + " Mass Members");
        session.setAttribute("mass", mass);
        return "mass_members_2";
    }

    @RequestMapping("/mass/{url}/new")
    public String addNewMemberIntoMass(@PathVariable("url") String url, Model model, Principal principal) {
        if(principal == null){ return "redirect:/login"; }
        MassMember massMember = new MassMember();
        massMember.setMass(massService.getByUrl(url));
        model.addAttribute("massMember", massMember);
        model.addAttribute("title", massMember.getMass().getName() + " - Add Member");
        model.addAttribute("users", userDetailsService.listByRollName("USER"));
        return "new_mass_member";
    }

    @RequestMapping(value = "/mass/{url}/new/save", method = RequestMethod.POST)
    public String saveMassMember(@PathVariable("url") String url,
                                 @ModelAttribute("massMember") MassMember massMember,
                                 RedirectAttributes attributes,
                                 Principal principal) {
        if(principal == null){ return "redirect:/login"; }

        massMember.setMass(massService.getByUrl(url));
        List<MassMember> massList = massMemberService.massMemberListByUserName(massMember.getUser().getUsername());
        boolean exist = false;

        for (MassMember massMemberEach : massList) {
            exist = (massMemberEach.getUser().getUsername().equals(massMember.getUser().getUsername()));
            break;
        }

        if (exist) {
            attributes.addFlashAttribute("error", massMember.getUser().getUsername() + " is already in " + massMember.getMass().getName() + " this mass.");
            return "redirect:/mass/" + url + "/new";
        } else {
            User user = userDetailsService.getById(massMember.getUser().getId());
            massMember.setEnabled(false);
            massMemberService.save(massMember);
            user.getMassList().add(massMember.getMass());
            userDetailsService.updateMassList(user);

            attributes.addFlashAttribute("success", massMember.getUser().getUsername() + " is successfully added to the " + massMember.getMass().getName() + " mass");
            return "redirect:/mass/" + url + "";
        }
    }

    @RequestMapping(value = "/mass/{url}/status/{id}")
    public String updateStatus(@PathVariable("url") String url,
                               @PathVariable("id") long id,
                               RedirectAttributes attributes,
                               Principal principal) {
        if(principal == null){ return "redirect:/login"; }
        MassMember massMember = massMemberService.getById(id);
        massMember.setEnabled(!massMember.isEnabled());
        massMemberService.save(massMember);
        attributes.addFlashAttribute("success", "Mass member status is successfully updated: Status = " + massMember.isEnabled());
        return "redirect:/mass/" + url + "/";
    }

    @RequestMapping(value = "/mass/{url}/delete/{id}")
    public String deleteMassMember(@PathVariable("url") String url,
                               @PathVariable("id") long id,
                               RedirectAttributes attributes,
                               Principal principal) {

        if(principal == null){ return "redirect:/login"; }
        attributes.addFlashAttribute("success", massMemberService.getById(id).getUser().getUsername() + " is successfully deleted from " + massMemberService.getById(id).getMass().getName() + " mass");
        massMemberService.delete(id);
        return "redirect:/mass/" + url + "/";
    }
}
