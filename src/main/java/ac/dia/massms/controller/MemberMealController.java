package ac.dia.massms.controller;

import ac.dia.massms.config.UserDetailsServiceImpl;
import ac.dia.massms.model.MemberMeal;
import ac.dia.massms.model.UserService;
import ac.dia.massms.service.MealDateService;
import ac.dia.massms.service.MemberMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MemberMealController {

    @Autowired
    private MemberMealService memberMealService;

    @Autowired
    private MealDateService mealDateService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/meal/member")
    public String memberMealListAndAdd(Model model) {
        List<MemberMeal> memberMealList = memberMealService.listAll();
        model.addAttribute("memberMealList", memberMealList);
        model.addAttribute("newMemberMeal", new MemberMeal());
        model.addAttribute("mealDateService", mealDateService.listAll());
        model.addAttribute("listUser", userDetailsService.listALl());

        return "member_meal";
    }
}
