package ru.tsystems.sbb.services.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.ScheduledStopDto;
import ru.tsystems.sbb.model.dto.SignUpDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.model.dto.TransferTrainsDto;
import ru.tsystems.sbb.services.data.RouteDataService;
import ru.tsystems.sbb.services.data.ScheduleDataService;

import javax.persistence.NoResultException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ScheduleViewServiceImplTest {

    @InjectMocks
    private ScheduleViewServiceImpl scheduleViewService;

    @Mock
    private RouteDataService mockRouteDataService;

    @Mock
    private ScheduleDataService mockScheduleDataService;

    @Mock
    private Clock mockClock;

    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 2, 2);
    private static final String ERROR = "There was an error processing your "
            + "request. Please check your inputs.";
    private static final String FAIL = "Sorry, there were no trains found "
            + "fulfilling your search criteria :(";

    @BeforeEach
    void initMocks(@Mock Authentication auth,
                   @Mock SecurityContext securityContext){
        MockitoAnnotations.initMocks(this);
        Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId
                .systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.lenient().when(mockClock.instant())
                .thenReturn(fixedClock.instant());
        Mockito.lenient().when(mockClock.getZone())
                .thenReturn(fixedClock.getZone());
        Mockito.lenient().when(securityContext.getAuthentication())
                .thenReturn(auth);
        Mockito.lenient().when(auth.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getStationScheduleExceptionTest() {
        String stationName = "stationName";
        String momentFrom = "2020-02-02 20:02";
        LocalDateTime moment = LocalDateTime.parse(momentFrom,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        when(mockScheduleDataService.stationSchedule(anyString(),
                any(LocalDateTime.class))).thenThrow(new NoResultException());

        Map<String, Object> result = scheduleViewService
                .getStationSchedule(stationName, momentFrom);

        verify(mockScheduleDataService, times(1))
                .stationSchedule(same(stationName), eq(moment));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("error"));
        assertEquals(ERROR, result.get("error"));
        assertFalse(result.containsKey("trains"));
        assertFalse(result.containsKey("stationName"));
        assertFalse(result.containsKey("momentFrom"));
    }

    @Test
    void getStationScheduleNullDateTest() {
        String stationName = "stationName";
        String momentFrom = null;
        List<ScheduledStopDto> trains = new ArrayList<>();
        when(mockScheduleDataService.stationSchedule(anyString(),
                any(LocalDateTime.class))).thenReturn(trains);

        Map<String, Object> result = scheduleViewService
                .getStationSchedule(stationName, momentFrom);

        verify(mockScheduleDataService, times(1))
                .stationSchedule(same(stationName), eq(LOCAL_DATE.atStartOfDay()));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("trains"));
        assertSame(trains, result.get("trains"));
        assertTrue(result.containsKey("stationName"));
        assertSame(stationName, result.get(stationName));
        assertTrue(result.containsKey("momentFrom"));
        assertEquals(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .format(LOCAL_DATE.atStartOfDay()), result.get("momentFrom"));
        assertFalse(result.containsKey("error"));
    }

    @Test
    void getStationScheduleEmptyDateTest() {
        String stationName = "stationName";
        String momentFrom = "";
        List<ScheduledStopDto> trains = new ArrayList<>();
        when(mockScheduleDataService.stationSchedule(anyString(),
                any(LocalDateTime.class))).thenReturn(trains);

        Map<String, Object> result = scheduleViewService
                .getStationSchedule(stationName, momentFrom);

        verify(mockScheduleDataService, times(1))
                .stationSchedule(same(stationName), eq(LOCAL_DATE.atStartOfDay()));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("trains"));
        assertSame(trains, result.get("trains"));
        assertTrue(result.containsKey("stationName"));
        assertSame(stationName, result.get(stationName));
        assertTrue(result.containsKey("momentFrom"));
        assertEquals(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .format(LOCAL_DATE.atStartOfDay()), result.get("momentFrom"));
        assertFalse(result.containsKey("error"));

    }

    @Test
    void getStationScheduleFixedDateTest() {
        String stationName = "stationName";
        String momentFrom = "2020-02-02 20:02";
        LocalDateTime moment = LocalDateTime.parse(momentFrom,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        List<ScheduledStopDto> trains = new ArrayList<>();
        when(mockScheduleDataService.stationSchedule(anyString(),
                any(LocalDateTime.class))).thenReturn(trains);

        Map<String, Object> result = scheduleViewService
                .getStationSchedule(stationName, momentFrom);

        verify(mockScheduleDataService, times(1))
                .stationSchedule(same(stationName), eq(moment));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("trains"));
        assertSame(trains, result.get("trains"));
        assertTrue(result.containsKey("stationName"));
        assertSame(stationName, result.get(stationName));
        assertTrue(result.containsKey("momentFrom"));
        assertEquals(momentFrom, result.get("momentFrom"));
        assertFalse(result.containsKey("error"));
    }

    @Test
    void getStationsListAnonymousTest(@Mock SecurityContext securityContext) {
        Authentication auth = new AnonymousAuthenticationToken("key",
                "anonymous", AuthorityUtils
                .createAuthorityList("ROLE_ANONYMOUS"));
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        List<StationDto> stations = new ArrayList<>();
        when(mockRouteDataService.allStations()).thenReturn(stations);

        Map<String, Object> result = scheduleViewService.getStationsList();

        verify(mockRouteDataService, times(1)).allStations();
        verifyNoMoreInteractions(mockRouteDataService);
        verifyZeroInteractions(mockScheduleDataService);

        assertTrue(result.containsKey("stations"));
        assertSame(stations, result.get("stations"));
        assertTrue(result.containsKey("signUpDto"));
        assertEquals(new SignUpDto(), result.get("signUpDto"));
    }

    @Test
    void getStationsListLoggedInTest() {
        List<StationDto> stations = new ArrayList<>();
        when(mockRouteDataService.allStations()).thenReturn(stations);

        Map<String, Object> result = scheduleViewService.getStationsList();

        verify(mockRouteDataService, times(1)).allStations();
        verifyNoMoreInteractions(mockRouteDataService);
        verifyZeroInteractions(mockScheduleDataService);

        assertTrue(result.containsKey("stations"));
        assertSame(stations, result.get("stations"));
        assertFalse(result.containsKey("signUpDto"));
    }

    @Test
    void getTrainsFromToExceptionTest() {
        String origin = "origin";
        String destination = "destination";
        String dateTime = "2020-02-02 20:02";
        String searchType = "departure";
        when(mockScheduleDataService.directTrainsFromTo(anyString(),
                anyString(), any(LocalDateTime.class), anyString()))
                .thenThrow(new NoResultException());

        Map<String, Object> result = scheduleViewService
                .getTrainsFromTo(origin, destination, dateTime, searchType);

        verify(mockScheduleDataService, times(1))
                .directTrainsFromTo(same(origin), same(destination),
                        eq(LocalDateTime.parse(dateTime, DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm"))), same(searchType));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("error"));
        assertEquals(ERROR, result.get("error"));
        assertFalse(result.containsKey("trains"));
        assertFalse(result.containsKey("connections"));
        assertFalse(result.containsKey("origin"));
        assertFalse(result.containsKey("destination"));
        assertFalse(result.containsKey("fail"));
    }

    @Test
    void getTrainsFromToDirectSuccessTest() {
        String origin = "origin";
        String destination = "destination";
        String dateTime = "2020-02-02 20:02";
        String searchType = "departure";
        List<JourneyDto> trains = new ArrayList<>();
        trains.add(new JourneyDto());
        when(mockScheduleDataService.directTrainsFromTo(anyString(),
                anyString(), any(LocalDateTime.class), anyString()))
                .thenReturn(trains);

        Map<String, Object> result = scheduleViewService
                .getTrainsFromTo(origin, destination, dateTime, searchType);

        verify(mockScheduleDataService, times(1))
                .directTrainsFromTo(same(origin), same(destination),
                        eq(LocalDateTime.parse(dateTime, DateTimeFormatter
                                .ofPattern("yyyy-MM-dd HH:mm"))),
                        same(searchType));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("trains"));
        assertSame(trains, result.get("trains"));
        assertTrue(result.containsKey("origin"));
        assertSame(origin, result.get("origin"));
        assertTrue(result.containsKey("destination"));
        assertSame(destination, result.get("destination"));
        assertFalse(result.containsKey("connections"));
        assertFalse(result.containsKey("fail"));
        assertFalse(result.containsKey("error"));
    }

    @Test
    void getTrainsFromToTransferSuccessTest() {
        String origin = "origin";
        String destination = "destination";
        String dateTime = "2020-02-02 20:02";
        String searchType = "departure";
        List<JourneyDto> trains = new ArrayList<>();
        List<TransferTrainsDto> connections = new ArrayList<>();
        connections.add(new TransferTrainsDto());
        when(mockScheduleDataService.directTrainsFromTo(anyString(),
                anyString(), any(LocalDateTime.class), anyString()))
                .thenReturn(trains);
        when(mockScheduleDataService.trainsWithTransfer(anyString(),
                anyString(), any(LocalDateTime.class), anyString()))
                .thenReturn(connections);

        Map<String, Object> result = scheduleViewService
                .getTrainsFromTo(origin, destination, dateTime, searchType);

        verify(mockScheduleDataService, times(1))
                .directTrainsFromTo(same(origin), same(destination),
                        eq(LocalDateTime.parse(dateTime, DateTimeFormatter
                                .ofPattern("yyyy-MM-dd HH:mm"))),
                        same(searchType));
        verify(mockScheduleDataService, times(1))
                .trainsWithTransfer(same(origin), same(destination),
                        eq(LocalDateTime.parse(dateTime, DateTimeFormatter
                                .ofPattern("yyyy-MM-dd HH:mm"))),
                        same(searchType));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("connections"));
        assertSame(connections, result.get("connections"));
        assertTrue(result.containsKey("origin"));
        assertSame(origin, result.get("origin"));
        assertTrue(result.containsKey("destination"));
        assertSame(destination, result.get("destination"));
        assertFalse(result.containsKey("trains"));
        assertFalse(result.containsKey("fail"));
        assertFalse(result.containsKey("error"));

    }

    @Test
    void getTrainsFromToFailTest() {
        String origin = "origin";
        String destination = "destination";
        String dateTime = "2020-02-02 20:02";
        String searchType = "departure";
        List<JourneyDto> trains = new ArrayList<>();
        List<TransferTrainsDto> connections = new ArrayList<>();
        when(mockScheduleDataService.directTrainsFromTo(anyString(),
                anyString(), any(LocalDateTime.class), anyString()))
                .thenReturn(trains);
        when(mockScheduleDataService.trainsWithTransfer(anyString(),
                anyString(), any(LocalDateTime.class), anyString()))
                .thenReturn(connections);

        Map<String, Object> result = scheduleViewService
                .getTrainsFromTo(origin, destination, dateTime, searchType);

        verify(mockScheduleDataService, times(1))
                .directTrainsFromTo(same(origin), same(destination),
                        eq(LocalDateTime.parse(dateTime, DateTimeFormatter
                                .ofPattern("yyyy-MM-dd HH:mm"))),
                        same(searchType));
        verify(mockScheduleDataService, times(1))
                .trainsWithTransfer(same(origin), same(destination),
                        eq(LocalDateTime.parse(dateTime, DateTimeFormatter
                                .ofPattern("yyyy-MM-dd HH:mm"))),
                        same(searchType));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("fail"));
        assertEquals(FAIL, result.get("fail"));
        assertTrue(result.containsKey("origin"));
        assertSame(origin, result.get("origin"));
        assertTrue(result.containsKey("destination"));
        assertSame(destination, result.get("destination"));
        assertFalse(result.containsKey("trains"));
        assertFalse(result.containsKey("connections"));
        assertFalse(result.containsKey("error"));
    }

}
