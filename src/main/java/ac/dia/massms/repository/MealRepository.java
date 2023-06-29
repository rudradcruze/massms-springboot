package ac.dia.massms.repository;

import ac.dia.massms.model.Meal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends CrudRepository<Meal, Long> {
    Meal findById(long id);
}
