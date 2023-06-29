package ac.dia.massms.service;

import ac.dia.massms.model.Meal;
import ac.dia.massms.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {

    @Autowired
    private MealRepository mealRepository;

    public List<Meal> listAll() { return (List<Meal>) mealRepository.findAll();}

    public Meal getById(long id) { return mealRepository.findById(id); }

    public void update(Meal meal) {
        Meal newMeal = getById(meal.getId());
        newMeal.setName(meal.getName());
        newMeal.setDescription(meal.getDescription());
        newMeal.setPrice(meal.getPrice());
        newMeal.setAllergens(meal.getAllergens());
        newMeal.setServingSize(meal.getServingSize());
        newMeal.setDailyMeals(meal.getDailyMeals());
        newMeal.setServeTime(meal.getServeTime());
        mealRepository.save(newMeal);
    }

    // Save the time
    public void save(Meal meal) {
        mealRepository.save(meal);
    }
//
//    // Delete the time
//    public void delete(long id) { mealRepository.delete(getById(id)); }
}