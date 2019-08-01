package ru.tsystems.sbb.model.dao;

import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.RouteStation;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.StationsDistance;
import ru.tsystems.sbb.model.entities.Train;

import java.util.List;

public interface AdminDao {
    void add(Station station);
    void add(LineStation lineStation);
    void update(LineStation lineStation);
    void add(Train train);
    void add(Route route);
    void add(RouteStation routeStation);
    void add(Journey journey);
    void add(ScheduledStop scheduledStop);
    void cleanRouteStopPattern(Route route);
    List<Train> getAllTrainModels();
    void add(StationsDistance stationsDistance);
    Station getStation(Line line, int order);
    void deleteDistance(Station s1, Station s2);
}
