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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleDataServiceImpl implements ScheduleDataService {

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private EntityToDtoMapper mapper;

    private static final String DEFAULT_SEARCH_TYPE = "departure";
    private static final int TRANSFER_TIME = 10;

    @Override
    public List<ScheduledStopDto> stationSchedule(final String stationName,
                                                  final LocalDateTime from) {
        Station station = scheduleDao.getStationByName(stationName);
        List<ScheduledStop> scheduledStops = scheduleDao
                .stationSchedule(station, from);
        return scheduledStops.stream()
                .map(scheduledStop -> mapper.convert(scheduledStop))
                .collect(Collectors.toList());
    }

    @Override
    public List<JourneyDto> directTrainsFromTo(final String stationFrom,
                                         final String stationTo,
                                         final LocalDateTime dateTime,
                                         final String searchType) {
        Station origin = scheduleDao.getStationByName(stationFrom);
        Station destination = scheduleDao.getStationByName(stationTo);
        if (!sameLine(origin, destination)) {
            return new ArrayList<>();
        }
        List<Journey> journeys;
        if (searchType.equalsIgnoreCase(DEFAULT_SEARCH_TYPE)) {
            journeys = scheduleDao.
                    trainsFromToByDeparture(origin, destination, dateTime);
        } else {
            journeys = scheduleDao.
                    trainsFromToByArrival(origin, destination, dateTime);
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
        List<Station> transferStations = scheduleDao.getTransferStations();
        for (Station transfer : transferStations) {
            List<Journey> trainsToTransfer = scheduleDao
                    .trainsFromToByDeparture(origin, transfer, from);
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
        List<Station> transferStations = scheduleDao.getTransferStations();
        for (Station transfer : transferStations) {
            List<Journey> trainsFromTransfer = scheduleDao
                    .trainsFromToByArrival(transfer, destination, by);
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

}
