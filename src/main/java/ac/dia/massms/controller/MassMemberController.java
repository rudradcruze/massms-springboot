package ac.dia.massms.controller;

import ac.dia.massms.model.Mass;
import ac.dia.massms.model.MassMember;
import ac.dia.massms.service.MassMemberService;
import ac.dia.massms.service.MassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MassMemberController {
    @Autowired
    private MassMemberService massMemberService;

    @Autowired
    private MassService massService;

    @RequestMapping("mass/{url}/member")
    public String listAllMassMember(@PathVariable("url") String url, Model model, HttpSession session) {
        List<MassMember> massMemberList = massMemberService.listAll();
        Mass mass = massService.getByUrl(url);
        model.addAttribute("massMemberList", mass.getMessMemberList());
        session.setAttribute("mass", mass);
        return "mass_members";
    }
}
