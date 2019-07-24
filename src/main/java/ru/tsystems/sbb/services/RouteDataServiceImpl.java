package ru.tsystems.sbb.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.model.dao.RouteDao;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.RouteDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RouteDataServiceImpl implements RouteDataService {

    @Autowired
    private RouteDao routeDao;

    @Autowired
    private ModelMapper mapper;

    @Override
    public List<StationDto> getAllLineStations(int lineId) {
        Line line = routeDao.getLineById(lineId);
        List<Station> stations = routeDao.allLineStations(line);
        List<StationDto> stationsDto = stations.stream()
                .map(station -> mapper.map(station, StationDto.class))
                .collect(Collectors.toList());
        return stationsDto;
    }

    @Override
    public List<StationDto> getAllRouteStations(int routeId) {
        Route route = routeDao.getRouteById(routeId);
        List<Station> stations = routeDao.allRouteStations(route);
        List<StationDto> stationsDto = stations.stream()
                .map(station -> mapper.map(station, StationDto.class))
                .collect(Collectors.toList());
        return stationsDto;
    }

    @Override
    public List<RouteDto> getAllRoutes(int lineId) {
        Line line = routeDao.getLineById(lineId);
        List<Route> routes = routeDao.allLineRoutes(line);
        List<RouteDto> routesDto = routes.stream()
                .map(route -> mapper.map(route, RouteDto.class))
                .collect(Collectors.toList());
        return routesDto;
    }

    @Override
    public List<LineDto> getAllLines() {
        List<Line> lines = routeDao.allLines();
        List<LineDto> linesDto = lines.stream()
                .map(line -> mapper.map(line, LineDto.class))
                .collect(Collectors.toList());
        return linesDto;
    }
}
