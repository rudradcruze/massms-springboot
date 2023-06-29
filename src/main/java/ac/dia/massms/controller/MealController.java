package ac.dia.massms.controller;

import ac.dia.massms.model.Meal;
import ac.dia.massms.service.ServeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MealController {

    @Autowired
    private ServeTimeService serveTimeService;

    @GetMapping("/meal/new")
    public String newMealPage(Model model, Principal principal) {
        if(principal == null){ return "redirect:/login"; }
        model.addAttribute("meal", new Meal());
        model.addAttribute("serveTimes", serveTimeService.listAll());
        return "new_meal";
    }
}
