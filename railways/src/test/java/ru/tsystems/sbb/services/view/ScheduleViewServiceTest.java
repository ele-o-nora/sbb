package ru.tsystems.sbb.services.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ru.tsystems.sbb.config.ViewServiceTestConfig;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.dto.ScheduledStopDto;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.SignUpDto;
import ru.tsystems.dto.StationDto;
import ru.tsystems.sbb.model.dto.TransferTrainsDto;
import ru.tsystems.sbb.services.data.RouteDataService;
import ru.tsystems.sbb.services.data.ScheduleDataService;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ViewServiceTestConfig.class},
    loader = AnnotationConfigContextLoader.class)
@WithMockUser(username = "testUser")
class ScheduleViewServiceTest {

    @Autowired
    private ScheduleViewService scheduleViewService;

    @Autowired
    private RouteDataService mockRouteDataService;

    @Autowired
    private ScheduleDataService mockScheduleDataService;

    private static final LocalDate LOCAL_DATE = LocalDate.of(2020, 2, 2);
    private static final String ERROR_TEXT = "There was an error processing your "
            + "request. Please check your inputs.";
    private static final String FAIL_TEXT = "Sorry, there were no trains found "
            + "fulfilling your search criteria :(";

    private static final String STATION_NAME = "stationName";
    private static final String PATTERN = "yyyy-MM-dd HH:mm";
    private static final String ERROR = "error";
    private static final String TRAINS = "trains";
    private static final String MOMENT_FROM = "momentFrom";
    private static final String DATE_TIME = "2020-02-02 20:02";
    private static final String STATIONS = "stations";
    private static final String SIGN_UP_DTO = "signUpDto";
    private static final String FAIL = "fail";
    private static final String ORIGIN = "origin";
    private static final String DESTINATION = "destination";
    private static final String SEARCH_TYPE = "departure";
    private static final String CONNECTIONS = "connections";

    @AfterEach
    void resetMocks() {
        reset(mockRouteDataService);
        reset(mockScheduleDataService);
    }

