package ac.dia.massms.controller;

import ac.dia.massms.config.UserDetailsServiceImpl;
import ac.dia.massms.model.*;
import ac.dia.massms.service.MassService;
import ac.dia.massms.service.MealDateService;
import ac.dia.massms.service.MemberMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    @RequestMapping(value = "/mass/{url}/meal/date/member/status/{id}")
    public String grantPayment(Principal principal, @PathVariable String id, @PathVariable String url, RedirectAttributes attributes) {
        Mass mass = massService.getByUrl(url);
        User user = userDetailsService.getByUserName(principal.getName());
        if (Objects.equals(mass.getUser().getUsername(), user.getUsername())) {
            MemberMeal memberMeal = memberMealService.get(Long.parseLong(id));
            if (memberMeal.isPayment())
                attributes.addFlashAttribute("error", "You already paid the amount for meal.");
            else {
                memberMeal.setPayment(true);
                memberMealService.save(memberMeal);
                attributes.addFlashAttribute("success", "Payment is completed.");
            }
        } else
            attributes.addFlashAttribute("error", "You are not belong to this mass.");
        return "redirect:/mass/" + url + "/meal/date/member";
    }

    @RequestMapping(value = "/mass/{url}/meal/date/member")
    public String listMealsByMemberAndMass(Model model, @PathVariable String url, Principal principal, HttpSession session) {

        if (principal == null) {
            return "redirect:/login";
        }

        User user = userDetailsService.getByUserName(principal.getName());

        Set<Role> roleSet = user.getRoles();
        boolean isAdmin = false;

        for (Role role : roleSet) {
            if (Objects.equals(role.getName(), "MANAGER")) {
                isAdmin = true;
                break;
            }
        }
        List<MemberMeal> getAllMealListByUser;
        List<MemberMeal> memberMealList = new ArrayList<>();

        double paymentDue = 0, paymentPaid = 0;


        if (isAdmin) {
            Mass mass = massService.getByUrl(url);
            List<MassMember> massMemberList = mass.getMessMemberList();
            for (MassMember massMember : massMemberList) {
                getAllMealListByUser = memberMealService.getMemberMealListByUser(massMember.getUser().getUsername());
                for (MemberMeal meal : getAllMealListByUser) {
                    if (Objects.equals(meal.getMealDate().getMass().getUrl(), url)) {
                        memberMealList.add(meal);
                    }
                }
            }
        } else {
            getAllMealListByUser = memberMealService.getMemberMealListByUser(user.getUsername());

            for (MemberMeal meal : getAllMealListByUser) {
                if (Objects.equals(meal.getMealDate().getMass().getUrl(), url)) {
                    memberMealList.add(meal);
                    if (meal.isPayment()) {
                        paymentPaid += (meal.getQuantity() * meal.getMealDate().getMeal().getPrice());
                    } else {
                        paymentDue += (meal.getQuantity() * meal.getMealDate().getMeal().getPrice());
                    }
                }
            }
        }

        session.setAttribute("user", user);
        session.setAttribute("mass", massService.getByUrl(url));
        model.addAttribute("paymentDue", paymentDue);
        model.addAttribute("paymentPaid", paymentPaid);

        model.addAttribute("memberMealList", memberMealList);
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

    @RequestMapping(value = "mass/{url}/meal/date/member/new/{mealDateId}")
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

    @RequestMapping(value = "mass/{url}/meal/date/member/edit/{memberMealId}")
    public ModelAndView editMealDatePage(Model model, @PathVariable String memberMealId, @PathVariable String url, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("edit_member_meal");
        MemberMeal memberMeal = memberMealService.get(Long.parseLong(memberMealId));
        modelAndView.addObject("memberMeal", memberMeal);
        session.setAttribute("mass", massService.getByUrl(url));
        return modelAndView;
    }


    @PostMapping("mass/{url}/meal/date/member/edit/update")
    public String saveMemberMealDate(@ModelAttribute("memberMeal") MemberMeal memberMeal, Model model, @PathVariable String url, RedirectAttributes attributes, Principal principal) {
        memberMealService.updateQuantity(memberMeal);
        attributes.addFlashAttribute("success", "Meal quantity is successfully updated");
        return "redirect:/mass/" + url + "/meal/date/member";
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

    @RequestMapping("mass/{url}/meal/date/member/delete/{memberMealId}")
    public String deleteMealDate(@PathVariable("memberMealId") long memberMealId, RedirectAttributes attributes, Principal principal, @PathVariable String url) {
        if (principal == null) { return "redirect:/login"; }

        memberMealService.delete(memberMealId);
        attributes.addFlashAttribute("success", "Meal is successfully deleted!");
        return "redirect:/mass/" + url + "/meal/date/member";
    }
}
