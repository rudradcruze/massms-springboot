package ac.dia.massms.service;

import ac.dia.massms.model.MemberMeal;
import ac.dia.massms.repository.MemberMealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberMealService {

    @Autowired
    private MemberMealRepository memberMealRepository;

    public List<MemberMeal> listAll() {
        return (List<MemberMeal>) memberMealRepository.findAll();
    }

    public MemberMeal get(long id) {
        return memberMealRepository.getMemberMealById(id);
    }

    public void save(MemberMeal meal) {
        memberMealRepository.save(meal);
    }
}
