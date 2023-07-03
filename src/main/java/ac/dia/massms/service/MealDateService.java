package ac.dia.massms.service;

import ac.dia.massms.model.MealDate;
import ac.dia.massms.model.MemberMeal;
import ac.dia.massms.repository.MealDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealDateService {
    @Autowired
    private MealDateRepository mealDateRepository;

    public List<MealDate> listAll() {
        return (List<MealDate>) mealDateRepository.findAll();
    }

    public MealDate getById(long id) {
        return mealDateRepository.findMealDateById(id);
    }

    public void save(MealDate mealDate) {
        mealDateRepository.save(mealDate);
    }

    public void update(MealDate mealDate) {
        MealDate newMealDate = mealDateRepository.findMealDateById(mealDate.getId());
        newMealDate.setMeal(mealDate.getMeal());
        newMealDate.setMealDate(mealDate.getMealDate());
        mealDateRepository.save(newMealDate);
    }

    public void delete(long id) {
        try {
            mealDateRepository.delete(getById(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
