package ru.tsystems.sbb.model.dao;

import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleDao {
    List<ScheduledStop> stationSchedule(Station station, LocalDateTime from);
    List<Journey> trainsFromTo(Station origin, Station destination,
                               LocalDateTime from);
    Station getStationByName(String stationName);
}
