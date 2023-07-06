package ac.dia.massms.service;

import ac.dia.massms.model.MassMember;
import ac.dia.massms.repository.MassMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MassMemberService{
    @Autowired
    private MassMemberRepository massMemberRepository;

    public List<MassMember> listAll() { return (List<MassMember>) massMemberRepository.findAll(); }
    public MassMember getById(long id) { return massMemberRepository.getMassMemberById(id); }
    public void save(MassMember massMember) { massMemberRepository.save(massMember); }
    public void delete(long id) { massMemberRepository.delete(getById(id)); }
    public void status(MassMember massMember) {
        massMember.setEnabled(!massMember.isEnabled());
        massMemberRepository.save(massMember);
    }

    public List<MassMember> massMemberListByUserName(String username) {
        return massMemberRepository.getMassMemberByUserUsername(username);
    }
}
