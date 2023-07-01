package ac.dia.massms.controller;

import ac.dia.massms.model.MealDate;
import ac.dia.massms.service.MealDateService;
import ac.dia.massms.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/meal/date")
    public String mealDateList(Model model) {
        List<MealDate> mealDateList = mealDateService.listAll();
        model.addAttribute("mealDateList", mealDateList);

        return "meal_date";
    }

    @GetMapping("/meal/date/new")
    public String newMealDatePage(Model model, Principal principal) {
        if(principal == null) { return "redirect:/login"; }
        model.addAttribute("mealDate", new MealDate());
        model.addAttribute("meals", mealService.listAll());

        return "new_meal_date";
    }


    @PostMapping("/meal/date/new/save")
    public String saveMealDate(@ModelAttribute("mealDate") MealDate mealDate, RedirectAttributes attributes, Principal principal, String str_mealDate) {

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

        mealDateService.save(mealDate);
        attributes.addFlashAttribute("success", mealDate.getMeal().getName() + " is successfully created.");

        return "redirect:/meal/date/new";
    }

    @RequestMapping("/meal/date/edit/{id}")
    public ModelAndView showEditMealDatePage(@PathVariable("id") long id, Model model) {

        ModelAndView modelAndView;
        modelAndView = new ModelAndView("edit_meal_date");
        modelAndView.addObject("mealDate", mealDateService.getById(id));
        model.addAttribute("meals", mealService.listAll());
        return modelAndView;
    }

    @PostMapping("/meal/date/edit/update")
    public String updateMealDate(@ModelAttribute("mealDate") MealDate mealDate, RedirectAttributes attributes, Principal principal, String str_mealDate) {
        if (principal == null) { return "redirect:/login"; }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(str_mealDate);
            mealDate.setMealDate(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        mealDate.setMeal(mealService.getById(mealDate.getMeal().getId()));

        mealDateService.update(mealDate);
        attributes.addFlashAttribute("success", "Meal date update successfully!");

        return "redirect:/meal/date";
    }
}
