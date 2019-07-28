package ru.tsystems.sbb.model.dao;

import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.Station;

import java.util.List;

public interface RouteDao {
    List<Line> allLines();
    List<Station> allLineStations(Line line);
    Line getLineById(int lineId);
    List<Route> allLineRoutes(Line line);
    List<Station> allRouteStations(Route route);
    Route getRouteById(int routeId);
    List<Station> getAllStations();
}
