package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.model.dao.ScheduleDao;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.ScheduledStopDto;
import ru.tsystems.sbb.model.dto.TransferTrainsDto;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.mappers.EntityToDtoMapper;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleDataServiceImpl implements ScheduleDataService {

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private EntityToDtoMapper mapper;

    @Autowired
    private Clock clock;

    private static final String DEFAULT_SEARCH_TYPE = "departure";
    private static final int TRANSFER_TIME = 15;
    private static final int TRAINS_SEARCH_PERIOD = 6;
    private static final int SCHEDULE_SEARCH_PERIOD = 4;
    private static final int FULL_SCHEDULE_SEARCH_PERIOD = 24;

    @Override
    public List<ScheduledStopDto> stationSchedule(final String stationName,
                                                  final LocalDateTime from) {
        Station station = scheduleDao.getStationByName(stationName);
        List<ScheduledStop> scheduledStops = scheduleDao
                .stationSchedule(station, from, SCHEDULE_SEARCH_PERIOD);
        Collections.sort(scheduledStops, new ScheduleComparator());
        return scheduledStops.stream()
                .map(scheduledStop -> mapper.convert(scheduledStop))
                .collect(Collectors.toList());
    }

    @Override
    public List<JourneyDto> directTrainsFromTo(final String stationFrom,
                                               final String stationTo,
                                               final LocalDateTime fromOrBy,
                                               final String searchType) {
        Station origin = scheduleDao.getStationByName(stationFrom);
        Station destination = scheduleDao.getStationByName(stationTo);
        if (!sameLine(origin, destination)) {
            return new ArrayList<>();
        }
        List<Journey> journeys;
        if (searchType.equalsIgnoreCase(DEFAULT_SEARCH_TYPE)) {
            journeys = scheduleDao.
                    trainsFromToByDeparture(origin, destination,
                            fromOrBy, TRAINS_SEARCH_PERIOD);
        } else {
            journeys = scheduleDao.
                    trainsFromToByArrival(origin, destination,
                            fromOrBy, TRAINS_SEARCH_PERIOD);
        }
        return journeys.stream().map(journey -> mapper.convert(journey))
                .collect(Collectors.toList());
    }

    @Override
    public List<TransferTrainsDto> trainsWithTransfer(final String stationFrom,
                                                      final String stationTo,
                                                      final LocalDateTime from,
                                                      final String searchType) {
        Station origin = scheduleDao.getStationByName(stationFrom);
        Station destination = scheduleDao.getStationByName(stationTo);
        List<TransferTrainsDto> connections;
        if (searchType.equalsIgnoreCase(DEFAULT_SEARCH_TYPE)) {
            connections = transferTrainsByDepart(origin, destination, from);
        } else {
            connections = transferTrainsByArrive(origin, destination, from);
        }
        return connections;
    }

    private boolean sameLine(final Station s1, final Station s2) {
        List<Line> firstStationLines = s1.getLines().stream()
                .map(LineStation::getLine).collect(Collectors.toList());
        List<Line> secondStationLines = s2.getLines().stream()
                .map(LineStation::getLine).collect(Collectors.toList());
        return firstStationLines.stream()
                .anyMatch(secondStationLines::contains);
    }

    private List<TransferTrainsDto> transferTrainsByDepart(final Station origin,
                                                           final Station destination,
                                                           final LocalDateTime from) {
        List<TransferTrainsDto> connections = new ArrayList<>();
        List<Station> transferStations = scheduleDao
                .getTransferStations(origin, destination);
        for (Station transfer : transferStations) {
            List<Journey> trainsToTransfer = scheduleDao
                    .trainsFromToByDeparture(origin, transfer,
                            from, TRAINS_SEARCH_PERIOD);
            if (!trainsToTransfer.isEmpty()) {
                for (Journey firstTrain : trainsToTransfer) {
                    LocalDateTime arrivalAtTransfer = firstTrain.getStops()
                            .stream().filter(scheduledStop -> scheduledStop
                                    .getStation().equals(transfer))
                            .findFirst().orElse(new ScheduledStop())
                            .getArrival();
                    Journey secondTrain = scheduleDao
                            .firstTrainAfter(transfer, destination,
                                    arrivalAtTransfer
                                            .plusMinutes(TRANSFER_TIME));
                    if (secondTrain != null) {
                        TransferTrainsDto trains = new TransferTrainsDto();
                        trains.setFirstTrain(mapper.convert(firstTrain));
                        trains.setSecondTrain(mapper.convert(secondTrain));
                        trains.setTransferStation(transfer.getName());
                        connections.add(trains);
                    }
                }
            }
        }
        return connections;
    }

    private List<TransferTrainsDto> transferTrainsByArrive(final Station origin,
                                                           final Station destination,
                                                           final LocalDateTime by) {
        List<TransferTrainsDto> connections = new ArrayList<>();
        List<Station> transferStations = scheduleDao
                .getTransferStations(origin, destination);
        for (Station transfer : transferStations) {
            List<Journey> trainsFromTransfer = scheduleDao
                    .trainsFromToByArrival(transfer, destination,
                            by, TRAINS_SEARCH_PERIOD);
            if (!trainsFromTransfer.isEmpty()) {
                for (Journey secondTrain : trainsFromTransfer) {
                    LocalDateTime departFromTransfer = secondTrain.getStops()
                            .stream().filter(scheduledStop -> scheduledStop
                                    .getStation().equals(transfer))
                            .findFirst().orElse(new ScheduledStop())
                            .getDeparture();
                    Journey firstTrain = scheduleDao
                            .lastTrainBefore(origin, transfer,
                                    departFromTransfer
                                            .minusMinutes(TRANSFER_TIME));
                    if (firstTrain != null) {
                        TransferTrainsDto trains = new TransferTrainsDto();
                        trains.setFirstTrain(mapper.convert(firstTrain));
                        trains.setSecondTrain(mapper.convert(secondTrain));
                        trains.setTransferStation(transfer.getName());
                        connections.add(trains);
                    }
                }
            }
        }
        return connections;
    }

    @Override
    public List<ScheduledStopDto> fullTodaySchedule(int stationId) {
        Station station = scheduleDao.getStationById(stationId);
        List<ScheduledStop> schedule = scheduleDao
                .stationSchedule(station, LocalDate.now(clock).atStartOfDay(),
                        FULL_SCHEDULE_SEARCH_PERIOD);
        schedule.sort(new ScheduleComparator());
        return schedule.stream()
                .map(scheduledStop -> mapper.convert(scheduledStop))
                .collect(Collectors.toList());
    }

    private static class ScheduleComparator implements Comparator<ScheduledStop> {
        @Override
        public int compare(ScheduledStop o1, ScheduledStop o2) {
            if (o1.getArrival() != null && o2.getArrival() != null) {
                return compareArrivals(o1, o2);
            } else if (o1.getArrival() != null) {
                return compareTimes(o1.getArrival().plusMinutes(o1
                        .getJourney().getDelay()), o2.getDeparture()
                        .plusMinutes(o2.getJourney().getDelay()));
            } else if (o2.getArrival() != null) {
                return compareTimes(o1.getDeparture().plusMinutes(o1
                        .getJourney().getDelay()), o2.getArrival()
                        .plusMinutes(o2.getJourney().getDelay()));
            } else {
                return compareTimes(o1.getDeparture().plusMinutes(o1
                        .getJourney().getDelay()), o2.getDeparture()
                        .plusMinutes(o2.getJourney().getDelay()));
            }
        }

        private int compareArrivals(ScheduledStop o1, ScheduledStop o2) {
            if (o1.getArrival().plusMinutes(o1.getJourney().getDelay())
                    .isBefore(o2.getArrival().plusMinutes(o2.getJourney()
                            .getDelay()))) {
                return -1;
            } else if (o1.getArrival().plusMinutes(o1.getJourney().getDelay())
                    .isAfter(o2.getArrival().plusMinutes(o2.getJourney()
                            .getDelay()))) {
                return 1;
            } else if (o1.getDeparture() != null
                    && o2.getDeparture() != null) {
                if (o1.getDeparture().plusMinutes(o1.getJourney().getDelay())
                        .isBefore(o2.getDeparture().plusMinutes(o2.getJourney()
                                .getDelay()))) {
                    return -1;
                } else if (o1.getDeparture().plusMinutes(o1.getJourney().getDelay())
                        .isAfter(o2.getDeparture().plusMinutes(o2.getJourney()
                                .getDelay()))) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }

        private int compareTimes(LocalDateTime first, LocalDateTime second) {
            if (first.isBefore(second)) {
                return -1;
            } else if (first.isAfter(second)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
