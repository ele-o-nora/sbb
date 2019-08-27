package ru.tsystems.sbb.services.data;

import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.dto.ScheduledStopDto;
import ru.tsystems.sbb.model.dto.TransferTrainsDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interaction with DAO regarding existing scheduled journeys
 *  and conversion of Entities into DTOs.
 */
public interface ScheduleDataService {

    /**
     * Gets from DAO list of scheduled stops that contain information about
     *  trains that pass station with specific name during specific period.
     *  Return them in the form of corresponding DTO.
     * @param stationName name of the station for which search is conducted
     * @param from moment in time that sets the beginning of the search period
     * @return list of ScheduledStopDto for specified station which arrival or
     *  departure falls into particular period of time
     */
    List<ScheduledStopDto> stationSchedule(String stationName,
                                           LocalDateTime from);

    /**
     * Gets from DAO list of scheduled journeys that stop at one specific
     *  station first and another specific station second. Search is conducted
     *  either by arrival, where resulting journeys must have arrived at the
     *  second station by specific moment in time, or by departure, where
     *  the journeys must depart from the first station after specific moment
     *  in time. Resulting list is returned in the form of corresponding DTO.
     * @param stationFrom station where train stops earlier in time
     * @param stationTo station where train stops later in time
     * @param fromOrBy moment setting either start or end of the search period
     * @param searchType indicates whether search is by departure or arrival
     * @return list of JourneyDto that pass through one of the specified
     *  stations first and another second during particular period of time
     */
    List<JourneyDto> directTrainsFromTo(String stationFrom, String stationTo,
                                  LocalDateTime fromOrBy, String searchType);

    /**
     * Gets pairs of journeys, where the first journey connects specific station
     *  to some transfer station and the second connects that transfer station
     *  to another specific station. Search can be conducted either by departure
     *  or by arrival. Resulting list consists of pairs of corresponding DTOs.
     * @param stationFrom station from which train must get to the transfer
     * @param stationTo station to which train must get from the transfer
     * @param fromOrBy moment setting either start or end of the search period
     * @param searchType indicates whether search is by departure or arrival
     * @return list of TransferTrainsDto where first journey runs from specified
     *  station to some transfer station and second connects transfer station to
     *  another specified station during particular period of time
     */
    List<TransferTrainsDto> trainsWithTransfer(String stationFrom,
                                               String stationTo,
                                               LocalDateTime fromOrBy,
                                               String searchType);

    /**
     * Gets from DAO list of scheduled stops for station with specific name that
     *  contain information about trains stopping at this station during current
     *  day. Returns the list of corresponding DTOs.
     * @param stationId id of the station for which the schedule is returned
     * @return list of ScheduledStopDto for specified station where arrival or
     *  departure fall on the current day
     */
    List<ScheduledStopDto> currentSchedule(int stationId);
}
