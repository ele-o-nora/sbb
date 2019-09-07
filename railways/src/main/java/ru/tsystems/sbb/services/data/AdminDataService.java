package ru.tsystems.sbb.services.data;

import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.dto.TrainDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Interaction with DAO regarding updating railway network, schedule,
 *  and pricing and information necessary for the network administration.
 */
public interface AdminDataService {

    /**
     * Creates new station with specific name that has specific position
     *  on specific line and lies at specific distances from the stations
     *  preceding and succeeding it.
     * @param stationName name of the station to be created
     * @param lineId id indicating which line the new station belongs to
     * @param order position of the new station on the line
     * @param x station's x coordinate
     * @param y station's y coordinate
     */
    void addNewStation(String stationName, int lineId, int order, int x, int y);

    /**
     * Creates new train model with specific name, quantity of seats and speed.
     * @param model name of the train model to be created
     * @param seats number of seats that the new train model has
     * @param speed speed of the new train model
     */
    void addNewTrainModel(String model, int seats, int speed);

    /**
     * Creates new route that has specific number, is linked to specific line,
     *  stops at specific stations on this line and spends on those stations
     *  specific number of minutes.
     * @param routeNumber number of the route to be created
     * @param lineId id indicating which line the route belongs to
     * @param stations array of stations the new route stops at
     * @param waitTimes array of numbers of minutes route spends on each station
     */
    void addNewRoute(String routeNumber, int lineId, String[] stations,
                     int[] waitTimes);

    /**
     * Modifies route that is identified by specific id.
     * @param routeId id that indicates which route is to be updated
     * @param stations list of stations the route will stop at from now on
     * @param waitTimes array of number of minutes route spends on each station
     */
    void modifyRoute(int routeId, String[] stations,
                     int[] waitTimes);

    /**
     * Gets from DAO the list of all existing train models and returns
     *  the list of corresponding DTOs.
     * @return complete list of TrainDto
     */
    List<TrainDto> getAllTrainModels();

    /**
     * Creates one or more journeys that travel specific route and have specific
     *  departure time and specific train model. Journeys are created one for
     *  each day for the period of time specified by the first and the last day,
     *  both inclusive. Journeys can be scheduled outbound (departing from
     *  the station with the lowest ordinal number on the line) or
     *  inbound (departing from the station with the highest ordinal number).
     * @param routeId id indication for which route the schedule is created
     * @param departure departure time
     * @param dayFrom first day of new schedule (inclusive)
     * @param dayUntil last day of new schedule (inclusive)
     * @param trainId id indication which train model will serve the journeys
     * @param outbound indicates whether the journeys are outbound or inbound
     */
    void scheduleJourneys(int routeId, LocalTime departure,
                          LocalDate dayFrom, LocalDate dayUntil,
                          int trainId, boolean outbound);

    /**
     * Creates new tariff with specific price and current timestamp.
     * @param price price of the tariff to be created
     */
    void updateTariff(float price);

    /**
     * Gets from DAO and returns tariff with the latest timestamp.
     * @return float value of the tariff with the latest timestamp
     */
    float currentTariff();

    /**
     * Gets from DAO list of the journeys for one calendar day starting at
     *  specific moment in time, those that fall onto specific page with
     *  limited number of entries on each page, and returns corresponding DTOs.
     * @param start moment in time from which search period begins
     * @param page page on which journeys must fall
     * @return list of JourneyDto for one calendar day starting with specified
     *  moment in time that fall onto specified page
     */
    List<JourneyDto> getJourneys(LocalDateTime start, int page);

    /**
     * Gets from DAO list of the tickets for the journey with specific id,
     *  those that fall onto specific page with limited number of entries
     *  on each page, and returns the list of corresponding DTOs.
     * @param journeyId id indicating which journey the tickets are for
     * @param page page on which the tickets must fall
     * @return list of TicketDto for the specified journey that fall onto
     *  specified page
     */
    List<TicketDto> getTickets(int journeyId, int page);

    /**
     * Gets from DAO total quantity of the journeys for one calendar day
     *  starting with specific moment in time and calculates total number
     *  of pages these journeys can fill.
     * @param start moment in time from which the search period begins
     * @return integer value of the number of the pages that journeys fill
     */
    int maxJourneyPages(LocalDateTime start);

    /**
     * Gets from DAO total quantity of the tickets bought for specific journey
     *  and calculates total number of the pages these tickets can fill.
     * @param journeyId id indicating which journey the tickets are for
     * @return integer value of the number of the pages that tickets fill
     */
    int maxPassengerPages(int journeyId);

    /**
     * Gets from DAO journey with specific id and returns corresponding DTO.
     * @param journeyId id indicating which journey will be returned
     * @return JourneyDto with specified id
     */
    JourneyDto getJourneyById(int journeyId);

    /**
     * Marks specific journey as cancelled.
     * @param journeyId id indicating which journey to cancel
     */
    void cancelJourney(int journeyId);

    /**
     * Sets specific journey's delay to specific amount.
     * @param journeyId id indicating which journey to update
     * @param delay number of minutes journey is now delayed
     */
    void delayJourney(int journeyId, int delay);

    /**
     * Updates station with specific id so that it would have specific name.
     * @param stationId id indicating which station to update
     * @param newName new station's name
     */
    void renameStation(int stationId, String newName);
}
