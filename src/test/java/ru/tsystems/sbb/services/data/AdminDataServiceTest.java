package ru.tsystems.sbb.services.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tsystems.sbb.model.dao.AdminDao;
import ru.tsystems.sbb.model.dao.PassengerDao;
import ru.tsystems.sbb.model.dao.RouteDao;
import ru.tsystems.sbb.model.dao.ScheduleDao;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.StationsDistance;
import ru.tsystems.sbb.model.mappers.EntityToDtoMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDate;
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

    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 2, 2);

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
        String stationName = "stationName";
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
        when(mockAdminDao.getStation(any(Line.class), anyInt()))
                .thenReturn(new Station());

        adminDataService.addNewStation(stationName, lineId, order,
                distBefore, distAfter);

        verify(mockRouteDao, times(2)).getLineById(eq(0));
        verify(mockRouteDao, times(1)).getLineStations(same(line), eq(1));
        verify(mockAdminDao, times(2)).update(lineStation);
        verify(mockAdminDao, times(1)).add(any(Station.class));
        verify(mockAdminDao, times(1)).getStation(same(line), eq(2));
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
        String stationName = "stationName";
        int lineId = 0;
        int order = 10;
        int distBefore = 10;
        int distAfter = 0;
        Line line = new Line();
        when(mockRouteDao.getLineById(anyInt())).thenReturn(line);
        when(mockRouteDao.getLineStations(any(Line.class), anyInt()))
                .thenReturn(new ArrayList<>());
        when(mockAdminDao.getStation(any(Line.class), anyInt()))
                .thenReturn(new Station());

        adminDataService.addNewStation(stationName, lineId, order,
                distBefore, distAfter);

        verify(mockRouteDao, times(2)).getLineById(eq(0));
        verify(mockRouteDao, times(1)).getLineStations(same(line), eq(10));
        verify(mockAdminDao, times(1)).add(any(Station.class));
        verify(mockAdminDao, times(1)).getStation(same(line), eq(9));
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
        String stationName = "stationName";
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
        when(mockAdminDao.getStation(any(Line.class), eq(4)))
                .thenReturn(firstStation);
        when(mockAdminDao.getStation(any(Line.class), eq(5)))
                .thenReturn(secondStation);
        when(mockAdminDao.getStation(any(Line.class), eq(6)))
                .thenReturn(new Station());

        adminDataService.addNewStation(stationName, lineId, order,
                distBefore, distAfter);

        verify(mockRouteDao, times(3)).getLineById(eq(0));
        verify(mockAdminDao, times(1))
                .deleteDistance(same(firstStation), same(secondStation));
        verify(mockRouteDao, times(1)).getLineStations(same(line), eq(5));
        verify(mockAdminDao, times(2)).update(lineStation);
        verify(mockAdminDao, times(1)).add(any(Station.class));
        verify(mockAdminDao, times(2)).getStation(same(line), eq(4));
        verify(mockAdminDao, times(1)).getStation(same(line), eq(5));
        verify(mockAdminDao, times(1)).getStation(same(line), eq(6));
        verify(mockAdminDao, times(4)).add(any(StationsDistance.class));
        verify(mockAdminDao, times(1)).add(any(LineStation.class));
        verifyNoMoreInteractions(mockAdminDao);
        verifyNoMoreInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockPassengerDao);
        verifyZeroInteractions(mockMapper);
    }
}
