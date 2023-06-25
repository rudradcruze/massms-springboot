package ac.dia.massms.service;

import ac.dia.massms.model.ServeTime;
import ac.dia.massms.repository.ServeTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServeTimeService {
    @Autowired
    private ServeTimeRepository serveTimeRepository;

    // get by identifier name
    public ServeTime getByIdentifier(String identifier) { return serveTimeRepository.findServeTimeByIdentifier(identifier); }

    // get by id
    public ServeTime getById(long id) { return serveTimeRepository.findServeTimeById(id); }


    // update the element
    public ServeTime update(ServeTime serveTime) {
        ServeTime newTime = getById(serveTime.getId());
        newTime.setEndTime(serveTime.getEndTime());
        newTime.setStartTime(serveTime.getStartTime());
        newTime.setIdentifier(serveTime.getIdentifier());
        newTime.setMealList(serveTime.getMealList());
        return serveTimeRepository.save(newTime);
    }


    // Save the time
    public ServeTime save(ServeTime serveTime) { return serveTimeRepository.save(serveTime); }

    // Delete the time
    public void delete(String identifier) { serveTimeRepository.delete(getByIdentifier(identifier)); }
}