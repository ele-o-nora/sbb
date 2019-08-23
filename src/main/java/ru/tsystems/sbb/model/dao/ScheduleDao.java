package ru.tsystems.sbb.model.dao;

import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Direct interaction with database regarding existing scheduled journeys.
 */
public interface ScheduleDao {
    /**
     * Gets from database the list of scheduled stops that contain information
     *  about trains passing specific station during specific time period.
     * @param station station for which the schedule will be returned
     * @param from moment in time that sets the beginning of the search period
     * @param searchPeriod number of hours the search period lasts
     * @return list of ScheduledStop for specified station which departure or
     *  arrival fall into the period of time starting with specified moment
     * @see ScheduledStop
     */
    List<ScheduledStop> stationSchedule(Station station, LocalDateTime from,
                                        int searchPeriod);

    /**
     * Gets from database the list of scheduled journeys that depart from origin
     *  during specific time period and pass through destination later on.
     * @param origin station which train departs from during specified time
     * @param destination station where train must stop after origin
     * @param from moment in time that sets the beginning of the search period
     * @param searchPeriod number of hours the search period lasts
     * @return list of Journey that depart from origin during certain period
     *  of time after specified moment and later stop at destination
     */
    List<Journey> trainsFromToByDeparture(Station origin, Station destination,
                               LocalDateTime from, int searchPeriod);

    /**
     * Gets from database the list of scheduled journeys that arrive at
     *  destination during specific time period, having passed
     *  through origin before.
     * @param origin station where train must stop before destination
     * @param destination station where train arrives during specified time
     * @param by moment in time that sets the end of the search period
     * @param searchPeriod number of hours the search period lasts
     * @return list of Journey that arrive at destination during certain period
     *  before specified moment having stopped at origin before that
     */
    List<Journey> trainsFromToByArrival(Station origin, Station destination,
                                        LocalDateTime by, int searchPeriod);

    /**
     * Gets from database the station that has specific name.
     * @param stationName name of the station to get
     * @return Station with specified name
     */
    Station getStationByName(String stationName);

    /**
     * Gets from database the list of stations that are reachable from both
     *  origin and destination via existing routes.
     * @param origin first of the stations to be connected via transfer station
     * @param destination second station to be connected
     * @return list of Station that that connect origin and destination
     */
    List<Station> getTransferStations(Station origin, Station destination);

    /**
     * Gets from database the latest scheduled journey that arrives at
     *  destination by specific moment in time having passed origin before that.
     * @param origin station where train must stop before destination
     * @param destination station where train arrives by specified time
     * @param by moment in time by which train must have arrived at destination
     * @return latest Journey that arrives at destination before specified
     *  moment having stopped at origin before that
     */
    Journey lastTrainBefore(Station origin, Station destination,
                            LocalDateTime by);

    /**
     * Gets from database the first scheduled journey that departs from origin
     *  after specific moment in time and stops at destination afterwards.
     * @param origin station which train departs from after specified time
     * @param destination stations where train stops after having left origin
     * @param from moment in time after which train departs from origin
     * @return first Journey that departs from origin after specified moment
     *  and stops at destination later on
     */
    Journey firstTrainAfter(Station origin, Station destination,
                            LocalDateTime from);
}
