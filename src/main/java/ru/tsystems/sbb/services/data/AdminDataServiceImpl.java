package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.model.dao.AdminDao;
import ru.tsystems.sbb.model.dao.RouteDao;
import ru.tsystems.sbb.model.dao.ScheduleDao;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.RouteStation;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.StationsDistance;
import ru.tsystems.sbb.model.entities.Train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
                              final int order, final int distBefore,
                              final int distAfter) {
        if (distBefore > 0 && distAfter > 0) {
            cleanOldDistance(lineId, order);
        }
        recalculateOrders(lineId, order);
        Station station = new Station();
        station.setName(stationName);
        adminDao.add(station);
        Line line = routeDao.getLineById(lineId);
        if (distAfter > 0) {
            Station nextStation = adminDao.getStation(line, order + 1);
            connectStations(station, nextStation, distAfter);
        }
        if (distBefore > 0) {
            Station prevStation = adminDao.getStation(line, order - 1);
            connectStations(station, prevStation, distBefore);
        }
        LineStation lineStation = new LineStation();
        lineStation.setLine(line);
        lineStation.setStation(station);
        lineStation.setOrder(order);
        adminDao.add(lineStation);
    }

    @Override
    public void addNewRoute(final String routeNumber, final int lineId,
                            final String[] stations, final int[] waitTimes) {
        Line line = routeDao.getLineById(lineId);
        Route route = new Route();
        route.setNumber(routeNumber);
        route.setLine(line);
        adminDao.add(route);
        createRouteStopPattern(route, stations, waitTimes);
    }

    @Override
    public void modifyRoute(final int routeId, final String[] stations,
                            final int[] waitTimes) {
        Route route = routeDao.getRouteById(routeId);
        adminDao.cleanRouteStopPattern(route);
        createRouteStopPattern(route, stations, waitTimes);
    }

    private void createRouteStopPattern(final Route route,
                                        final String[] stations,
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
            routeStation.setWaitTime(waitTimes[i]);
            adminDao.add(routeStation);
        }
        Station lastStation = scheduleDao
                .getStationByName(stations[stations.length - 1]);
        RouteStation lastRouteStation = new RouteStation();
        lastRouteStation.setRoute(route);
        lastRouteStation.setStation(lastStation);
        adminDao.add(lastRouteStation);
    }

    @Override
    public void addNewTrainModel(final String model,
                                 final int seats, final int speed) {
        Train train = new Train();
        train.setModel(model);
        train.setSeats(seats);
        train.setSpeed(speed);
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

    private void cleanOldDistance(final int lineId, final int newStationOrder) {
        Line line = routeDao.getLineById(lineId);
        Station stationBefore = adminDao.getStation(line, newStationOrder - 1);
        Station stationAfter = adminDao.getStation(line, newStationOrder);
        adminDao.deleteDistance(stationBefore, stationAfter);
    }

    private void connectStations(final Station s1, final Station s2,
                                 final int distance) {
        StationsDistance sd1 = new StationsDistance();
        sd1.setFirstStation(s1);
        sd1.setSecondStation(s2);
        sd1.setDistance(distance);
        StationsDistance sd2 = new StationsDistance();
        sd2.setFirstStation(s2);
        sd2.setSecondStation(s1);
        sd2.setDistance(distance);
        adminDao.add(sd1);
        adminDao.add(sd2);
    }

    @Override
    public void scheduleJourneys(final int routeId,
                                 final LocalTime departure,
                                 final LocalDate dayFrom,
                                 final LocalDate dayUntil,
                                 final int trainId,
                                 final boolean outbound) {
        Route route = routeDao.getRouteById(routeId);
        Train train = adminDao.getTrainById(trainId);
        List<RouteStation> stations = route.getStations();
        for (LocalDate day = dayFrom;
             day.isBefore(dayUntil) || day.isEqual(dayUntil);
             day = day.plusDays(1)) {
            LocalDateTime departDateTime = LocalDateTime.of(day, departure);
            Journey journey = new Journey();
            journey.setRoute(route);
            journey.setTrainType(train);
            if (outbound) {
                journey.setDestination(stations
                        .get(stations.size() - 1).getStation());
            } else {
                journey.setDestination(stations.get(0).getStation());
            }
            adminDao.add(journey);
            if (outbound) {
                scheduleOutbound(journey, departDateTime, stations,
                        train.getSpeed());
            } else {
                scheduleInbound(journey, departDateTime, stations,
                        train.getSpeed());
            }
        }
    }

    private void scheduleInbound(final Journey journey,
                                 final LocalDateTime departure,
                                 final List<RouteStation> stations,
                                 final int trainSpeed) {
        LocalDateTime curMoment = departure;
        ScheduledStop departureStop = new ScheduledStop();
        departureStop.setJourney(journey);
        departureStop.setStation(stations.get(stations.size() - 1)
                .getStation());
        departureStop.setDeparture(curMoment);
        adminDao.add(departureStop);
        for (int i = stations.size() - 2; i >= 0; i--) {
            int distanceFromPrevStop = adminDao
                    .inboundDistance(stations.get(i + 1).getStation(),
                            stations.get(i).getStation(),
                            journey.getRoute().getLine());
            int timeEnRoute = calcTimeEnRoute(distanceFromPrevStop,
                    trainSpeed);
            curMoment = curMoment.plusMinutes(timeEnRoute);
            ScheduledStop curStop = new ScheduledStop();
            curStop.setJourney(journey);
            curStop.setStation(stations.get(i).getStation());
            curStop.setArrival(curMoment);
            if (i > 0) {
                curMoment = curMoment.plusMinutes(stations.get(i)
                        .getWaitTime());
                curStop.setDeparture(curMoment);
            }
            adminDao.add(curStop);
        }
    }

    private void scheduleOutbound(final Journey journey,
                                  final LocalDateTime departure,
                                  final List<RouteStation> stations,
                                  final int trainSpeed) {
        LocalDateTime curMoment = departure;
        ScheduledStop departureStop = new ScheduledStop();
        departureStop.setJourney(journey);
        departureStop.setStation(stations.get(0).getStation());
        departureStop.setDeparture(curMoment);
        adminDao.add(departureStop);
        for (int i = 1; i < stations.size(); i++) {
            int distanceFromPrevStop = adminDao
                    .outboundDistance(stations.get(i - 1).getStation(),
                            stations.get(i).getStation(),
                            journey.getRoute().getLine());
            int timeEnRoute = calcTimeEnRoute(distanceFromPrevStop,
                    trainSpeed);
            curMoment = curMoment.plusMinutes(timeEnRoute);
            ScheduledStop curStop = new ScheduledStop();
            curStop.setJourney(journey);
            curStop.setStation(stations.get(i).getStation());
            curStop.setArrival(curMoment);
            if (i < stations.size() - 1) {
                curMoment = curMoment.plusMinutes(stations.get(i).getWaitTime());
                curStop.setDeparture(curMoment);
            }
            adminDao.add(curStop);
        }
    }

    private int calcTimeEnRoute(int distance, int speed) {
        float hours = distance / (float) speed;
        return (int) Math.ceil(hours * 60);
    }

}
