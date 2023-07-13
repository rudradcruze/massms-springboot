package ac.dia.massms.controller;

import ac.dia.massms.model.Mass;
import ac.dia.massms.model.Meal;
import ac.dia.massms.model.ServeTime;
import ac.dia.massms.service.MassService;
import ac.dia.massms.service.MealService;
import ac.dia.massms.service.ServeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class MealController {

    @Autowired
    private ServeTimeService serveTimeService;

    @Autowired
    private MealService mealService;

    @Autowired
    private MassService massService;

    @RequestMapping(value = "/mass/{url}/meal/new")
    public String newMassMealPage(Model model, Principal principal, @PathVariable String url, HttpSession session) {
        if(principal == null){ return "redirect:/login"; }
        model.addAttribute("meal", new Meal());
        model.addAttribute("serveTimes", serveTimeService.listAll());
        Mass mass = massService.getByUrl(url);
        session.setAttribute("mass", mass);
        model.addAttribute("title", mass.getName() + " New Meal");
        return "new_meal_2";
    }

    @GetMapping(value = "/mass/{url}/meal")
    public String getMassByMeal(@PathVariable String url, Model model, Principal principal, HttpSession session) {
        if(principal == null){ return "redirect:/login"; }
        List<Meal> massMealList = mealService.listByMassUrl(url);
        Mass mass = massService.getByUrl(url);
        model.addAttribute("massMealList", massMealList);
        session.setAttribute("mass", mass);
        return "meals";
    }

    @PostMapping(value = "/mass/{url}/meal/new/save")
    public String saveNewMeal(@ModelAttribute("meal") Meal meal, RedirectAttributes attributes, Principal principal, @PathVariable String url) {

        if(principal == null){ return "redirect:/login"; }

        Mass instanceofMass = massService.getByUrl(url);
        meal.setMass(instanceofMass);
        instanceofMass.getMealList().add(meal);
        mealService.save(meal);
        massService.update(instanceofMass);
        attributes.addFlashAttribute("success", "Meal Successfully Created!");
        return "redirect:/mass/" + url + "/meal";
    }

    @RequestMapping(value = "/mass/{url}/meal/edit/{id}")
    public ModelAndView showEditMealPage(@PathVariable("id") long id, Model model, @PathVariable String url, HttpSession session) {
        ModelAndView modelAndView;
        modelAndView = new ModelAndView("edit_meal");
        Meal meal = mealService.getById(id);
        if (meal != null) {
            modelAndView.addObject("meal", meal);
            List<ServeTime> serveTimes = serveTimeService.listAll();
            model.addAttribute("serveTimes", serveTimes);
        }
        else {
            modelAndView = new ModelAndView("meals");
            model.addAttribute("meals", mealService.listByMassUrl(url));
        }

        return modelAndView;
    }

    @PostMapping("/mass/{url}/meal/edit/update")
    public String updateMeal(Meal meal, RedirectAttributes attributes, Principal principal, @PathVariable String url) {
        if(principal == null){ return "redirect:/login"; }
        meal.setMass(massService.getByUrl(url));
        try {
            meal.setServeTime(serveTimeService.getById(meal.getServeTime().getId()));
            mealService.update(meal);
            attributes.addFlashAttribute("success","Meal Updated successfully");
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update because duplicate.");
            return "redirect:/mass/" + url + "/meal/edit/" + meal.getId();
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Error server");
        }

        return "redirect:/mass/" + url + "/meal";
    }

    @RequestMapping("/mass/{url}/meal/delete/{id}")
    public String deleteMeal(@PathVariable("id") long id, RedirectAttributes attributes, Principal principal, @PathVariable String url) {
        if(principal == null){ return "redirect:/login"; }

        try {
            mealService.delete(id);
            attributes.addFlashAttribute("success", "Meal Delete successfully");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to delete");
        }

        return "redirect:/mass/" + url + "/meal";
    }
}
