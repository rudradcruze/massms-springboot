package ac.dia.massms.service;

import ac.dia.massms.model.Mass;
import ac.dia.massms.repository.MassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MassService {
    @Autowired
    private MassRepository massRepository;

    public Mass getById(long id) {
        return massRepository.getMassById(id);
    }

    public Mass getByUrl(String url) {
        return massRepository.getMassByUrl(url);
    }

    public void save(Mass mass) {
        massRepository.save(mass);
    }

    public void update(Mass mass) {
        Mass newMass = massRepository.getMassById(mass.getId());
        newMass.setAddress(mass.getAddress());
        newMass.setName(mass.getName());
        newMass.setContact(mass.getContact());
        newMass.setEmail(mass.getUrl());
    }
}
