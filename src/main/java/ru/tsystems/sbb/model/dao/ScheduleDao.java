package ru.tsystems.sbb.model.dao;

import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleDao {
    List<ScheduledStop> stationSchedule(Station station, LocalDateTime from);
    List<Journey> trainsFromToByDeparture(Station origin, Station destination,
                               LocalDateTime from);
    List<Journey> trainsFromToByArrival(Station origin, Station destination,
                                          LocalDateTime by);
    Station getStationByName(String stationName);
}
