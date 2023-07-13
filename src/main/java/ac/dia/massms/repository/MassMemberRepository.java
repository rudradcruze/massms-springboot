package ac.dia.massms.repository;

import ac.dia.massms.model.MassMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MassMemberRepository extends CrudRepository<MassMember, Long> {
    MassMember getMassMemberById(long id);

//    List<MassMember> getMassMemberByUserUsername(String username);
    List<MassMember> getMassMembersByUserUsernameAndMassId(String username, long id);
}
