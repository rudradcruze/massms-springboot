package ac.dia.massms.controller;


import ac.dia.massms.model.ServeTime;
import ac.dia.massms.service.ServeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ServeTimeController {

    @Autowired
    private ServeTimeService serveTimeService;

    @GetMapping("/meal/time")
    public String mealTimes(Model model) {
        List<ServeTime> mealTimes = serveTimeService.listAll();
        model.addAttribute("mealTimes", mealTimes);

        return "meal_times";
    }

    @GetMapping("/meal/time/new")
    public String newServeTime(Model model, Principal principal) {
        if(principal == null){ return "redirect:/login"; }
        model.addAttribute("serveTime", new ServeTime());

        return "new_meal_time";
    }

    @PostMapping("/meal/time/new/save")
    public String saveServeTime(@ModelAttribute("serveTime") ServeTime serveTime, RedirectAttributes attributes, Principal principal) {

        if (principal == null) { return "redirect:/login"; }

        ServeTime dbServeTime = serveTimeService.getByIdentifier(serveTime.getIdentifier());

        if (dbServeTime != null) {
            attributes.addFlashAttribute("error", "Can't insert. This Identifier is already exist!");
            return "redirect:/meal/time/new";
        }

        attributes.addFlashAttribute("success", serveTime.getIdentifier() + " is successfully created.");
        serveTimeService.save(serveTime);

        return "redirect:/meal/time";
    }
}
