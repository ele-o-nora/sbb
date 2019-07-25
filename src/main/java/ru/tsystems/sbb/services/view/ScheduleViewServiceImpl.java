package ru.tsystems.sbb.services.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tsystems.sbb.model.dto.ScheduledStopDto;
import ru.tsystems.sbb.services.data.ScheduleDataService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleViewServiceImpl implements ScheduleViewService {

    @Autowired
    private ScheduleDataService dataService;

    @Override
    public Map<String, Object> getStationSchedule(String stationName) {
        return getStationSchedule(stationName, LocalDateTime.now());
    }

    @Override
    public Map<String, Object> getStationSchedule(String stationName,
                                                  LocalDateTime from) {
        List<ScheduledStopDto> trains = dataService
                .stationSchedule(stationName, from);
        Map<String, Object> objects = new HashMap<>();
        objects.put("trains", trains);
        objects.put("stationName", stationName);
        return objects;
    }

}
