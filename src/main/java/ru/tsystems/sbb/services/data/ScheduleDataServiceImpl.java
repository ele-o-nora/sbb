package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.model.dao.ScheduleDao;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.ScheduledStopDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.mappers.EntityToDtoMapper;

import java.time.LocalDateTime;
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
    public List<JourneyDto> trainsFromTo(final String stationFrom,
                                         final String stationTo,
                                         final LocalDateTime dateTime,
                                         final String searchType) {
        Station origin = scheduleDao.getStationByName(stationFrom);
        Station destination = scheduleDao.getStationByName(stationTo);
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
    public List<StationDto> allStations() {
        return scheduleDao.getAllStations()
                .stream().map(station -> mapper.convert(station))
                .collect(Collectors.toList());
    }

}
