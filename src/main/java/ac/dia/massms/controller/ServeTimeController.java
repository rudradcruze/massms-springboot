package ac.dia.massms.controller;

import ac.dia.massms.model.Mass;
import ac.dia.massms.model.ServeTime;
import ac.dia.massms.model.User;
import ac.dia.massms.repository.UserRepository;
import ac.dia.massms.service.MassService;
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

    @Autowired
    private MassService massService;

    @Autowired
    private UserRepository userRepository;

    // list of meals
    @RequestMapping(value = "/mass/{url}/meal/time")
    public String mealTimes(Model model, @PathVariable String url) {
        List<ServeTime> mealTimes = serveTimeService.listAll();
        model.addAttribute("mealTimes", mealTimes);
        model.addAttribute("mass", massService.getByUrl(url));
        return "meal_times";
    }

    // Get new creating page
    @RequestMapping(value = "/mass/{url}/meal/time/new")
    public String newServeTime(Model model, Principal principal, @PathVariable String url) {
        if(principal == null){ return "redirect:/login"; }
        ServeTime serveTime = new ServeTime();
        Mass mass = massService.getByUrl(url);
        serveTime.setMass(mass);
        model.addAttribute("serveTime", serveTime);
        return "new_meal_time_2";
    }

    // Save new
    @PostMapping("/mass/{url}/meal/time/new/save")
    public String saveServeTime(@ModelAttribute("serveTime") ServeTime serveTime, RedirectAttributes attributes, Principal principal, @PathVariable String url) {

        if (principal == null) { return "redirect:/login"; }

        ServeTime dbServeTime = serveTimeService.getByIdentifier(serveTime.getIdentifier());

        if (dbServeTime != null) {
            attributes.addFlashAttribute("error", "Can't insert. This Identifier is already exist!");
            return "redirect:/meal/time/new";
        }

        Mass mass = massService.getByUrl(url);
        serveTime.setMass(mass);
        attributes.addFlashAttribute("success", serveTime.getIdentifier() + " is successfully created.");
        serveTimeService.save(serveTime);
        mass.getServeTimeList().add(serveTime);
        massService.save(mass);

        return "redirect:/mass/" + mass.getUrl() + "/meal/time";
    }

    // Update page
    @RequestMapping("/meal/time/edit/{identifier}")
    public ModelAndView showEditMealTimePage(@PathVariable("identifier") String identifier, Model model) {
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
    @PostMapping("/meal/time/edit/update")
    public String update(ServeTime serveTime, RedirectAttributes attributes, Principal principal){

        if(principal == null){ return "redirect:/login"; }

        try {
            serveTimeService.update(serveTime);
            attributes.addFlashAttribute("success","Updated successfully");
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update because duplicate name");
            return "redirect:/meal/time/edit/" + serveTime.getIdentifier();
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Error server");
        }
        return "redirect:/meal/time";
    }

    @RequestMapping("/meal/time/delete/{identifier}")
    public String deleteCategory(@PathVariable("identifier") String identifier, RedirectAttributes attributes) {
        try {
            serveTimeService.delete(identifier);
            attributes.addFlashAttribute("success", "Delete successfully");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "Failed to delete");
        }

        return "redirect:/meal/time";
    }
}
