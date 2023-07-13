package ac.dia.massms.controller;

import ac.dia.massms.config.UserDetailsServiceImpl;
import ac.dia.massms.model.Mass;
import ac.dia.massms.model.MassMember;
import ac.dia.massms.model.MemberMeal;
import ac.dia.massms.model.User;
import ac.dia.massms.service.MassService;
import ac.dia.massms.service.MealDateService;
import ac.dia.massms.service.MemberMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
public class MemberMealController {

    @Autowired
    private MassService massService;

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

    public boolean activateCheck(String massUrl, String userName) {
        boolean status = false;
        Mass mass = massService.getByUrl(massUrl);
        List<MassMember> massUserList = mass.getMessMemberList();
        User userCheck = userDetailsService.getByUserName(userName);
        for (MassMember member : massUserList) {
            if (member.getUser().getId() == userCheck.getId() && member.isEnabled()) {
                status = true;
                break;
            }
        }
        return status;
    }

    @RequestMapping(value = "mass/{url}/meal/date/{mealDateId}/member/new")
    public String addSelfIntoMealDate(Model model, @PathVariable String mealDateId, @PathVariable String url, RedirectAttributes attributes, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        User user = userDetailsService.getByUserName(principal.getName());

        if (activateCheck(url, user.getUsername())) {
            MemberMeal memberMealCheck = memberMealService.getMemberByMealDateIdAndUserUserName(Long.parseLong(mealDateId), user.getUsername());
            if (memberMealCheck == null) {
                memberMealService.addCustomerSel(user.getId(), 1, Long.parseLong(mealDateId));
                attributes.addFlashAttribute("success", "You successfully apply for meal with quantity of 1.");
            } else {
                memberMealService.updateCustomerSelfQuantity(memberMealCheck);
                attributes.addFlashAttribute("success", "Your meal quantity is successfully updated.");
            }
        } else {
            attributes.addFlashAttribute("error", "Your not currently active in this mass, please get approval from admin.");
        }

        return "redirect:/mass/" + url + "/meal/date";
    }

    @PostMapping("/meal/member/add")
    public String addMemberIntoMealList(@ModelAttribute("newMemberMeal") MemberMeal memberMeal, Principal principal, RedirectAttributes attributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        memberMealService.addCustomerSel(memberMeal.getUser().getId(), 1, memberMeal.getMealDate().getId());
        attributes.addFlashAttribute("success", "Member successfully added into the meal");

        return "redirect:/meal/member";
    }

    @RequestMapping("/meal/member/edit/{id}")
    public ModelAndView memberMealEditPage(@PathVariable("id") long id, Model model) {
        ModelAndView modelAndView = new ModelAndView("edit_member_meal");
        MemberMeal memberMeal = memberMealService.get(id);
        modelAndView.addObject("memberMeal", memberMeal);
        return modelAndView;
    }

    @PostMapping("/meal/member/edit/update")
    public String updateMemberMeal(@ModelAttribute("memberMeal") MemberMeal memberMeal, RedirectAttributes attributes, Principal principal) {
        if (principal == null) { return "redirect:/login"; }
        MemberMeal newMemberMeal = memberMealService.get(memberMeal.getId());
        newMemberMeal.setQuantity(memberMeal.getQuantity());
        memberMealService.save(newMemberMeal);
        attributes.addFlashAttribute("success", newMemberMeal.getUser().getUsername() + "'s meal is updated!");
        return "redirect:/meal/member/";
    }

    @RequestMapping("/meal/member/delete/{id}")
    public String deleteMealDate(@PathVariable("id") long id, RedirectAttributes attributes, Principal principal) {
        if (principal == null) { return "redirect:/login"; }

        memberMealService.delete(id);
        attributes.addFlashAttribute("success", "Delete Successful!");
        return "redirect:/meal/member/";
    }
}
