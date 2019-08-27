package ru.tsystems.board.controllers;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tsystems.board.services.ScheduleService;
import ru.tsystems.dto.ScheduledStopDto;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.List;

@Named
@ApplicationScoped
@Startup
@Getter
@Setter
public class ScheduleBean {

    @Inject
    private ScheduleService scheduleService;

    private static final String DEFAULT_STATION = "King's Landing";
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ScheduleBean.class);

    private List<String> stations;
    private List<ScheduledStopDto> currentSchedule;
    private String currentStation;

    @PostConstruct
    public void init() {
        currentStation = DEFAULT_STATION;
        stations = scheduleService.stationsList();
        currentSchedule = scheduleService.stationSchedule(currentStation);
    }

    @Schedule(hour = "*")
    public void updateSchedule() {
        LOGGER.info("Updating schedule");
        scheduleService.updateMaps();
        stations = scheduleService.stationsList();
        currentSchedule = scheduleService.stationSchedule(currentStation);
    }

    public void changeStation() {
        currentSchedule = scheduleService.stationSchedule(currentStation);
    }

}
