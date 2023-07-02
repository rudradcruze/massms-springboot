package ac.dia.massms.repository;

import ac.dia.massms.model.MemberMeal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberMealRepository extends CrudRepository<MemberMeal, Long> {
    MemberMeal getMemberMealById(long id);
}
