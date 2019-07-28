package ru.tsystems.sbb.services.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.services.data.AdminDataService;
import ru.tsystems.sbb.services.data.RouteDataService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminViewServiceImpl implements AdminViewService {

    @Autowired
    private RouteDataService routeDataService;

    @Autowired
    private AdminDataService adminDataService;

    @Override
    public Map<String, Object> getLinesList() {
        List<LineDto> lines = routeDataService.getAllLines();
        Map<String, Object> objects = new HashMap<>();
        objects.put("lines", lines);
        return objects;
    }

    @Override
    public Map<String, Object> addNewStation(String stationName, int lineId, int zone, int order) {
        adminDataService.addNewStation(stationName, lineId, zone, order);
        return getCurrentLineStations(lineId);
    }

    @Override
    public Map<String, Object> getCurrentLineStations(int lineId) {
        List<StationDto> stations = routeDataService
                .getAllLineStations(lineId);
        LineDto line = routeDataService.getLine(lineId);
        Map<String, Object> objects = new HashMap<>();
        objects.put("stations", stations);
        objects.put("line", line);
        return objects;
    }

}
