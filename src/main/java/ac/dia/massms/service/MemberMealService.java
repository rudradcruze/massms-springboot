package ac.dia.massms.service;

import ac.dia.massms.model.MealDate;
import ac.dia.massms.model.MemberMeal;
import ac.dia.massms.model.User;
import ac.dia.massms.repository.MealDateRepository;
import ac.dia.massms.repository.MemberMealRepository;
import ac.dia.massms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberMealService {

    @Autowired
    private MemberMealRepository memberMealRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MealDateRepository mealDateRepository;

    public List<MemberMeal> listAll() {
        return (List<MemberMeal>) memberMealRepository.findAll();
    }

    public MemberMeal get(long id) {
        return memberMealRepository.getMemberMealById(id);
    }

    // add customer self
    public void addCustomerSel(long customerId, MemberMeal memberMeal, long mealDateId) {
        User user = userRepository.getUserById(customerId);
        MealDate mealDate = mealDateRepository.findMealDateById(mealDateId);
        memberMeal.setUser(user);
        memberMeal.setMeal(mealDate);
        memberMealRepository.save(memberMeal);
    }

    public void save(MemberMeal meal) {
        memberMealRepository.save(meal);
    }
}
