package ac.dia.massms.repository;

import ac.dia.massms.model.MealDate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealDateRepository extends CrudRepository<MealDate, Long> {
    MealDate findMealDateById(long id);
}
