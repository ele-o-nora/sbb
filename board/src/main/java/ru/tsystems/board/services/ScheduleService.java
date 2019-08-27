package ru.tsystems.board.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tsystems.dto.ScheduledStopDto;
import ru.tsystems.dto.StationDto;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class ScheduleService {

    @Inject
    private RestService restService;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ScheduleService.class);

    private Map<String, Integer> stationsMap;
    private Map<String, List<ScheduledStopDto>> scheduleMap;

    @PostConstruct
    public void init() {
        stationsMap = new HashMap<>();
        scheduleMap = new HashMap<>();
        updateMaps();
    }

    public List<String> stationsList() {
        List<String> stationNames = new ArrayList<>(stationsMap.keySet());
        Collections.sort(stationNames);
        return stationNames;
    }

    public List<ScheduledStopDto> stationSchedule(final String stationName) {
        return scheduleMap.get(stationName);
    }

    public void updateMaps() {
        try {
            StationDto[] stations = restService.getStationList();
            for (StationDto station : stations) {
                stationsMap.put(station.getName(), station.getId());
                List<ScheduledStopDto> schedule = restService
                        .getStationSchedule(station.getId());
                scheduleMap.put(station.getName(), schedule);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get schedule data", e);
        }
    }
}
