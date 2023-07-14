package ac.dia.massms.repository;

import ac.dia.massms.model.Mass;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MassRepository extends CrudRepository<Mass, Long> {
    Mass getMassById(long id);
    Mass getMassByUrl(String url);
    List<Mass> getMassesByUserUsername(String username);
}
