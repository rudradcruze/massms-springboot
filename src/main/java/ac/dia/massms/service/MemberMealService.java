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

    // add customer
    public void addCustomerSel(long customerId, int quantity, long mealDateId) {
        User user = userRepository.getUserById(customerId);
        MealDate mealDate = mealDateRepository.findMealDateById(mealDateId);
        MemberMeal memberMeal = new MemberMeal();
        memberMeal.setUser(user);
        memberMeal.setQuantity(quantity);
        memberMeal.setMealDate(mealDate);
        memberMealRepository.save(memberMeal);
        user.getMemberMealList().add(memberMeal);
        userRepository.save(user);
    }

    public void updateCustomerSelfQuantity(MemberMeal memberMeal) {
        MemberMeal newMemberMeal = memberMealRepository.getMemberMealById(memberMeal.getId());
        newMemberMeal.setQuantity(memberMeal.getQuantity() + 1);
        memberMealRepository.save(newMemberMeal);
    }

    public void updateQuantity(MemberMeal memberMeal) {
        MemberMeal newMemberMeal = memberMealRepository.getMemberMealById(memberMeal.getId());
        newMemberMeal.setUser(userRepository.getUserById(memberMeal.getUser().getId()));
        newMemberMeal.setMealDate(mealDateRepository.findMealDateById(memberMeal.getMealDate().getId()));
        newMemberMeal.setQuantity(memberMeal.getQuantity());
        memberMealRepository.save(newMemberMeal);
    }

    public void save(MemberMeal meal) {
        memberMealRepository.save(meal);
    }

    public void delete(long id) { memberMealRepository.delete(get(id)); }

    public MemberMeal getMemberByMealDateIdAndUserUserName(long id, String username) {
        return memberMealRepository.getMemberMealByMealDateIdAndUserUsername(id, username);
    }

    public List<MemberMeal> getMemberMealListByUser(String username) {
        return memberMealRepository.getMemberMealsByUserUsername(username);
    }
}
