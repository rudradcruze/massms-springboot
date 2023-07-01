package ac.dia.massms.service;

import ac.dia.massms.model.MealDate;
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

    // add customer self
    public void addCustomerSel(long customerId, long mealId) {

    }

    public MealDate getById(long id) {
        return mealDateRepository.findMealDateById(id);
    }

    public void save(MealDate mealDate) {
        mealDateRepository.save(mealDate);
    }

    public void delete(long id) {
        mealDateRepository.delete(getById(id));
    }
}
