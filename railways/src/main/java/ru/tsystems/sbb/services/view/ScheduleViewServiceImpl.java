package ru.tsystems.sbb.services.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.ScheduledStopDto;
import ru.tsystems.sbb.model.dto.SignUpDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.model.dto.TransferTrainsDto;
import ru.tsystems.sbb.services.data.RouteDataService;
import ru.tsystems.sbb.services.data.ScheduleDataService;

import java.time.Clock;
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

    @Autowired
    private Clock clock;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ScheduleViewServiceImpl.class);

    private static final String FAIL = "Sorry, there were no trains found "
            + "fulfilling your search criteria :(";

    private static final String ERROR = "There was an error processing your "
            + "request. Please check your inputs.";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public Map<String, Object> getTrainsFromTo(final String origin,
                                               final String destination,
                                               final String dateTime,
                                               final String searchType) {
        LOGGER.info("Method call: getTrainsFromTo({}, {}, {}, {})", origin,
                destination, dateTime, searchType);
        Map<String, Object> objects = prepSignUp();
        try {
            LocalDateTime moment = LocalDateTime.parse(dateTime, FORMATTER);
            List<JourneyDto> trains = scheduleDataService
                    .directTrainsFromTo(origin, destination,
                            moment, searchType);
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
            LOGGER.error("Method getTrainsFromTo({}, {}, {}, {}) raised {}",
                    origin, destination, dateTime, searchType, e);
            objects.put("error", ERROR);
        }

        return objects;
    }

    @Override
    public Map<String, Object> getStationsList() {
        List<StationDto> stations = routeDataService.allStations();
        Map<String, Object> objects = prepSignUp();
        objects.put("stations", stations);
        return objects;
    }

    @Override
    public Map<String, Object> getStationSchedule(final String stationName,
                                                  final String momentFrom) {
        LOGGER.info("Method call: getStationSchedule({}, {})", stationName,
                momentFrom);
        try {
            if (momentFrom == null || momentFrom.isEmpty()) {
                return getStationSchedule(stationName,
                        LocalDateTime.now(clock));
            } else {
                LocalDateTime moment = LocalDateTime
                        .parse(momentFrom, FORMATTER);
                return getStationSchedule(stationName, moment);
            }
        } catch (Exception e) {
            LOGGER.error("Method getStationSchedule({}, {}) raised {}",
                    stationName, momentFrom, e);
            Map<String, Object> objects = prepSignUp();
            objects.put("error", ERROR);
            return objects;
        }
    }

    private Map<String, Object> getStationSchedule(final String stationName,
                                                  final LocalDateTime from) {
        List<ScheduledStopDto> trains = scheduleDataService
                .stationSchedule(stationName, from);
        Map<String, Object> objects = prepSignUp();
        objects.put("trains", trains);
        objects.put("stationName", stationName);
        objects.put("momentFrom", from.format(FORMATTER));
        return objects;
    }

    private Map<String, Object> prepSignUp() {
        Map<String, Object> objects = new HashMap<>();
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            objects.put("signUpDto", new SignUpDto());
        }
        return objects;
    }

}
