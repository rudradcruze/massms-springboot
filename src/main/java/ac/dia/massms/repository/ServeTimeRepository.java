package ac.dia.massms.repository;

import ac.dia.massms.model.ServeTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServeTimeRepository extends CrudRepository<ServeTime, Long> {
    ServeTime findServeTimeByIdentifier(String identifier);
    ServeTime findServeTimeById(long id);

    List<ServeTime> findServeTimesByMassUrl(String url);
}
