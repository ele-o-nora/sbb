package ru.tsystems.sbb.services.view;

import org.hibernate.HibernateException;
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

    private static final String FAIL = "Sorry, there were no trains found "
            + "fulfilling your search criteria :(";

    private static final String ERROR = "There was an error processing your request";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public Map<String, Object> getTrainsFromTo(final String origin,
                                               final String destination,
                                               final String dateTime,
                                               final String searchType) {
        Map<String, Object> objects = new HashMap<>();
        try {
            LocalDateTime moment = LocalDateTime.parse(dateTime, FORMATTER);
            List<JourneyDto> trains = scheduleDataService
                    .directTrainsFromTo(origin, destination, moment, searchType);
            if (!trains.isEmpty()) {
                objects.put("trains", trains);
            } else {
                List<TransferTrainsDto> connections = scheduleDataService
                        .trainsWithTransfer(origin, destination,
                                moment, searchType);
                if (!connections.isEmpty()) {
                    objects.put("connections", connections);
                } else {
                    objects.put("fail", FAIL);
                }
            }
            objects.put("origin", origin);
            objects.put("destination", destination);
        } catch (Exception e) {
            objects.put("error", ERROR);
        }

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
    public Map<String, Object> getStationSchedule(final String stationName,
                                                  final String from) {
        try {
            if (from == null || from.isEmpty()) {
                return getStationSchedule(stationName, LocalDateTime.now());
            } else {
                LocalDateTime moment = LocalDateTime.parse(from, FORMATTER);
                return getStationSchedule(stationName, moment);
            }
        } catch (Exception e) {
            Map<String, Object> objects = new HashMap<>();
            objects.put("error", ERROR);
            return objects;
        }
    }

    private Map<String, Object> getStationSchedule(final String stationName,
                                                  final LocalDateTime from) {
        List<ScheduledStopDto> trains = scheduleDataService
                .stationSchedule(stationName, from);
        Map<String, Object> objects = new HashMap<>();
        objects.put("trains", trains);
        objects.put("stationName", stationName);
        objects.put("momentFrom", from.format(FORMATTER));
        return objects;
    }

}
