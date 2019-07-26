package ru.tsystems.sbb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.sbb.services.view.ScheduleViewService;

@Controller
public class ScheduleController {

    @Autowired
    private ScheduleViewService viewService;

    @PostMapping("/curSchedule")
    public ModelAndView stationSchedule(@RequestParam(value = "stationName")
                                            final String stationName) {
        return new ModelAndView("schedule",
                viewService.getStationSchedule(stationName));
    }
}
