package ac.dia.massms.service;

import ac.dia.massms.model.Mass;
import ac.dia.massms.repository.MassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MassService {
    @Autowired
    private MassRepository massRepository;

    public List<Mass> listAll() { return (List<Mass>) massRepository.findAll(); }

    public Mass findById(Long id) {
        return massRepository.findById(id).get();
    }

    public Mass getById(long id) {
        return massRepository.getMassById(id);
    }

    public Mass getByUrl(String url) { return massRepository.getMassByUrl(url); }

    public void save(Mass mass) {
        massRepository.save(mass);
    }

    public void update(Mass mass) {
        Mass newMass = massRepository.getMassById(mass.getId());
        newMass.setAddress(mass.getAddress());
        newMass.setName(mass.getName());
        newMass.setContact(mass.getContact());
        newMass.setEmail(mass.getEmail());
        newMass.setUrl(mass.getUrl());
        newMass.setMealList(mass.getMealList());
        newMass.setServeTimeList(mass.getServeTimeList());
        massRepository.save(newMass);
    }

    public boolean checkExistsUrl(String url) {
        Mass mass = massRepository.getMassByUrl(url);
        return mass != null;
    }

    public void delete(long id) { massRepository.delete(getById(id)); }

    public List<Mass> getManagerMasses(String username) {
        return massRepository.getMassesByUserUsername(username);
    }
}
