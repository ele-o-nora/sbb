package ru.tsystems.sbb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.tsystems.sbb.model.dto.ScheduledStopDto;
import ru.tsystems.sbb.services.data.ScheduleDataService;

import java.util.List;

@RestController
public class ScheduleRestController {

    @Autowired
    private ScheduleDataService dataService;

    @GetMapping("/schedule/{id}")
    public List<ScheduledStopDto> todaySchedule(@PathVariable final int id) {
        return dataService.fullTodaySchedule(id);
    }

}
