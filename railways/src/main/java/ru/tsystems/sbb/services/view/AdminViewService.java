package ru.tsystems.sbb.services.view;

import java.util.Map;

/**
 * Interaction with data service regarding railway network administration
 *  and filling necessary attribute maps.
 */
public interface AdminViewService {

    /**
     * Gets from data service lists of lines, routes, and train models and
     *  current tariff. Returns map that contains aforementioned objects
     *  and String representation of current date.
     * @return map that contains current tariff, current date and lists of
     *  lines, routes and train models
     */
    Map<String, Object> prepAdminPanel();

    /**
     * Gets from data service list of station that belong to a line with
     *  specific id. Returns map containing that list and the line.
     * @param lineId id indicating which line and stations will be returned
     * @return map that contains line specified by id and list of its stations
     */
    Map<String, Object> getCurrentLineStations(int lineId);

    /**
     * Calls to data service to process adding new station with specific name,
     *  that occupies specific position on specific line and is lies at certain
     *  distance from preceding and to following stations on the line. Returns
     *  map containing updated list of this line's stations.
     * @param stationName new station's name
     * @param lineId id of the line to which the station belongs
     * @param order position from centre on the line that new station occupies
     * @param x station's x coordinate
     * @param y station's y coordinate
     * @return map containing updated list of this line's stations
     */
    Map<String, Object> addNewStation(String stationName, int lineId,
                                      int order, int x, int y);

    /**
     * Calls to data service to process adding new train model with specific
     *  name, specific number of seats and specific speed.
     * @param model new train model's name
     * @param seats new train model's number of seats
     * @param speed new train model's speed
     */
    void addNewTrainModel(String model, int seats, int speed);

    /**
     * Gets from data service lists of specific line's stations and specific
     *  route's stations. Returns map containing these lists, line and route.
     * @param lineId id indicating line which stations will be returned
     * @param routeId id indication route which stations will be returned
     * @return map containing line and route specified by ids and lists
     *  of stations for both line and route
     */
    Map<String, Object> modifyRouteStations(int lineId, int routeId);

    /**
     * Gets from data service route with specific if it already exists.
     *  Returns map that contains line, route, route number and the list
     *  of routes stations, where line and route are identified by ids
     *  and route number and stations are method's parameters.
     * @param routeNumber new or existing route number
     * @param routeId route id (zero for new routes)
     * @param lineId id indication which line the route belongs to
     * @param stations list of route's stations' names
     * @return map that contains line with specified id, route if its id is
     *  above 0, route number and list of route's stations' names
     */
    Map<String, Object> newRouteStopPattern(String routeNumber, int routeId,
                                            int lineId, String[] stations);

    /**
     * Calls to data service to process creating new route with specific number
     *  that belongs to specific line, stops at specific stations and spends on
     *  those stations specific number of minutes.
     * @param routeNumber new route's number
     * @param lineId id indicating which line the route belongs to
     * @param stations list of new route's stations' names
     * @param waitTimes list of number of minutes route spends on each station
     */
    void addNewRoute(String routeNumber, int lineId, String[] stations,
                     int[] waitTimes);

    /**
     * Calls to data service to process updating existing route identified by
     *  specific id, that stops at specific stations and spends on those
     *  stations specific number of minutes.
     * @param routeId id indicating which route will be updated
     * @param stations list of route's new stations' names
     * @param waitTimes list of number of minutes route spends on each station
     */
    void modifyRoute(int routeId, String[] stations, int[] waitTimes);

    /**
     * Calls to data service to process creating schedule for a route
     *  identified by specific id, which departs at specific time, is served
     *  by specific train model and runs from specific day to another specific
     *  day. Schedule can be created either outbound or inbound.
     * @param routeId id indication which route is to be scheduled
     * @param departureTime departure time
     * @param dateFrom first day of the schedule (inclusive)
     * @param dateUntil last day of the schedule (inclusive)
     * @param trainId id of the train model
     * @param direction inbound or outbound
     */
    void scheduleRoute(int routeId, String departureTime,
                       String dateFrom, String dateUntil,
                       int trainId, String direction);

    /**
     * Calls to data service to create new tariff with specific price, as long
     *  as the price is above 0.
     * @param price price for the new tariff
     */
    void updateTariff(float price);

    /**
     * Gets from data service list of journeys that depart on specific day and
     *  fall onto specific page with limited number of entries on each page.
     *  Returns map that contains the date, list of journeys, links to previous
     *  and next days and if necessary, links to previous and/or next pages.
     * @param date day for which journeys will be returned
     * @param page page on which journeys must fall
     * @return map that contains list of journeys, date, links to previous and
     *  next days and links to previous/next pages
     */
    Map<String, Object> lookUpJourneys(String date, int page);

    /**
     * Gets from data service list of tickets bought for specific journey that
     *  fall onto specific page with limited number of entries on each page.
     *  Returns map that contains journey, tickets and if necessary, links to
     *  previous and next pages.
     * @param journeyId id indicating for which journey the tickets must be
     * @param page page on which the tickets must fall
     * @return map containing journey, list of tickets bought for the journey,
     *  and links to previous/next pages
     */
    Map<String, Object> listPassengers(int journeyId, int page);

    /**
     * Gets from data service journey with specific id. Returns map containing
     *  this journey.
     * @param journeyId id indicating which journey to return
     * @return map that contains journey with specified id
     */
    Map<String, Object> journeyInfo(int journeyId);

    /**
     * Calls to data service to mark specific journey as cancelled.
     * @param journeyId id indicating which journey to cancel
     */
    void cancelJourney(int journeyId);

    /**
     * Calls to data service in order to set specific journey's delay
     *  to specific amount.
     * @param journeyId id indicating which journey to adjust
     * @param delay number of minutes this journey is now delayed
     */
    void delayJourney(int journeyId, int delay);

    /**
     * Calls to data service in order to update the station with specific id
     *  so that its name would be specific name.
     * @param stationId id indicating which station to update
     * @param newName new station's name
     */
    void renameStation(int stationId, String newName);
}
