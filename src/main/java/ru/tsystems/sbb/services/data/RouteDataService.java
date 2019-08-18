package ru.tsystems.sbb.services.data;

import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.RouteDto;
import ru.tsystems.sbb.model.dto.StationDto;

import java.util.List;

/**
 * Interaction with DAO regarding existing lines and routes.
 */
public interface RouteDataService {

    /**
     * Gets from DAO list of all stations belonging to the line with specific
     *  id and returns them in the form of corresponding DTOs.
     * @param lineId id of the line for which stations will be returned
     * @return list of StationDto belonging to the specified line
     */
    List<StationDto> getAllLineStations(int lineId);

    /**
     * Gets from DAO list of all routes belonging to the line with specific id
     *  and returns them in the form of corresponding DTOs.
     * @param lineId id of the line for which routes will be returned
     * @return lsit of RouteDto belonging to the specified line
     */
    List<RouteDto> getAllRoutes(int lineId);

    /**
     * Gets from DAO list of all lines and returns them in the form of
     *  corresponding DTOs.
     * @return complete list of LineDto
     */
    List<LineDto> getAllLines();

    /**
     * Gets from DAO list of all stations and returns them in the form of
     *  corresponding DTOs.
     * @return complete list of StationDto
     */
    List<StationDto> allStations();

    /**
     * Gets from DAO line that is identified by specific id and returns
     *  corresponding DTO.
     * @param lineId id indicating which line will be returned
     * @return LineDto with specified id
     */
    LineDto getLine(int lineId);

    /**
     * Gets from DAO route that is identified by specific id and returns
     *  corresponding DTO.
     * @param routeId id indicating which route will be returned
     * @return RouteDto with specified id
     */
    RouteDto getRoute(int routeId);
}
