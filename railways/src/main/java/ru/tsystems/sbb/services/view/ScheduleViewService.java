package ru.tsystems.sbb.services.view;

import java.util.Map;

/**
 * Interaction with data service regarding existing scheduled journeys
 *  and filling necessary attribute maps.
 */
public interface ScheduleViewService {

    /**
     * Gets from data service list of scheduled stops that contain information
     *  about trains stopping at the station with specific name during certain
     *  period of time starting at specific moment. If moment in time that
     *  sets the beginning of the search period is not specified, current
     *  time is used. Returns map which contains station's name, moment in time
     *  and the list of scheduled stops.
     * @param stationName name of the station for which the schedule is returned
     * @param momentFrom moment in time that sets the start of the search period
     * @return map that contains list of scheduled stops, station name, and
     *  moment in time that indicates the beginning of the search period
     */
    Map<String, Object> getStationSchedule(String stationName,
                                           String momentFrom);

    /**
     * Gets from data service complete list of existing stations. Returns map
     *  which contains said list.
     * @return map that contains full list of the stations
     */
    Map<String, Object> getStationsList();

    /**
     * Gets from data service list of scheduled journeys that stop at origin
     *  and later at destination during specific time period. Search can be
     *  conducted either by departure or by arrival. If no trains are found,
     *  gets from data service list of pairs of journeys connected via some
     *  transfer station. Returns map that contains names of origin and
     *  destination and either list of journeys or pairs of journeys.
     * @param origin station which journey must stop at before destination
     * @param destination station where journey must stop after origin
     * @param dateTime moment setting the beginning or end of the search period
     * @param searchType indicates whether the search is by departure or arrival
     * @return map containing origin, destination, and either list of single
     *  journeys or list of pairs of journeys
     */
    Map<String, Object> getTrainsFromTo(String origin, String destination,
                                        String dateTime, String searchType);

    /**
     * Gets from data service lists of stations by line to be drawn on the map.
     * @return map containing lists of stations for each existing line
     */
    Map<String, Object> prepareRailwayMap();
}
