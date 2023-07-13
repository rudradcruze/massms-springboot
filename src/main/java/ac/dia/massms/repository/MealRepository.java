package ac.dia.massms.repository;

import ac.dia.massms.model.Meal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends CrudRepository<Meal, Long> {
    Meal findById(long id);

    List<Meal> getMealsByMass_Url(String url);
}
