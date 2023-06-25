package ac.dia.massms.controller;


import ac.dia.massms.model.ServeTime;
import ac.dia.massms.service.ServeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ServeTimeController {

    @Autowired
    private ServeTimeService serveTimeService;

    @GetMapping("/meal/time")
    public String mealTimes(Model model) {
        List<ServeTime> mealTimes = serveTimeService.listAll();
        model.addAttribute("mealTimes", mealTimes);

        return "meal_times";
    }
}
