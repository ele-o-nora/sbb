package ru.tsystems.board.services;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tsystems.dto.ScheduledStopDto;
import ru.tsystems.dto.StationDto;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public class ScheduleService implements Serializable {

    @Inject
    private RestService restService;

    private static final transient Logger LOGGER = LoggerFactory
            .getLogger(ScheduleService.class);

    private Map<String, Integer> stationsMap;
    private Map<Integer, List<ScheduledStopDto>> scheduleMap;

    @PostConstruct
    public void init() {
        stationsMap = new HashMap<>();
        scheduleMap = new HashMap<>();
        updateMaps();
    }

    /**
     * Returns list of station names from corresponding map.
     *
     * @return list of String that is complete list of stations' names
     */
    public List<String> stationsList() {
        List<String> stationNames = new ArrayList<>(stationsMap.keySet());
        Collections.sort(stationNames);
        return stationNames;
    }

    /**
     * Returns list of ScheduledStopDto that represent current schedule for
     * specific station.
     *
     * @param stationId id of the station for which the schedule is returned
     * @return list of ScheduledStopDto mapped by specified station name
     */
    public List<ScheduledStopDto> stationSchedule(final int stationId) {
        return scheduleMap.get(stationId);
    }

    /**
     * Calls to RestService in order to get current list of stations and current
     * schedule for each of the stations.
     */
    public void updateMaps() {
        scheduleMap.clear();
        stationsMap.clear();
        try {
            StationDto[] stations = restService.getStationList();
            for (StationDto station : stations) {
                stationsMap.put(station.getName(), station.getId());
                List<ScheduledStopDto> schedule = restService
                        .getStationSchedule(station.getId());
                scheduleMap.put(station.getId(), schedule);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get schedule data", e);
        }
    }

    public int getStationId(final String stationName) {
        return stationsMap.get(stationName);
    }

    public String getStationById(final int id) {
        Set<Map.Entry<String, Integer>> stations = stationsMap.entrySet();
        for (Map.Entry<String, Integer> s : stations) {
            if (s.getValue() == id) {
                return s.getKey();
            }
        }
        return null;
    }

}
