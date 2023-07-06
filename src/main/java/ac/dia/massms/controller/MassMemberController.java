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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping("mass/{url}/member")
    public String listAllMassMember(@PathVariable("url") String url, Model model, HttpSession session) {
        List<MassMember> massMemberList = massMemberService.listAll();
        Mass mass = massService.getByUrl(url);
        model.addAttribute("massMemberList", mass.getMessMemberList());
        session.setAttribute("mass", mass);
        return "mass_members";
    }

    @RequestMapping("/mass/{url}/member/new")
    public String addNewMemberIntoMass(@PathVariable("url") String url, Model model, Principal principal) {
        if(principal == null){ return "redirect:/login"; }
        MassMember massMember = new MassMember();
        massMember.setMass(massService.getByUrl(url));
        model.addAttribute("massMember", massMember);
        model.addAttribute("users", userDetailsService.listByRollName("USER"));
         model.addAttribute("masses", massService.listAll());
        return "new_mass_member";
    }
}