    @Test
    void getStationScheduleExceptionTest() {
        LocalDateTime moment = LocalDateTime.parse(DATE_TIME,
                DateTimeFormatter.ofPattern(PATTERN));
        when(mockScheduleDataService.stationSchedule(anyString(),
                any(LocalDateTime.class))).thenThrow(new NoResultException());

        Map<String, Object> result = scheduleViewService
                .getStationSchedule(STATION_NAME, DATE_TIME);

        verify(mockScheduleDataService, times(1))
                .stationSchedule(same(STATION_NAME), eq(moment));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(ERROR));
        assertEquals(ERROR_TEXT, result.get(ERROR));
        assertFalse(result.containsKey(TRAINS));
        assertFalse(result.containsKey(STATION_NAME));
        assertFalse(result.containsKey(MOMENT_FROM));
    }

    @Test
    void getStationScheduleNullDateTest() {
        List<ScheduledStopDto> trains = new ArrayList<>();
        when(mockScheduleDataService.stationSchedule(anyString(),
                any(LocalDateTime.class))).thenReturn(trains);

        Map<String, Object> result = scheduleViewService
                .getStationSchedule(STATION_NAME, null);

        verify(mockScheduleDataService, times(1))
                .stationSchedule(same(STATION_NAME), eq(LOCAL_DATE.atStartOfDay()));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(TRAINS));
        assertSame(trains, result.get(TRAINS));
        assertTrue(result.containsKey(STATION_NAME));
        assertSame(STATION_NAME, result.get(STATION_NAME));
        assertTrue(result.containsKey(MOMENT_FROM));
        assertEquals(DateTimeFormatter.ofPattern(PATTERN)
                .format(LOCAL_DATE.atStartOfDay()), result.get(MOMENT_FROM));
        assertFalse(result.containsKey(ERROR));
    }

    @Test
    void getStationScheduleEmptyDateTest() {
        String momentFrom = "";
        List<ScheduledStopDto> trains = new ArrayList<>();
        when(mockScheduleDataService.stationSchedule(anyString(),
                any(LocalDateTime.class))).thenReturn(trains);

        Map<String, Object> result = scheduleViewService
                .getStationSchedule(STATION_NAME, momentFrom);

        verify(mockScheduleDataService, times(1))
                .stationSchedule(same(STATION_NAME), eq(LOCAL_DATE.atStartOfDay()));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(TRAINS));
        assertSame(trains, result.get(TRAINS));
        assertTrue(result.containsKey(STATION_NAME));
        assertSame(STATION_NAME, result.get(STATION_NAME));
        assertTrue(result.containsKey(MOMENT_FROM));
        assertEquals(DateTimeFormatter.ofPattern(PATTERN)
                .format(LOCAL_DATE.atStartOfDay()), result.get(MOMENT_FROM));
        assertFalse(result.containsKey(ERROR));

    }

    @Test
    void getStationScheduleFixedDateTest() {
        LocalDateTime moment = LocalDateTime.parse(DATE_TIME,
                DateTimeFormatter.ofPattern(PATTERN));
        List<ScheduledStopDto> trains = new ArrayList<>();
        when(mockScheduleDataService.stationSchedule(anyString(),
                any(LocalDateTime.class))).thenReturn(trains);

        Map<String, Object> result = scheduleViewService
                .getStationSchedule(STATION_NAME, DATE_TIME);

        verify(mockScheduleDataService, times(1))
                .stationSchedule(same(STATION_NAME), eq(moment));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(TRAINS));
        assertSame(trains, result.get(TRAINS));
        assertTrue(result.containsKey(STATION_NAME));
        assertSame(STATION_NAME, result.get(STATION_NAME));
        assertTrue(result.containsKey(MOMENT_FROM));
        assertEquals(DATE_TIME, result.get(MOMENT_FROM));
        assertFalse(result.containsKey(ERROR));
    }

    @Test
    @WithAnonymousUser
    void getStationsListAnonymousTest() {
        List<StationDto> stations = new ArrayList<>();
        when(mockRouteDataService.allStations()).thenReturn(stations);

        Map<String, Object> result = scheduleViewService.getStationsList();

        verify(mockRouteDataService, times(1)).allStations();
        verifyNoMoreInteractions(mockRouteDataService);
        verifyZeroInteractions(mockScheduleDataService);

        assertTrue(result.containsKey(STATIONS));
        assertSame(stations, result.get(STATIONS));
        assertTrue(result.containsKey(SIGN_UP_DTO));
        assertEquals(new SignUpDto(), result.get(SIGN_UP_DTO));
    }

    @Test
    void getStationsListLoggedInTest() {
        List<StationDto> stations = new ArrayList<>();
        when(mockRouteDataService.allStations()).thenReturn(stations);

        Map<String, Object> result = scheduleViewService.getStationsList();

        verify(mockRouteDataService, times(1)).allStations();
        verifyNoMoreInteractions(mockRouteDataService);
        verifyZeroInteractions(mockScheduleDataService);

        assertTrue(result.containsKey(STATIONS));
        assertSame(stations, result.get(STATIONS));
        assertFalse(result.containsKey(SIGN_UP_DTO));
    }

    @Test
    void getTrainsFromToExceptionTest() {
        when(mockScheduleDataService.directTrainsFromTo(anyString(),
                anyString(), any(LocalDateTime.class), anyString()))
                .thenThrow(new NoResultException());

        Map<String, Object> result = scheduleViewService
                .getTrainsFromTo(ORIGIN, DESTINATION, DATE_TIME, SEARCH_TYPE);

        verify(mockScheduleDataService, times(1))
                .directTrainsFromTo(same(ORIGIN), same(DESTINATION),
                        eq(LocalDateTime.parse(DATE_TIME, DateTimeFormatter
                        .ofPattern(PATTERN))), same(SEARCH_TYPE));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(ERROR));
        assertEquals(ERROR_TEXT, result.get(ERROR));
        assertFalse(result.containsKey(TRAINS));
        assertFalse(result.containsKey(CONNECTIONS));
        assertFalse(result.containsKey(ORIGIN));
        assertFalse(result.containsKey(DESTINATION));
        assertFalse(result.containsKey(FAIL));
    }

    @Test
    void getTrainsFromToDirectSuccessTest() {
        List<JourneyDto> trains = new ArrayList<>();
        trains.add(new JourneyDto());
        when(mockScheduleDataService.directTrainsFromTo(anyString(),
                anyString(), any(LocalDateTime.class), anyString()))
                .thenReturn(trains);

        Map<String, Object> result = scheduleViewService
                .getTrainsFromTo(ORIGIN, DESTINATION, DATE_TIME, SEARCH_TYPE);

        verify(mockScheduleDataService, times(1))
                .directTrainsFromTo(same(ORIGIN), same(DESTINATION),
                        eq(LocalDateTime.parse(DATE_TIME, DateTimeFormatter
                                .ofPattern(PATTERN))),
                        same(SEARCH_TYPE));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(TRAINS));
        assertSame(trains, result.get(TRAINS));
        assertTrue(result.containsKey(ORIGIN));
        assertSame(ORIGIN, result.get(ORIGIN));
        assertTrue(result.containsKey(DESTINATION));
        assertSame(DESTINATION, result.get(DESTINATION));
        assertFalse(result.containsKey(CONNECTIONS));
        assertFalse(result.containsKey(FAIL));
        assertFalse(result.containsKey(ERROR));
    }

    @Test
    void getTrainsFromToTransferSuccessTest() {
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
                .getTrainsFromTo(ORIGIN, DESTINATION, DATE_TIME, SEARCH_TYPE);

        verify(mockScheduleDataService, times(1))
                .directTrainsFromTo(same(ORIGIN), same(DESTINATION),
                        eq(LocalDateTime.parse(DATE_TIME, DateTimeFormatter
                                .ofPattern(PATTERN))),
                        same(SEARCH_TYPE));
        verify(mockScheduleDataService, times(1))
                .trainsWithTransfer(same(ORIGIN), same(DESTINATION),
                        eq(LocalDateTime.parse(DATE_TIME, DateTimeFormatter
                                .ofPattern(PATTERN))),
                        same(SEARCH_TYPE));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(CONNECTIONS));
        assertSame(connections, result.get(CONNECTIONS));
        assertTrue(result.containsKey(ORIGIN));
        assertSame(ORIGIN, result.get(ORIGIN));
        assertTrue(result.containsKey(DESTINATION));
        assertSame(DESTINATION, result.get(DESTINATION));
        assertFalse(result.containsKey(TRAINS));
        assertFalse(result.containsKey(FAIL));
        assertFalse(result.containsKey(ERROR));

    }

    @Test
    void getTrainsFromToFailTest() {
        List<JourneyDto> trains = new ArrayList<>();
        List<TransferTrainsDto> connections = new ArrayList<>();
        when(mockScheduleDataService.directTrainsFromTo(anyString(),
                anyString(), any(LocalDateTime.class), anyString()))
                .thenReturn(trains);
        when(mockScheduleDataService.trainsWithTransfer(anyString(),
                anyString(), any(LocalDateTime.class), anyString()))
                .thenReturn(connections);

        Map<String, Object> result = scheduleViewService
                .getTrainsFromTo(ORIGIN, DESTINATION, DATE_TIME, SEARCH_TYPE);

        verify(mockScheduleDataService, times(1))
                .directTrainsFromTo(same(ORIGIN), same(DESTINATION),
                        eq(LocalDateTime.parse(DATE_TIME, DateTimeFormatter
                                .ofPattern(PATTERN))),
                        same(SEARCH_TYPE));
        verify(mockScheduleDataService, times(1))
                .trainsWithTransfer(same(ORIGIN), same(DESTINATION),
                        eq(LocalDateTime.parse(DATE_TIME, DateTimeFormatter
                                .ofPattern(PATTERN))),
                        same(SEARCH_TYPE));
        verifyNoMoreInteractions(mockScheduleDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(FAIL));
        assertEquals(FAIL_TEXT, result.get(FAIL));
        assertTrue(result.containsKey(ORIGIN));
        assertSame(ORIGIN, result.get(ORIGIN));
        assertTrue(result.containsKey(DESTINATION));
        assertSame(DESTINATION, result.get(DESTINATION));
        assertFalse(result.containsKey(TRAINS));
        assertFalse(result.containsKey(CONNECTIONS));
        assertFalse(result.containsKey(ERROR));
    }

    @Test
    void prepareRailwayMapTest() {
        LineDto firstLine = new LineDto();
        firstLine.setId(1);
        firstLine.setName("First");
        LineDto secondLine = new LineDto();
        secondLine.setId(2);
        secondLine.setName("Second");
        List<LineDto> lines = Arrays.asList(firstLine, secondLine);
        List<StationDto> firstStations = Collections.emptyList();
        List<StationDto> secondStations = Collections.emptyList();
        when(mockRouteDataService.getAllLines()).thenReturn(lines);
        when(mockRouteDataService.getAllLineStations(eq(1)))
                .thenReturn(firstStations);
        when(mockRouteDataService.getAllLineStations(eq(2)))
                .thenReturn(secondStations);

        Map<String, Object> result = scheduleViewService.prepareRailwayMap();

        verify(mockRouteDataService, times(1)).getAllLines();
        verify(mockRouteDataService, times(1)).getAllLineStations(eq(1));
        verify(mockRouteDataService, times(1)).getAllLineStations(eq(2));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyZeroInteractions(mockScheduleDataService);

        assertTrue(result.containsKey("stationsFirst"));
        assertSame(firstStations, result.get("stationsFirst"));
        assertTrue(result.containsKey("stationsSecond"));
        assertSame(secondStations, result.get("stationsSecond"));
    }

}
