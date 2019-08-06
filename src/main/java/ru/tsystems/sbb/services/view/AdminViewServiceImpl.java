package ru.tsystems.sbb.services.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.RouteDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.model.entities.Train;
import ru.tsystems.sbb.services.data.AdminDataService;
import ru.tsystems.sbb.services.data.RouteDataService;

import java.time.LocalDate;
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

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter
            .ofPattern("HH:mm");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ISO_LOCAL_DATE;

    private static final String OUTBOUND = "outbound";

    @Override
    public Map<String, Object> prepAdminPanel() {
        List<LineDto> lines = routeDataService.getAllLines();
        List<Train> trainModels = adminDataService.getAllTrainModels();
        Map<String, Object> objects = new HashMap<>();
        float currentTariff = adminDataService.currentTariff();
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
        adminDataService.addNewStation(stationName, lineId, order,
                distBefore, distAfter);
        return getCurrentLineStations(lineId);
    }

    @Override
    public void addNewTrainModel(final String model,
                                 final int seats, final int speed) {
        adminDataService.addNewTrainModel(model, seats, speed);
    }

    @Override
    public Map<String, Object> getCurrentLineStations(final int lineId) {
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
        LineDto line = routeDataService.getLine(lineId);
        RouteDto route = routeDataService.getRoute(routeId);
        List<StationDto> lineStations = routeDataService.getAllLineStations(lineId);
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
        adminDataService.addNewRoute(routeNumber, lineId, stations, waitTimes);
    }

    @Override
    public void modifyRoute(final int routeId, final String[] stations,
                            final int[] waitTimes) {
        adminDataService.modifyRoute(routeId, stations, waitTimes);
    }

    @Override
    public void scheduleRoute(final int routeId, final String departureTime,
                              final String dateFrom, final String dateUntil,
                              final int trainId, final String direction) {
        boolean outbound = direction.equalsIgnoreCase(OUTBOUND);
        LocalDate dayFrom = LocalDate.parse(dateFrom, DATE_FORMATTER);
        LocalDate dayUntil = LocalDate.parse(dateUntil, DATE_FORMATTER);
        LocalTime departure = LocalTime.parse(departureTime, TIME_FORMATTER);
        adminDataService.scheduleJourneys(routeId, departure, dayFrom, dayUntil,
                trainId, outbound);
    }

    @Override
    public void updateTariff(final float price) {
        adminDataService.updateTariff(price);
    }
}
