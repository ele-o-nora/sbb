package ru.tsystems.sbb.services.data;

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
import ru.tsystems.sbb.model.mappers.EntityToDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RouteDataServiceImpl implements RouteDataService {

    @Autowired
    private RouteDao routeDao;

    @Autowired
    private EntityToDtoMapper mapper;

    @Override
    public List<StationDto> getAllLineStations(final int lineId) {
        Line line = routeDao.getLineById(lineId);
        List<Station> stations = routeDao.allLineStations(line);
        return stations.stream()
                .map(station -> mapper.convert(station))
                .collect(Collectors.toList());
    }

    @Override
    public List<RouteDto> getAllRoutes(final int lineId) {
        Line line = routeDao.getLineById(lineId);
        List<Route> routes = routeDao.allLineRoutes(line);
        return routes.stream()
                .map(route -> mapper.convert(route))
                .collect(Collectors.toList());
    }

    @Override
    public List<LineDto> getAllLines() {
        List<Line> lines = routeDao.allLines();
        return lines.stream()
                .map(line -> mapper.convert(line))
                .collect(Collectors.toList());
    }

    @Override
    public LineDto getLine(int lineId) {
        return mapper.convert(routeDao.getLineById(lineId));
    }

    @Override
    public List<StationDto> allStations() {
        return routeDao.getAllStations()
                .stream().map(station -> mapper.convert(station))
                .collect(Collectors.toList());
    }

    @Override
    public RouteDto getRoute(int routeId) {
        return mapper.convert(routeDao.getRouteById(routeId));
    }
}
