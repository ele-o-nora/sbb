package ru.tsystems.sbb.services.view;

import java.util.Map;

public interface ScheduleViewService {
    Map<String, Object> getStationSchedule(String stationName, String from);
    Map<String, Object> getStationsList();
    Map<String, Object> getTrainsFromTo(String origin, String destination,
                                        String dateTime, String searchType);
}
