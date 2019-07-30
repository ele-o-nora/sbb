package ru.tsystems.sbb.services.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.ScheduledStopDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.model.dto.TransferTrainsDto;
import ru.tsystems.sbb.services.data.RouteDataService;
import ru.tsystems.sbb.services.data.ScheduleDataService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleViewServiceImpl implements ScheduleViewService {

    @Autowired
    private ScheduleDataService scheduleDataService;

    @Autowired
    private RouteDataService routeDataService;

    @Override
    public Map<String, Object> getTrainsFromTo(final String origin,
                                               final String destination,
                                               final String dateTime,
                                               final String searchType) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime moment = LocalDateTime.parse(dateTime, formatter);
        List<JourneyDto> trains = scheduleDataService
                .directTrainsFromTo(origin, destination, moment, searchType);
        Map<String, Object> objects = new HashMap<>();
        if (!trains.isEmpty()) {
            objects.put("trains", trains);
        } else {
            List<TransferTrainsDto> connections = scheduleDataService
                    .trainsWithTransfer(origin, destination,
                            moment, searchType);
            objects.put("connections", connections);
        }
        objects.put("origin", origin);
        objects.put("destination", destination);

        return objects;
    }

    @Override
    public Map<String, Object> getStationsList() {
        List<StationDto> stations = routeDataService.allStations();
        Map<String, Object> objects = new HashMap<>();
        objects.put("stations", stations);
        return objects;
    }

    @Override
    public Map<String, Object> getStationSchedule(final String stationName) {
        return getStationSchedule(stationName, LocalDateTime.now());
    }

    @Override
    public Map<String, Object> getStationSchedule(final String stationName,
                                                  final LocalDateTime from) {
        List<ScheduledStopDto> trains = scheduleDataService
                .stationSchedule(stationName, from);
        Map<String, Object> objects = new HashMap<>();
        objects.put("trains", trains);
        objects.put("stationName", stationName);
        return objects;
    }

}
