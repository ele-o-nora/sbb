package ru.tsystems.sbb.model.dao;

import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.RouteStation;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.StationsDistance;
import ru.tsystems.sbb.model.entities.Tariff;
import ru.tsystems.sbb.model.entities.Train;

import java.util.List;

/**
 * Direct interaction with database regarding updating railway network,
 *  schedule, and pricing.
 */
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
    Train getTrainById(int trainId);
    void add(StationsDistance stationsDistance);
    void deleteDistance(Station s1, Station s2);
    void add(Tariff tariff);
}
