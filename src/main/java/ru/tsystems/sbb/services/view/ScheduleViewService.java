package ru.tsystems.sbb.services.view;

import java.time.LocalDateTime;
import java.util.Map;

public interface ScheduleViewService {
    Map<String, Object> getStationSchedule(String stationName);
    Map<String, Object> getStationSchedule(String stationName,
                                           LocalDateTime from);
}
