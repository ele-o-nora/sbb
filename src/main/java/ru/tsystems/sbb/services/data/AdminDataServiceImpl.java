package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.model.dao.AdminDao;
import ru.tsystems.sbb.model.dao.RouteDao;
import ru.tsystems.sbb.model.dao.ScheduleDao;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.RouteStation;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.Train;

import java.util.List;

@Service
@Transactional
public class AdminDataServiceImpl implements AdminDataService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private RouteDao routeDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Override
    public void addNewStation(final String stationName, final int lineId,
                              final int zone, final int order) {
        recalculateOrders(lineId, order);
        Station station = new Station();
        station.setName(stationName);
        station.setZone(zone);
        adminDao.add(station);
        LineStation lineStation = new LineStation();
        lineStation.setLine(routeDao.getLineById(lineId));
        lineStation.setStation(station);
        lineStation.setOrder(order);
        adminDao.add(lineStation);
    }

    @Override
    public void addNewRoute(final String routeNumber, final int lineId,
                            final String[] stations, final int[] timesEnRoute,
                            final int[] waitTimes) {
        Line line = routeDao.getLineById(lineId);
        Route route = new Route();
        route.setNumber(routeNumber);
        route.setLine(line);
        adminDao.add(route);
        createRouteStopPattern(route, stations, timesEnRoute, waitTimes);
    }

    @Override
    public void modifyRoute(final int routeId, final String[] stations,
                            final int[] timesEnRoute, final int[] waitTimes) {
        Route route = routeDao.getRouteById(routeId);
        adminDao.cleanRouteStopPattern(route);
        createRouteStopPattern(route, stations, timesEnRoute, waitTimes);
    }

    private void createRouteStopPattern(final Route route,
                                        final String[] stations,
                                        final int[] timesEnRoute,
                                        final int[] waitTimes) {
        Station firstStation = scheduleDao.getStationByName(stations[0]);
        RouteStation firstRouteStation = new RouteStation();
        firstRouteStation.setRoute(route);
        firstRouteStation.setStation(firstStation);
        adminDao.add(firstRouteStation);
        for (int i = 0; i < waitTimes.length; i++) {
            Station station = scheduleDao.getStationByName(stations[i + 1]);
            RouteStation routeStation = new RouteStation();
            routeStation.setRoute(route);
            routeStation.setStation(station);
            routeStation.setTimeEnRoute(timesEnRoute[i]);
            routeStation.setWaitTime(waitTimes[i]);
            adminDao.add(routeStation);
        }
        Station lastStation = scheduleDao
                .getStationByName(stations[stations.length - 1]);
        RouteStation lastRouteStation = new RouteStation();
        lastRouteStation.setRoute(route);
        lastRouteStation.setStation(lastStation);
        lastRouteStation.setTimeEnRoute(timesEnRoute[timesEnRoute.length - 1]);
        adminDao.add(lastRouteStation);
    }

    @Override
    public void addNewTrainModel(String model, int seats) {
        Train train = new Train();
        train.setModel(model);
        train.setSeats(seats);
        adminDao.add(train);
    }

    private void recalculateOrders(final int lineId, final int firstOrder) {
        List<LineStation> lineStations = routeDao
                .getLineStations(routeDao.getLineById(lineId), firstOrder);
        for (LineStation ls : lineStations) {
            ls.setOrder(ls.getOrder() + 1);
            adminDao.update(ls);
        }
    }

    @Override
    public List<Train> getAllTrainModels() {
        return adminDao.getAllTrainModels();
    }
}
