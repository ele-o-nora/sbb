package ru.tsystems.board.services;

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

@Singleton
public class ScheduleService implements Serializable {

    @Inject
    private RestService restService;

    private static final transient Logger LOGGER = LoggerFactory
            .getLogger(ScheduleService.class);

    private Map<String, Integer> stationsMapByName;
    private Map<Integer, String> stationsMapById;
    private Map<Integer, List<ScheduledStopDto>> scheduleMap;

    /**
     * Initializes maps used to maintain complete stations and schedule info.
     */
    @PostConstruct
    public void init() {
        stationsMapByName = new HashMap<>();
        stationsMapById = new HashMap<>();
        scheduleMap = new HashMap<>();
        updateMaps();
    }

    /**
     * Returns list of station names from corresponding map.
     *
     * @return list of String that is complete list of stations' names
     */
    public List<String> stationsList() {
        List<String> stationNames = new ArrayList<>(stationsMapByName.keySet());
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
        stationsMapByName.clear();
        stationsMapById.clear();
        try {
            StationDto[] stations = restService.getStationList();
            for (StationDto station : stations) {
                stationsMapByName.put(station.getName(), station.getId());
                stationsMapById.put(station.getId(), station.getName());
                List<ScheduledStopDto> schedule = restService
                        .getStationSchedule(station.getId());
                scheduleMap.put(station.getId(), schedule);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get schedule data", e);
        }
    }

    /**
     * Returns id for the station with specific name.
     * @param stationName name of the station for which the id is returned
     * @return id of the station with the specified name
     */
    public int getStationId(final String stationName) {
        return stationsMapByName.get(stationName);
    }

    /**
     * Returns name for the station with specific id.
     * @param id id of the station for which the name is returned
     * @return name of the station with the specified id
     */
    public String getStationName(final int id) {
        return stationsMapById.get(id);
    }

}
