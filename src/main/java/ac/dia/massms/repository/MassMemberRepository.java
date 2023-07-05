package ac.dia.massms.repository;

import ac.dia.massms.model.MassMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MassMemberRepository extends CrudRepository<MassMember, Long> {
    MassMember getMassMemberById(long id);
}
