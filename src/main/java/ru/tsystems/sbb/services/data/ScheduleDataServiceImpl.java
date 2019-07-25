package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.model.dao.ScheduleDao;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.ScheduledStopDto;
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

    @Override
    public List<ScheduledStopDto> stationSchedule(String stationName,
                                                  LocalDateTime from) {
        Station station = scheduleDao.getStationByName(stationName);
        List<ScheduledStop> scheduledStops = scheduleDao
                .stationSchedule(station, from);
        return scheduledStops.stream()
                .map(scheduledStop -> mapper.convert(scheduledStop))
                .collect(Collectors.toList());
    }

    @Override
    public List<JourneyDto> trainsFromTo(String stationFrom, String stationTo,
                                         LocalDateTime from) {
        Station origin = scheduleDao.getStationByName(stationFrom);
        Station destination = scheduleDao.getStationByName(stationTo);
        List<Journey> journeys = scheduleDao.
                trainsFromTo(origin, destination, from);
        return journeys.stream().map(journey -> mapper.convert(journey))
                .collect(Collectors.toList());
    }

}
