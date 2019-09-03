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
     * Gets from database partial list of stations from specific line that
     *  starts and ends on specific stations.
     * @param first order of the first station to be included in the list
     * @param last order of the last station to be included in the list
     * @param line line for which the list of stations is returned
     * @return list of Station that are linked to specified Line limited by
     *  specified ordinal numbers
     */
    List<Station> getStations(int first, int last, Line line);

    /**
     * Gets from database ordinal number of specific station on specific line.
     * @param line line for which the order is returned
     * @param station station which order is returned
     * @return integer value of specified station's position on specified line
     */
    int getStationOrder(Line line, Station station);
}
