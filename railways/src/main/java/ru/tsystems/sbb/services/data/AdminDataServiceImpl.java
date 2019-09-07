package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.model.dao.AdminDao;
import ru.tsystems.sbb.model.dao.PassengerDao;
import ru.tsystems.sbb.model.dao.RouteDao;
import ru.tsystems.sbb.model.dao.ScheduleDao;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.dto.TrainDto;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.RouteStation;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.Tariff;
import ru.tsystems.sbb.model.entities.Train;
import ru.tsystems.sbb.model.mappers.EntityToDtoMapper;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminDataServiceImpl implements AdminDataService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private RouteDao routeDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private PassengerDao passengerDao;

    @Autowired
    private EntityToDtoMapper mapper;

    @Autowired
    private Clock clock;

    @Autowired
    private JmsTemplate jmsTemplate;

    private static final int SEARCH_RESULTS_STEP = 10;
    private static final double KM_TO_MILES = 0.621371;

    @Override
    public void addNewStation(final String stationName, final int lineId,
                              final int order, final int x, final int y) {
        recalculateOrders(lineId, order);
        Station station = new Station();
        station.setName(stationName);
        station.setX(x);
        station.setY(y);
        adminDao.add(station);
        Line line = routeDao.getLineById(lineId);
        LineStation lineStation = new LineStation();
        lineStation.setLine(line);
        lineStation.setStation(station);
        lineStation.setOrder(order);
        adminDao.add(lineStation);
        jmsTemplate.send(session -> session.createTextMessage("New station"));
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
    public List<TrainDto> getAllTrainModels() {
        return adminDao.getAllTrainModels().stream()
                .map(train -> mapper.convert(train))
                .collect(Collectors.toList());
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
            createStops(journey, departDateTime, stations, train.getSpeed(),
                    outbound);
        }
        jmsTemplate.send(session -> session.createTextMessage("New schedule"));
    }

    @Override
    public void updateTariff(final float price) {
        Tariff tariff = Tariff.builder().momentFrom(LocalDateTime.now(clock))
                .pricePerTenLeagues(price).build();
        adminDao.add(tariff);
    }

    @Override
    public float currentTariff() {
        return passengerDao.getCurrentTariff();
    }

    private int calcTimeEnRoute(final double distance, final int speed) {
        double hours = distance / (double) speed;
        return (int) Math.ceil(hours * 60);
    }

    @Override
    public List<JourneyDto> getJourneys(final LocalDateTime start,
                                        final int page) {
        return passengerDao.getJourneys(start, page, SEARCH_RESULTS_STEP)
                .stream().map(journey -> mapper.convert(journey))
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDto> getTickets(final int journeyId, final int page) {
        Journey journey = passengerDao.getJourneyById(journeyId);
        return passengerDao.getTickets(journey, page, SEARCH_RESULTS_STEP)
                .stream().map(ticket -> mapper.convert(ticket))
                .collect(Collectors.toList());
    }

    @Override
    public int maxJourneyPages(final LocalDateTime start) {
        int journeysCount = passengerDao.journeysCount(start);
        return (journeysCount + SEARCH_RESULTS_STEP - 1) / SEARCH_RESULTS_STEP;
    }

    @Override
    public int maxPassengerPages(final int journeyId) {
        Journey journey = passengerDao.getJourneyById(journeyId);
        int ticketsCount = passengerDao.currentTickets(journey,
                journey.getStops().get(0), journey.getStops()
                        .get(journey.getStops().size() - 1));
        return (ticketsCount + SEARCH_RESULTS_STEP - 1) / SEARCH_RESULTS_STEP;
    }

    @Override
    public JourneyDto getJourneyById(final int journeyId) {
        return mapper.convert(passengerDao.getJourneyById(journeyId));
    }

    @Override
    public void cancelJourney(final int journeyId) {
        Journey journey = passengerDao.getJourneyById(journeyId);
        journey.setCancelled(true);
        adminDao.update(journey);
        jmsTemplate.send(session -> session.createTextMessage("Cancellation"));
    }

    @Override
    public void delayJourney(final int journeyId, final int delay) {
        Journey journey = passengerDao.getJourneyById(journeyId);
        journey.setDelay(delay);
        adminDao.update(journey);
        for (ScheduledStop stop : journey.getStops()) {
            if (stop.getDeparture() != null) {
                stop.setEtd(stop.getDeparture().plusMinutes(delay));
            }
            if (stop.getArrival() != null) {
                stop.setEta(stop.getArrival().plusMinutes(delay));
            }
            adminDao.update(stop);
        }
        jmsTemplate.send(session -> session.createTextMessage("Delay"));
    }

    @Override
    public void renameStation(final int stationId, final String newName) {
        Station station = scheduleDao.getStationById(stationId);
        station.setName(newName);
        adminDao.update(station);
    }

    private double calcDistanceBetweenPoints(final int x1, final int y1,
                                             final int x2, final int y2) {
        double xDiff = Math.abs(x1 - x2);
        double yDiff = Math.abs(y1 - y2);
        return Math.ceil(Math.hypot(xDiff, yDiff));
    }

    private double distanceBetweenStations(final Station first,
                                           final Station second,
                                           final Line line) {
        double distance = 0;
        int firstOrder = routeDao.getStationOrder(line, first);
        int secondOrder = routeDao.getStationOrder(line, second);
        List<Station> stationsFromTo = routeDao
                .getStations(firstOrder, secondOrder, line);
        for (int i = 1; i < stationsFromTo.size(); i++) {
            distance += calcDistanceBetweenPoints(
                    stationsFromTo.get(i - 1).getX(),
                    stationsFromTo.get(i - 1).getY(),
                    stationsFromTo.get(i).getX(),
                    stationsFromTo.get(i).getY());
        }
        return distance * KM_TO_MILES;
    }

    private void createStops(final Journey journey,
                             final LocalDateTime departure,
                             final List<RouteStation> stations,
                             final int trainSpeed,
                             final boolean outbound) {
        LocalDateTime curMoment = departure;
        int start = outbound ? 1 : stations.size() - 2;
        int end = outbound ? stations.size() : -1;
        int step = outbound ? 1 : -1;
        ScheduledStop departureStop = new ScheduledStop();
        departureStop.setJourney(journey);
        departureStop.setStation(outbound ? stations.get(0).getStation()
                : stations.get(stations.size() - 1).getStation());
        departureStop.setDeparture(curMoment);
        adminDao.add(departureStop);
        for (int i = start; i != end; i += step) {
            Station current = stations.get(i).getStation();
            Station previous = stations.get(i - step).getStation();
            double distanceFromPrevStop;
            if (outbound) {
                distanceFromPrevStop = distanceBetweenStations(previous,
                        current, journey.getRoute().getLine());
            } else {
                distanceFromPrevStop = distanceBetweenStations(current,
                        previous, journey.getRoute().getLine());
            }
            int timeEnRoute = calcTimeEnRoute(distanceFromPrevStop,
                    trainSpeed);
            curMoment = curMoment.plusMinutes(timeEnRoute);
            ScheduledStop curStop = new ScheduledStop();
            curStop.setJourney(journey);
            curStop.setStation(stations.get(i).getStation());
            curStop.setArrival(curMoment);
            if (Math.abs(i - end) > 1) {
                curMoment = curMoment.plusMinutes(stations.get(i).getWaitTime());
                curStop.setDeparture(curMoment);
            }
            adminDao.add(curStop);
        }
    }

}
