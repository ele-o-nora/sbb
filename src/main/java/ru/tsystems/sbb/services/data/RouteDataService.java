package ru.tsystems.sbb.services.data;

import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.RouteDto;
import ru.tsystems.sbb.model.dto.StationDto;

import java.util.List;

public interface RouteDataService {
    List<StationDto> getAllLineStations(int lineId);
    List<StationDto> getAllRouteStations(int routeId);
    List<RouteDto> getAllRoutes(int lineId);
    List<LineDto> getAllLines();
    List<StationDto> allStations();
    LineDto getLine(int lineId);
    RouteDto getRoute(int routeId);
}
