package ac.dia.massms.service;

import ac.dia.massms.model.ServeTime;
import ac.dia.massms.repository.ServeTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServeTimeService {
    @Autowired
    private ServeTimeRepository serveTimeRepository;

    // list all
    public List<ServeTime> listAll() { return (List<ServeTime>) serveTimeRepository.findAll(); }

    // get by identifier name
    public ServeTime getByIdentifier(String identifier) { return serveTimeRepository.findServeTimeByIdentifier(identifier); }

    // get by id
    public ServeTime getById(long id) { return serveTimeRepository.findServeTimeById(id); }


    // update the element
    public void update(ServeTime serveTime) {
        ServeTime newTime = getById(serveTime.getId());
        newTime.setEndTime(serveTime.getEndTime());
        newTime.setStartTime(serveTime.getStartTime());
        newTime.setIdentifier(serveTime.getIdentifier());
        newTime.setMealList(serveTime.getMealList());
        serveTimeRepository.save(newTime);
    }


    // Save the time
    public void save(ServeTime serveTime) {
        serveTimeRepository.save(serveTime);
    }

    // Delete the time
    public void delete(String identifier) { serveTimeRepository.delete(getByIdentifier(identifier)); }

    public List<ServeTime> getAllServeTimeByMassUrl(String url) {
        return serveTimeRepository.findServeTimesByMassUrl(url);
    }
}