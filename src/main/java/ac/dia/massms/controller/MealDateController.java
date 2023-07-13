package ac.dia.massms.controller;

import ac.dia.massms.model.Mass;
import ac.dia.massms.model.MealDate;
import ac.dia.massms.service.MassService;
import ac.dia.massms.service.MealDateService;
import ac.dia.massms.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class MealDateController {

    @Autowired
    private MealDateService mealDateService;

    @Autowired
    private MealService mealService;

    @Autowired
    private MassService massService;

    @GetMapping("/mass/{url}/meal/date")
    public String mealDateList(Model model, @PathVariable String url, HttpSession session) {
        List<MealDate> mealDateList = mealDateService.getMealDateByMassUrl(url);
        model.addAttribute("mealDateList", mealDateList);
        session.setAttribute("mass", massService.getByUrl(url));

        return "meal_date";
    }

    @GetMapping("/mass/{url}/meal/date/new")
    public String newMealDatePage(Model model, Principal principal, @PathVariable String url) {
        if(principal == null) { return "redirect:/login"; }
        Mass mass = massService.getByUrl(url);
        MealDate mealDate = new MealDate();
        mealDate.setMass(mass);
        model.addAttribute("mealDate", mealDate);
        model.addAttribute("meals", mealService.listAll());

        return "new_meal_date";
    }


    @PostMapping("/mass/{url}/meal/date/new/save")
    public String saveMealDate(@ModelAttribute("mealDate") MealDate mealDate, RedirectAttributes attributes, Principal principal, String str_mealDate, @PathVariable String url) {

        if (principal == null) { return "redirect:/login"; }

        if (mealDate == null)
            return "redirect:/meal/date/new";

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(str_mealDate);
            mealDate.setMealDate(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        mealDate.setMass(massService.getById(mealDate.getMass().getId()));
        mealDateService.save(mealDate);
        attributes.addFlashAttribute("success", mealDate.getMeal().getName() + " is successfully created.");

        return "redirect:/mass/" + url + "/meal/date/";
    }

    @RequestMapping("/mass/{url}/meal/date/edit/{id}")
    public ModelAndView showEditMealDatePage(@PathVariable("id") long id, Model model, @PathVariable String url) {

        ModelAndView modelAndView;
        modelAndView = new ModelAndView("edit_meal_date_2");
        MealDate mealDate = mealDateService.getById(id);
        mealDate.setMass(massService.getByUrl(url));
        modelAndView.addObject("mealDate", mealDate);
        model.addAttribute("meals", mealService.listByMassUrl(url));
        return modelAndView;
    }

    @PostMapping("/mass/{url}/meal/date/edit/update")
    public String updateMealDate(@ModelAttribute("mealDate") MealDate mealDate, RedirectAttributes attributes, Principal principal, String str_mealDate, @PathVariable String url) {
        if (principal == null) { return "redirect:/login"; }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(str_mealDate);
            mealDate.setMealDate(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        mealDate.setMass(massService.getByUrl(url));
        mealDate.setMeal(mealService.getById(mealDate.getMeal().getId()));

        mealDateService.update(mealDate);
        attributes.addFlashAttribute("success", "Meal date update successfully!");

        return "redirect:/mass/" + url + "/meal/date/";
    }

    @RequestMapping("/meal/date/delete/{id}")
    public String deleteMealDate(@PathVariable("id") long id, RedirectAttributes attributes, Principal principal) {
        if (principal == null) { return "redirect:/login"; }

        mealDateService.delete(id);
        attributes.addFlashAttribute("success", "Delete Successful!");
        return "redirect:/meal/date/";
    }
}
