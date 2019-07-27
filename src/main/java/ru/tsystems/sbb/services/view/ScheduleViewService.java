package ru.tsystems.sbb.services.view;

import java.time.LocalDateTime;
import java.util.Map;

public interface ScheduleViewService {
    Map<String, Object> getStationSchedule(String stationName);
    Map<String, Object> getStationSchedule(String stationName,
                                           LocalDateTime from);
    Map<String, Object> getStationsList();
    Map<String, Object> getTrainsFromTo(String origin, String destination,
                                        String dateTime, String searchType);
}
