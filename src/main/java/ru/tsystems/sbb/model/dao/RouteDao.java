package ru.tsystems.sbb.model.dao;

import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.Station;

import java.util.List;

/**
 * Direct interaction with database regarding existing lines and routes.
 */
public interface RouteDao {

    /**
     * Gets from database the list of all existing lines.
     * @return complete list of Line
     */
    List<Line> allLines();

    /**
     * Gets from database the list of all stations on specific line.
     * @param line line for which the list of stations is returned
     * @return list of Station that are linked to specified line
     */
    List<Station> allLineStations(Line line);

    /**
     * Gets from database the line that is identified by specific id.
     * @param lineId id that indicates which line to get
     * @return Line that has specified id
     */
    Line getLineById(int lineId);

    /**
     * Gets from database the list of all routes that run on specific line.
     * @param line line for which the list of routes is returned
     * @return list of Route that are linked to specified line
     */
    List<Route> allLineRoutes(Line line);

    /**
     * Gets from database the list of all stations where specific route stops.
     * @param route route for which the list of stations is returned
     * @return list of Station that are linked to specified route
     */
    List<Station> allRouteStations(Route route);

    /**
     * Gets from database the route that is identified by specific id.
     * @param routeId id that indicates which route to get
     * @return Route that has specified id
     */
    Route getRouteById(int routeId);

    /**
     * Gets from database the list of all existing stations.
     * @return complete list of Station
     */
    List<Station> getAllStations();

    /**
     * Gets from database the list of line-station relations that belong
     *  to specific line where station's order from center is equal to
     *  or greater than specific number.
     * @param line line for which relations will be returned
     * @param from ordinal number indicating first of relations to return
     * @return list of LineStation that belong to the specified line which
     *  position on said line is equal or greater than certain position
     */
    List<LineStation> getLineStations(Line line, int from);

    /**
     * Gets from database the station that is identified by occupying
     *  specific position on specific line.
     * @param line line to which station belongs
     * @param order ordinal number indicating station's position on the line
     * @return Station that occupies specified position on the specified line
     */
    Station getStation(Line line, int order);

    /**
     * Gets from database exact travel distance between two stations, where
     *  the first of the stations lies closer to the center of the network
     *  than the second one.
     * @param from station closer to the centre
     * @param to station farther the center
     * @param line line to which both stations belong
     * @return integer value of the distance between specified stations
     */
    int outboundDistance(Station from, Station to, Line line);

    /**
     * Gets from database exact travel distance between two stations, where
     *  the first of the stations lies farther from the center of the network
     *  than the second one.
     * @param from station farther from the centre
     * @param to station closer to the center
     * @param line line to which both stations belong
     * @return integer value of the distance between specified stations
     */
    int inboundDistance(Station from, Station to, Line line);

    /**
     * Gets from database ordinal number of specific station on specific line.
     * @param line line for which the order is returned
     * @param station station which order is returned
     * @return integer value of specified station's position on specified line
     */
    int getStationOrder(Line line, Station station);
}
