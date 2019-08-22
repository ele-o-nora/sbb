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

    /**
     * Adds new station to the database.
     * @param station station to add to the database
     */
    void add(Station station);

    /**
     * Adds new line-station relation to the database.
     * @param lineStation line-station relation to be added to the database
     */
    void add(LineStation lineStation);

    /**
     * Updates existing line-station relation.
     * @param lineStation line-station relation to be updated
     */
    void update(LineStation lineStation);

    /**
     * Adds new train model to the database.
     * @param train train model to be added to the database
     */
    void add(Train train);

    /**
     * Adds new route to the database.
     * @param route route to be added to the database
     */
    void add(Route route);

    /**
     * Adds new route-station relation to the database.
     * @param routeStation route-station relation to be added to the database
     */
    void add(RouteStation routeStation);

    /**
     * Adds new scheduled journey to the database.
     * @param journey journey to be added to the database
     */
    void add(Journey journey);

    /**
     * Adds new scheduled stop to the database.
     * @param scheduledStop stop to be added to the database
     */
    void add(ScheduledStop scheduledStop);

    /**
     * Removes current route-station relations from the database.
     * @param route route for which relations are to be removed
     */
    void cleanRouteStopPattern(Route route);

    /**
     * Gets from database all existing train models.
     * @return complete list of Train
     */
    List<Train> getAllTrainModels();

    /**
     * Gets from database train model with specific id.
     * @param trainId id indication which train model will be returned
     * @return Train identified by specified id
     */
    Train getTrainById(int trainId);

    /**
     * Adds new distance between stations to the database.
     * @param stationsDistance distance between stations to be added
     */
    void add(StationsDistance stationsDistance);

    /**
     * Removes from database distance between specific stations.
     * @param s1 first station for which distance is to be removed
     * @param s2 second station for which distance is to be removed
     */
    void deleteDistance(Station s1, Station s2);

    /**
     * Adds new tariff to the database.
     * @param tariff tariff to be added to the database
     */
    void add(Tariff tariff);

    /**
     * Updates existing scheduled journey.
     * @param journey journey to be updated
     */
    void update(Journey journey);
}
