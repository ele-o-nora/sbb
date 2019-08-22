package ru.tsystems.sbb.services.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.RouteDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.dto.TrainDto;
import ru.tsystems.sbb.services.data.AdminDataService;
import ru.tsystems.sbb.services.data.RouteDataService;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminViewServiceImpl implements AdminViewService {

    @Autowired
    private RouteDataService routeDataService;

    @Autowired
    private AdminDataService adminDataService;

    @Autowired
    private Clock clock;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AdminViewServiceImpl.class);

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter
            .ofPattern("HH:mm");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ISO_LOCAL_DATE;

    private static final String OUTBOUND = "outbound";

    @Override
    public Map<String, Object> prepAdminPanel() {
        LOGGER.info("Method call: prepAdminPanel() by employee: {}",
                getEmployeeUsername());
        List<LineDto> lines = routeDataService.getAllLines();
        List<TrainDto> trainModels = adminDataService.getAllTrainModels();
        Map<String, Object> objects = new HashMap<>();
        float currentTariff = adminDataService.currentTariff();
        String today = LocalDate.now(clock).format(DATE_FORMATTER);
        objects.put("today", today);
        objects.put("tariff", currentTariff);
        objects.put("lines", lines);
        objects.put("trainModels", trainModels);
        List<RouteDto> routes = new ArrayList<>();
        for (LineDto line : lines) {
            routes.addAll(routeDataService.getAllRoutes(line.getId()));
        }
        objects.put("routes", routes);
        return objects;
    }

    @Override
    public Map<String, Object> addNewStation(final String stationName,
                                             final int lineId,
                                             final int order,
                                             final int distBefore,
                                             final int distAfter) {
        LOGGER.info("Method call: addNewStation({}, {}, {}, {}, {}) by "
                        + "employee: {}", stationName, lineId, order,
                distBefore, distAfter, getEmployeeUsername());
        adminDataService.addNewStation(stationName, lineId, order,
                distBefore, distAfter);
        return getCurrentLineStations(lineId);
    }

    @Override
    public void addNewTrainModel(final String model,
                                 final int seats, final int speed) {
        LOGGER.info("Method call: addNewTrainModel({}, {}, {}) by employee: {}",
                model, seats, speed, getEmployeeUsername());
        adminDataService.addNewTrainModel(model, seats, speed);
    }

    @Override
    public Map<String, Object> getCurrentLineStations(final int lineId) {
        LOGGER.info("Method call: getCurrentLineStations({}) by employee: {}",
                lineId, getEmployeeUsername());
        List<StationDto> stations = routeDataService
                .getAllLineStations(lineId);
        LineDto line = routeDataService.getLine(lineId);
        Map<String, Object> objects = new HashMap<>();
        objects.put("stations", stations);
        objects.put("line", line);
        return objects;
    }

    @Override
    public Map<String, Object> modifyRouteStations(final int lineId,
                                                   final int routeId) {
        LOGGER.info("Method call: modifyRouteStations({}, {}) by employee: {}",
                lineId, routeId, getEmployeeUsername());
        LineDto line = routeDataService.getLine(lineId);
        RouteDto route = routeDataService.getRoute(routeId);
        List<StationDto> lineStations = routeDataService
                .getAllLineStations(lineId);
        Map<String, Object> objects = new HashMap<>();
        objects.put("line", line);
        objects.put("route", route);
        objects.put("stations", lineStations);
        return objects;
    }

    @Override
    public Map<String, Object> newRouteStopPattern(final String routeNumber,
                                                   final int routeId,
                                                   final int lineId,
                                                   final String[] stations) {
        LOGGER.info("Method call: newRouteStopPattern({}, {}, {}, {}) "
                        + "by employee: {}", routeNumber, lineId, routeId,
                stations, getEmployeeUsername());
        LineDto line = routeDataService.getLine(lineId);
        Map<String, Object> objects = new HashMap<>();
        if (routeId > 0) {
            RouteDto route = routeDataService.getRoute(routeId);
            objects.put("route", route);
        }
        objects.put("routeNumber", routeNumber);
        objects.put("line", line);
        objects.put("routeStations", stations);
        return objects;
    }

    @Override
    public void addNewRoute(final String routeNumber, final int lineId,
                            final String[] stations, final int[] waitTimes) {
        LOGGER.info("Method call: addNewRoute({}, {}, {}, {}) by employee: {}",
                routeNumber, lineId, stations, waitTimes,
                getEmployeeUsername());
        adminDataService.addNewRoute(routeNumber, lineId, stations, waitTimes);
    }

    @Override
    public void modifyRoute(final int routeId, final String[] stations,
                            final int[] waitTimes) {
        LOGGER.info("Method call: modifyRoute({}, {}, {}) by employee: {}",
                routeId, stations, waitTimes, getEmployeeUsername());
        adminDataService.modifyRoute(routeId, stations, waitTimes);
    }

    @Override
    public void scheduleRoute(final int routeId, final String departureTime,
                              final String dateFrom, final String dateUntil,
                              final int trainId, final String direction) {
        LOGGER.info("Method call: scheduleRoute({}, {}, {}, {}, {}, {}) "
                        + "by employee: {}", routeId, departureTime,
                dateFrom, dateUntil, trainId, direction, getEmployeeUsername());
        boolean outbound = direction.equalsIgnoreCase(OUTBOUND);
        LocalDate dayFrom = LocalDate.parse(dateFrom, DATE_FORMATTER);
        LocalDate dayUntil = LocalDate.parse(dateUntil, DATE_FORMATTER);
        LocalTime departure = LocalTime.parse(departureTime, TIME_FORMATTER);
        adminDataService.scheduleJourneys(routeId, departure, dayFrom, dayUntil,
                trainId, outbound);
    }

    @Override
    public void updateTariff(final float price) {
        LOGGER.info("Method call: updateTariff({}) by employee: {}", price,
                getEmployeeUsername());
        if (price > 0) {
            adminDataService.updateTariff(price);
        }
    }

    @Override
    public Map<String, Object> lookUpJourneys(final String date,
                                              final int page) {
        LOGGER.info("Method call: lookUpJourneys({}, {}) by employee: {}",
                date, page, getEmployeeUsername());
        LocalDate day = LocalDate.parse(date, DATE_FORMATTER);
        LocalDateTime searchFrom = LocalDateTime.of(day,
                LocalTime.MIDNIGHT);
        Map<String, Object> objects = new HashMap<>();
        objects.put("today", date);
        String previousDay = day.minusDays(1)
                .format(DATE_FORMATTER);
        objects.put("previousDay", previousDay);
        String nextDay = day.plusDays(1).format(DATE_FORMATTER);
        objects.put("nextDay", nextDay);
        if (page > 1) {
            objects.put("previousPage", (page - 1));
        }
        int maxPages = adminDataService.maxJourneyPages(searchFrom);
        if (page < maxPages) {
            objects.put("nextPage", (page + 1));
        }
        List<JourneyDto> journeys = adminDataService
                .getJourneys(searchFrom, page);
        objects.put("journeys", journeys);
        return objects;
    }

    @Override
    public Map<String, Object> listPassengers(final int journeyId,
                                              final int page) {
        LOGGER.info("Method call: listPassengers({}, {}) by employee: {}",
                journeyId, page, getEmployeeUsername());
        Map<String, Object> objects = new HashMap<>();
        JourneyDto journey = adminDataService.getJourneyById(journeyId);
        objects.put("journey", journey);
        if (page > 1) {
            objects.put("previousPage", (page - 1));
        }
        int maxPages = adminDataService.maxPassengerPages(journeyId);
        if (page < maxPages) {
            objects.put("nextPage", (page + 1));
        }
        List<TicketDto> tickets = adminDataService.getTickets(journeyId, page);
        objects.put("tickets", tickets);
        return objects;
    }

    @Override
    public Map<String, Object> journeyInfo(final int journeyId) {
        LOGGER.info("Method call: journeyInfo({}) by employee: {}",
                journeyId, getEmployeeUsername());
        Map<String, Object> objects = new HashMap<>();
        JourneyDto journey = adminDataService.getJourneyById(journeyId);
        objects.put("journey", journey);
        return objects;
    }

    @Override
    public void cancelJourney(int journeyId) {
        LOGGER.info("Method call: cancelJourney({}) by employee: {}",
                journeyId, getEmployeeUsername());
        adminDataService.cancelJourney(journeyId);
    }

    @Override
    public void delayJourney(int journeyId, int delay) {
        LOGGER.info("Method call: delayJourney({}, {}) by employee: {}",
                journeyId, delay, getEmployeeUsername());
        adminDataService.delayJourney(journeyId, delay);
    }

    private String getEmployeeUsername() {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        return auth.getName().replaceAll("[\\n|\\r|\\t]", "_");
    }
}
