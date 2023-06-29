package ac.dia.massms.controller;


import ac.dia.massms.model.ServeTime;
import ac.dia.massms.service.ServeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ServeTimeController {

    @Autowired
    private ServeTimeService serveTimeService;

    // list of meals
    @GetMapping("/meal/time")
    public String mealTimes(Model model) {
        List<ServeTime> mealTimes = serveTimeService.listAll();
        model.addAttribute("mealTimes", mealTimes);

        return "meal_times";
    }

    // Get new creating page
    @GetMapping("/meal/time/new")
    public String newServeTime(Model model, Principal principal) {
        if(principal == null){ return "redirect:/login"; }
        model.addAttribute("serveTime", new ServeTime());

        return "new_meal_time";
    }

    // Save new
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

    // Update page
    @RequestMapping("/meal/time/{identifier}")
    public ModelAndView showEditMealTimePage(@PathVariable("identifier") String identifier, Model model, Principal principal) {
        ModelAndView modelAndView;

        modelAndView = new ModelAndView("edit_meal_time");
        ServeTime serveTime = serveTimeService.getByIdentifier(identifier);
        if (serveTime != null)
            modelAndView.addObject("serveTime", serveTime);
        else {
            modelAndView = new ModelAndView("meal_times");
            model.addAttribute("mealTimes", serveTimeService.listAll());
        }

        return modelAndView;
    }

    // update
    @PostMapping("/meal/time/update")
    public String update(ServeTime serveTime, RedirectAttributes attributes, Principal principal){

        if(principal == null){ return "redirect:/login"; }

        try {
            serveTimeService.update(serveTime);
            attributes.addFlashAttribute("success","Updated successfully");
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update because duplicate name");
            return "redirect:/meal/time/" + serveTime.getIdentifier();
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Error server");
        }
        return "redirect:/meal/time";
    }
}
