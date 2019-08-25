package ru.tsystems.sbb.services.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import ru.tsystems.sbb.model.dao.AdminDao;
import ru.tsystems.sbb.model.dao.PassengerDao;
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
import ru.tsystems.sbb.model.mappers.EntityToDtoMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AdminDataServiceTest {

    @InjectMocks
    private AdminDataServiceImpl adminDataService;

    @Mock
    private AdminDao mockAdminDao;

    @Mock
    private ScheduleDao mockScheduleDao;

    @Mock
    private RouteDao mockRouteDao;

    @Mock
    private PassengerDao mockPassengerDao;

    @Mock
    private EntityToDtoMapper mockMapper;

    @Mock
    private Clock mockClock;

    @Mock
    private JmsTemplate mockJmsTemplate;

    private static final LocalDate LOCAL_DATE = LocalDate.of(2020, 2, 2);

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
        Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId
                .systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.lenient().when(mockClock.instant())
                .thenReturn(fixedClock.instant());
        Mockito.lenient().when(mockClock.getZone())
                .thenReturn(fixedClock.getZone());
    }

    @Test
    void addNewFirstStationTest() {
        String stationName = "firstStation";
        int lineId = 0;
        int order = 1;
        int distBefore = 0;
        int distAfter = 10;
        Line line = new Line();
        LineStation lineStation = new LineStation();
        List<LineStation> lineStations = Arrays
                .asList(lineStation, lineStation);
        when(mockRouteDao.getLineById(anyInt())).thenReturn(line);
        when(mockRouteDao.getLineStations(any(Line.class), anyInt()))
                .thenReturn(lineStations);
        when(mockRouteDao.getStation(any(Line.class), anyInt()))
                .thenReturn(new Station());

        adminDataService.addNewStation(stationName, lineId, order,
                distBefore, distAfter);

        verify(mockRouteDao, times(2)).getLineById(eq(0));
        verify(mockRouteDao, times(1)).getLineStations(same(line), eq(1));
        verify(mockAdminDao, times(2)).update(lineStation);
        verify(mockAdminDao, times(1)).add(any(Station.class));
        verify(mockRouteDao, times(1)).getStation(same(line), eq(2));
        verify(mockAdminDao, times(2)).add(any(StationsDistance.class));
        verify(mockAdminDao, times(1)).add(any(LineStation.class));
        verifyNoMoreInteractions(mockAdminDao);
        verifyNoMoreInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockPassengerDao);
        verifyZeroInteractions(mockMapper);
    }

    @Test
    void addNewLastStationTest() {
        String stationName = "lastStation";
        int lineId = 0;
        int order = 10;
        int distBefore = 10;
        int distAfter = 0;
        Line line = new Line();
        when(mockRouteDao.getLineById(anyInt())).thenReturn(line);
        when(mockRouteDao.getLineStations(any(Line.class), anyInt()))
                .thenReturn(new ArrayList<>());
        when(mockRouteDao.getStation(any(Line.class), anyInt()))
                .thenReturn(new Station());

        adminDataService.addNewStation(stationName, lineId, order,
                distBefore, distAfter);

        verify(mockRouteDao, times(2)).getLineById(eq(0));
        verify(mockRouteDao, times(1)).getLineStations(same(line), eq(10));
        verify(mockAdminDao, times(1)).add(any(Station.class));
        verify(mockRouteDao, times(1)).getStation(same(line), eq(9));
        verify(mockAdminDao, times(2)).add(any(StationsDistance.class));
        verify(mockAdminDao, times(1)).add(any(LineStation.class));
        verifyNoMoreInteractions(mockAdminDao);
        verifyNoMoreInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockPassengerDao);
        verifyZeroInteractions(mockMapper);
    }

    @Test
    void addNewMiddleStationTest() {
        String stationName = "middleStation";
        int lineId = 0;
        int order = 5;
        int distBefore = 10;
        int distAfter = 10;
        Line line = new Line();
        LineStation lineStation = new LineStation();
        List<LineStation> lineStations = Arrays
                .asList(lineStation, lineStation);
        Station firstStation = new Station();
        Station secondStation = new Station();
        when(mockRouteDao.getLineById(anyInt())).thenReturn(line);
        when(mockRouteDao.getLineStations(any(Line.class), anyInt()))
                .thenReturn(lineStations);
        when(mockRouteDao.getStation(any(Line.class), eq(4)))
                .thenReturn(firstStation);
        when(mockRouteDao.getStation(any(Line.class), eq(5)))
                .thenReturn(secondStation);
        when(mockRouteDao.getStation(any(Line.class), eq(6)))
                .thenReturn(new Station());

        adminDataService.addNewStation(stationName, lineId, order,
                distBefore, distAfter);

        verify(mockRouteDao, times(3)).getLineById(eq(0));
        verify(mockAdminDao, times(1))
                .deleteDistance(same(firstStation), same(secondStation));
        verify(mockRouteDao, times(1)).getLineStations(same(line), eq(5));
        verify(mockAdminDao, times(2)).update(lineStation);
        verify(mockAdminDao, times(1)).add(any(Station.class));
        verify(mockRouteDao, times(2)).getStation(same(line), eq(4));
        verify(mockRouteDao, times(1)).getStation(same(line), eq(5));
        verify(mockRouteDao, times(1)).getStation(same(line), eq(6));
        verify(mockAdminDao, times(4)).add(any(StationsDistance.class));
        verify(mockAdminDao, times(1)).add(any(LineStation.class));
        verifyNoMoreInteractions(mockAdminDao);
        verifyNoMoreInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockPassengerDao);
        verifyZeroInteractions(mockMapper);
    }

    @Test
    void modifyRouteTest() {
        int routeId = 0;
        String[] stations = new String[]{"firstStation", "secondStation",
            "thirdStation", "fourthStation"};
        int[] waitTimes = new int[]{5, 5};
        Route route = new Route();
        Station station = new Station();
        when(mockRouteDao.getRouteById(anyInt())).thenReturn(route);
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);

        adminDataService.modifyRoute(routeId, stations, waitTimes);

        verify(mockRouteDao, times(1)).getRouteById(eq(0));
        verify(mockAdminDao, times(1)).cleanRouteStopPattern(same(route));
        verify(mockScheduleDao, times(1)).getStationByName(same(stations[0]));
        verify(mockScheduleDao, times(1)).getStationByName(same(stations[1]));
        verify(mockScheduleDao, times(1)).getStationByName(same(stations[2]));
        verify(mockScheduleDao, times(1)).getStationByName(same(stations[3]));
        verify(mockAdminDao, times(4)).add(any(RouteStation.class));
        verifyNoMoreInteractions(mockAdminDao);
        verifyNoMoreInteractions(mockRouteDao);
        verifyNoMoreInteractions(mockScheduleDao);
        verifyZeroInteractions(mockPassengerDao);
        verifyZeroInteractions(mockMapper);
    }

    @Test
    void scheduleJourneysInboundTest() {
        int routeId = 0;
        LocalTime departure = LocalTime.NOON;
        LocalDate dayFrom = LocalDate.of(2020, 2, 20);
        LocalDate dayUntil = LocalDate.of(2020, 2, 22);
        int trainId = 0;
        Route route = new Route();
        Line line = new Line();
        route.setLine(line);
        Train train = new Train();
        train.setSpeed(100);
        RouteStation routeStation = new RouteStation();
        Station station = new Station();
        routeStation.setWaitTime(10);
        routeStation.setStation(station);
        List<RouteStation> routeStations = Arrays.asList(routeStation,
                routeStation, routeStation, routeStation);
        route.setStations(routeStations);
        when(mockRouteDao.getRouteById(anyInt())).thenReturn(route);
        when(mockAdminDao.getTrainById(anyInt())).thenReturn(train);
        when(mockRouteDao.inboundDistance(any(Station.class),
                any(Station.class), any(Line.class))).thenReturn(50);

        adminDataService.scheduleJourneys(routeId, departure, dayFrom,
                dayUntil, trainId, false);

        verify(mockRouteDao, times(1)).getRouteById(eq(0));
        verify(mockAdminDao, times(1)).getTrainById(eq(0));
        verify(mockAdminDao, times(3)).add(any(Journey.class));
        verify(mockRouteDao, times(9)).inboundDistance(same(station),
                same(station), same(line));
        verify(mockAdminDao, times(12)).add(any(ScheduledStop.class));
        verify(mockJmsTemplate, times(1)).send(any());
        verifyNoMoreInteractions(mockRouteDao);
        verifyNoMoreInteractions(mockAdminDao);
        verifyNoMoreInteractions(mockJmsTemplate);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockPassengerDao);
        verifyZeroInteractions(mockMapper);
    }

    @Test
    void scheduleJourneysOutboundTest() {
        int routeId = 0;
        LocalTime departure = LocalTime.NOON;
        LocalDate dayFrom = LocalDate.of(2020, 2, 20);
        LocalDate dayUntil = LocalDate.of(2020, 2, 22);
        int trainId = 0;
        Route route = new Route();
        Line line = new Line();
        route.setLine(line);
        Train train = new Train();
        train.setSpeed(100);
        RouteStation routeStation = new RouteStation();
        Station station = new Station();
        routeStation.setStation(station);
        routeStation.setWaitTime(10);
        List<RouteStation> routeStations = Arrays.asList(routeStation,
                routeStation, routeStation, routeStation);
        route.setStations(routeStations);
        when(mockRouteDao.getRouteById(anyInt())).thenReturn(route);
        when(mockAdminDao.getTrainById(anyInt())).thenReturn(train);
        when(mockRouteDao.outboundDistance(any(Station.class),
                any(Station.class), any(Line.class))).thenReturn(50);

        adminDataService.scheduleJourneys(routeId, departure, dayFrom,
                dayUntil, trainId, true);

        verify(mockRouteDao, times(1)).getRouteById(eq(0));
        verify(mockAdminDao, times(1)).getTrainById(eq(0));
        verify(mockAdminDao, times(3)).add(any(Journey.class));
        verify(mockRouteDao, times(9)).outboundDistance(same(station),
                same(station), same(line));
        verify(mockAdminDao, times(12)).add(any(ScheduledStop.class));
        verify(mockJmsTemplate, times(1)).send(any());
        verifyNoMoreInteractions(mockRouteDao);
        verifyNoMoreInteractions(mockAdminDao);
        verifyNoMoreInteractions(mockJmsTemplate);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockPassengerDao);
        verifyZeroInteractions(mockMapper);
    }

    @Test
    void delayJourneyTest() {
        Journey journey = new Journey();
        ScheduledStop firstStop = new ScheduledStop();
        firstStop.setDeparture(LocalDateTime.now(mockClock));
        ScheduledStop middleStop = new ScheduledStop();
        middleStop.setArrival(LocalDateTime.now(mockClock));
        middleStop.setDeparture(LocalDateTime.now(mockClock));
        ScheduledStop lastStop = new ScheduledStop();
        lastStop.setArrival(LocalDateTime.now(mockClock));
        journey.setStops(Arrays.asList(firstStop, middleStop, lastStop));
        LocalDateTime expected = LocalDateTime.now(mockClock).plusMinutes(5);
        when(mockPassengerDao.getJourneyById(anyInt())).thenReturn(journey);

        adminDataService.delayJourney(0, 5);

        verify(mockPassengerDao, times(1)).getJourneyById(eq(0));
        verify(mockAdminDao, times(1)).update(same(journey));
        verify(mockAdminDao, times(1)).update(same(firstStop));
        verify(mockAdminDao, times(1)).update(same(middleStop));
        verify(mockAdminDao, times(1)).update(same(lastStop));
        verify(mockJmsTemplate, times(1)).send(any());
        verifyNoMoreInteractions(mockPassengerDao);
        verifyNoMoreInteractions(mockAdminDao);
        verifyNoMoreInteractions(mockJmsTemplate);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockRouteDao);
        verifyZeroInteractions(mockMapper);

        assertNull(firstStop.getEta());
        assertEquals(expected, firstStop.getEtd());
        assertEquals(expected, middleStop.getEta());
        assertEquals(expected, middleStop.getEtd());
        assertNull(lastStop.getEtd());
        assertEquals(expected, lastStop.getEta());
    }
}
