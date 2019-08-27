package ru.tsystems.sbb.controllers.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsystems.dto.ScheduledStopDto;
import ru.tsystems.dto.StationDto;
import ru.tsystems.sbb.services.data.RouteDataService;
import ru.tsystems.sbb.services.data.ScheduleDataService;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ScheduleRestController {

    @Autowired
    private ScheduleDataService scheduleDataService;

    @Autowired
    private RouteDataService routeDataService;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ScheduleRestController.class);

    @GetMapping("/schedule/{id}")
    public List<ScheduledStopDto> todaySchedule(@PathVariable final int id) {
        LOGGER.trace("Rest request: todaySchedule({})", id);
        return scheduleDataService.currentSchedule(id);
    }

    @GetMapping("/stations")
    public List<StationDto> allStations() {
        LOGGER.trace("Rest request: allStations()");
        return routeDataService.allStations();
    }

}
