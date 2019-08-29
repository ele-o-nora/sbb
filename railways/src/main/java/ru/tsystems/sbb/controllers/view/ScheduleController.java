package ru.tsystems.sbb.controllers.view;

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
                                            final String stationName,
                                        @RequestParam(value = "dateTime",
                                        required = false)
                                            final String dateTime) {
        return new ModelAndView("schedule",
                viewService.getStationSchedule(stationName, dateTime));
    }

    @PostMapping("/findTrains")
    public ModelAndView findTrainsFromTo(@RequestParam(value = "stationFrom")
                                             final String stationFrom,
                                         @RequestParam(value = "stationTo")
                                             final String stationTo,
                                         @RequestParam(value = "searchType")
                                             final String searchType,
                                         @RequestParam(value = "dateTime")
                                             final String dateTime) {
        return new ModelAndView("trains",
                viewService.getTrainsFromTo(stationFrom, stationTo,
                        dateTime, searchType));
    }
}
