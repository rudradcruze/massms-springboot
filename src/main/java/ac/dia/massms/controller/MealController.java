package ac.dia.massms.controller;

import ac.dia.massms.model.Meal;
import ac.dia.massms.service.MealService;
import ac.dia.massms.service.ServeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class MealController {

    @Autowired
    private ServeTimeService serveTimeService;

    @Autowired
    private MealService mealService;

    @GetMapping("/meal")
    public String mealIndex(Model model) {
        List<Meal> allMeals = mealService.listAll();
        model.addAttribute("meals", allMeals);
        return "meals";
    }

    @GetMapping("/meal/new")
    public String newMealPage(Model model, Principal principal) {
        if(principal == null){ return "redirect:/login"; }
        model.addAttribute("meal", new Meal());
        model.addAttribute("serveTimes", serveTimeService.listAll());
        return "new_meal";
    }

    @PostMapping("/meal/new/save")
    public String saveNewMeal(@ModelAttribute("meal") Meal meal, RedirectAttributes attributes, Principal principal) {

        if(principal == null){ return "redirect:/login"; }

        mealService.save(meal);
        attributes.addFlashAttribute("success", "Meal Successfully Created!");
        return "redirect:/meal";
    }

    @RequestMapping("/meal/edit/{id}")
    public ModelAndView showEditMealPage(@PathVariable("id") long id, Model model) {
        ModelAndView modelAndView;

        modelAndView = new ModelAndView("edit_meal");
        Meal meal = mealService.getById(id);
        if (meal != null) {
            modelAndView.addObject("meal", meal);
            model.addAttribute("serveTimes", serveTimeService.listAll());
        }
        else {
            modelAndView = new ModelAndView("meals");
            model.addAttribute("meals", mealService.listAll());
        }

        return modelAndView;
    }

}
