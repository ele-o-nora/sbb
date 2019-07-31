package ru.tsystems.sbb.services.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.RouteDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.model.entities.Train;
import ru.tsystems.sbb.services.data.AdminDataService;
import ru.tsystems.sbb.services.data.RouteDataService;

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

    @Override
    public Map<String, Object> prepAdminPanel() {
        List<LineDto> lines = routeDataService.getAllLines();
        List<Train> trainModels = adminDataService.getAllTrainModels();
        Map<String, Object> objects = new HashMap<>();
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
                                             final int zone,
                                             final int order) {
        adminDataService.addNewStation(stationName, lineId, zone, order);
        return getCurrentLineStations(lineId);
    }

    @Override
    public void addNewTrainModel(String model, int seats) {
        adminDataService.addNewTrainModel(model, seats);
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
                            final String[] stations, final int[] timesEnRoute,
                            final int[] waitTimes) {
        adminDataService.addNewRoute(routeNumber, lineId, stations, timesEnRoute, waitTimes);
    }

    @Override
    public void modifyRoute(final int routeId, final String[] stations,
                            final int[] timesEnRoute, final int[] waitTimes) {
        adminDataService.modifyRoute(routeId, stations, timesEnRoute, waitTimes);
    }
}
