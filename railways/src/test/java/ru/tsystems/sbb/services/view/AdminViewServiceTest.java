package ru.tsystems.sbb.services.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ru.tsystems.dto.StationDto;
import ru.tsystems.sbb.config.ViewServiceTestConfig;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.RouteDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.dto.TrainDto;
import ru.tsystems.sbb.services.data.AdminDataService;
import ru.tsystems.sbb.services.data.RouteDataService;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ViewServiceTestConfig.class},
    loader = AnnotationConfigContextLoader.class)
@WithMockUser(username = "testEmployee", roles = {"ADMIN"})
class AdminViewServiceTest {

    @Autowired
    private AdminViewService adminViewService;

    @Autowired
    private RouteDataService mockRouteDataService;

    @Autowired
    private AdminDataService mockAdminDataService;

    @Autowired
    private Clock mockClock;

    private static final String ROUTE_NUMBER = "routeNumber";
    private static final String ROUTE = "route";
    private static final String ROUTES = "routes";
    private static final String ROUTE_STATIONS = "routeStations";
    private static final String LINE = "line";
    private static final String LINES = "lines";
    private static final String TODAY = "today";
    private static final String NEXT_DAY = "nextDay";
    private static final String PREVIOUS_DAY = "previousDay";
    private static final String NEXT_PAGE = "nextPage";
    private static final String PREVIOUS_PAGE = "previousPage";
    private static final String JOURNEY = "journey";
    private static final String JOURNEYS = "journeys";
    private static final String TICKETS = "tickets";
    private static final String TARIFF = "tariff";
    private static final String TRAIN_MODELS = "trainModels";
    private static final String STATIONS = "stations";

    @AfterEach
    void resetMocks() {
        reset(mockAdminDataService);
        reset(mockRouteDataService);
    }

    @Test
    void updateTariffZeroTest() {
        float price = 0;

        adminViewService.updateTariff(price);

        verifyZeroInteractions(mockAdminDataService);
        verifyZeroInteractions(mockRouteDataService);
    }

    @Test
    void updateTariffSuccessTest() {
        float price = 1;

        adminViewService.updateTariff(price);

        verify(mockAdminDataService, times(1)).updateTariff(eq(price));
        verifyNoMoreInteractions(mockAdminDataService);
        verifyZeroInteractions(mockRouteDataService);
    }

    @Test
    void modifyRouteStationsZeroRouteIdTest() {
        LineDto line = new LineDto();
        List<StationDto> stations = new ArrayList<>();
        when(mockRouteDataService.getLine(anyInt())).thenReturn(line);
        when(mockRouteDataService.getAllLineStations(anyInt()))
                .thenReturn(stations);

        Map<String, Object> result = adminViewService.modifyRouteStations(1, 0);

        verify(mockRouteDataService, times(1)).getLine(eq(1));
        verify(mockRouteDataService, times(1)).getAllLineStations(eq(1));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyZeroInteractions(mockAdminDataService);

        assertTrue(result.containsKey(LINE));
        assertSame(line, result.get(LINE));
        assertTrue(result.containsKey(STATIONS));
        assertSame(stations, result.get(STATIONS));
        assertFalse(result.containsKey(ROUTE));
    }

    @Test
    void modifyRouteStationsNonZeroRouteIdTest() {
        LineDto line = new LineDto();
        List<StationDto> stations = new ArrayList<>();
        RouteDto route = new RouteDto();
        when(mockRouteDataService.getLine(anyInt())).thenReturn(line);
        when(mockRouteDataService.getAllLineStations(anyInt()))
                .thenReturn(stations);
        when(mockRouteDataService.getRoute(anyInt())).thenReturn(route);

        Map<String, Object> result = adminViewService.modifyRouteStations(1, 1);

        verify(mockRouteDataService, times(1)).getLine(eq(1));
        verify(mockRouteDataService, times(1)).getAllLineStations(eq(1));
        verify(mockRouteDataService, times(1)).getRoute(eq(1));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyZeroInteractions(mockAdminDataService);

        assertTrue(result.containsKey(LINE));
        assertSame(line, result.get(LINE));
        assertTrue(result.containsKey(STATIONS));
        assertSame(stations, result.get(STATIONS));
        assertTrue(result.containsKey(ROUTE));
        assertSame(route, result.get(ROUTE));

    }

    @Test
    void newRouteStopPatternZeroRouteIdTest() {
        String routeNumber = ROUTE_NUMBER;
        int routeId = 0;
        int lineId = 0;
        String[] stations = new String[]{};
        LineDto line = new LineDto();

        when(mockRouteDataService.getLine(anyInt())).thenReturn(line);

        Map<String, Object> result = adminViewService
                .newRouteStopPattern(routeNumber, routeId, lineId, stations);

        verify(mockRouteDataService, times(1)).getLine(eq(lineId));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyZeroInteractions(mockAdminDataService);

        assertTrue(result.containsKey(ROUTE_NUMBER));
        assertSame(routeNumber, result.get(ROUTE_NUMBER));
        assertTrue(result.containsKey(LINE));
        assertSame(line, result.get(LINE));
        assertTrue(result.containsKey(ROUTE_STATIONS));
        assertSame(stations, result.get(ROUTE_STATIONS));
        assertFalse(result.containsKey(ROUTE));
    }

    @Test
    void newRouteStopPatternNonZeroRouteIdTest() {
        String routeNumber = ROUTE_NUMBER;
        int routeId = 1;
        int lineId = 0;
        String[] stations = new String[]{};
        LineDto line = new LineDto();
        RouteDto route = new RouteDto();

        when(mockRouteDataService.getLine(anyInt())).thenReturn(line);
        when(mockRouteDataService.getRoute(anyInt())).thenReturn(route);

        Map<String, Object> result = adminViewService
                .newRouteStopPattern(routeNumber, routeId, lineId, stations);

        verify(mockRouteDataService, times(1)).getLine(eq(lineId));
        verify(mockRouteDataService, times(1)).getRoute(eq(routeId));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyZeroInteractions(mockAdminDataService);

        assertTrue(result.containsKey(ROUTE_NUMBER));
        assertSame(routeNumber, result.get(ROUTE_NUMBER));
        assertTrue(result.containsKey(LINE));
        assertSame(line, result.get(LINE));
        assertTrue(result.containsKey(ROUTE_STATIONS));
        assertSame(stations, result.get(ROUTE_STATIONS));
        assertTrue(result.containsKey(ROUTE));
        assertSame(route, result.get(ROUTE));
    }

    @Test
    void lookUpJourneysOnlyPageTest() {
        String date = "2020-02-02";
        LocalDateTime moment = LocalDateTime
                .of(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE),
                        LocalTime.MIDNIGHT);
        int page = 1;
        List<JourneyDto> journeys = new ArrayList<>();
        when(mockAdminDataService.maxJourneyPages(any(LocalDateTime.class)))
                .thenReturn(1);
        when(mockAdminDataService.getJourneys(any(LocalDateTime.class),
                anyInt())).thenReturn(journeys);

        Map<String, Object> result = adminViewService
                .lookUpJourneys(date, page);

        verify(mockAdminDataService, times(1)).maxJourneyPages(eq(moment));
        verify(mockAdminDataService, times(1))
                .getJourneys(eq(moment), eq(page));
        verifyNoMoreInteractions(mockAdminDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(TODAY));
        assertSame(date, result.get(TODAY));
        assertTrue(result.containsKey(PREVIOUS_DAY));
        assertEquals("2020-02-01", result.get(PREVIOUS_DAY));
        assertTrue(result.containsKey(NEXT_DAY));
        assertEquals("2020-02-03", result.get(NEXT_DAY));
        assertTrue(result.containsKey(JOURNEYS));
        assertSame(journeys, result.get(JOURNEYS));
        assertFalse(result.containsKey(PREVIOUS_PAGE));
        assertFalse(result.containsKey(NEXT_PAGE));
    }

    @Test
    void lookUpJourneysFirstPageTest() {
        String date = "2020-02-02";
        LocalDateTime moment = LocalDateTime
                .of(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE),
                        LocalTime.MIDNIGHT);
        int page = 1;
        List<JourneyDto> journeys = new ArrayList<>();
        when(mockAdminDataService.maxJourneyPages(any(LocalDateTime.class)))
                .thenReturn(2);
        when(mockAdminDataService.getJourneys(any(LocalDateTime.class),
                anyInt())).thenReturn(journeys);

        Map<String, Object> result = adminViewService
                .lookUpJourneys(date, page);

        verify(mockAdminDataService, times(1)).maxJourneyPages(eq(moment));
        verify(mockAdminDataService, times(1))
                .getJourneys(eq(moment), eq(page));
        verifyNoMoreInteractions(mockAdminDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(TODAY));
        assertSame(date, result.get(TODAY));
        assertTrue(result.containsKey(PREVIOUS_DAY));
        assertEquals("2020-02-01", result.get(PREVIOUS_DAY));
        assertTrue(result.containsKey(NEXT_DAY));
        assertEquals("2020-02-03", result.get(NEXT_DAY));
        assertTrue(result.containsKey(JOURNEYS));
        assertSame(journeys, result.get(JOURNEYS));
        assertTrue(result.containsKey(NEXT_PAGE));
        assertEquals(2, result.get(NEXT_PAGE));
        assertFalse(result.containsKey(PREVIOUS_PAGE));
    }

    @Test
    void lookUpJourneysMiddlePageTest() {
        String date = "2020-02-02";
        LocalDateTime moment = LocalDateTime
                .of(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE),
                        LocalTime.MIDNIGHT);
        int page = 2;
        List<JourneyDto> journeys = new ArrayList<>();
        when(mockAdminDataService.maxJourneyPages(any(LocalDateTime.class)))
                .thenReturn(3);
        when(mockAdminDataService.getJourneys(any(LocalDateTime.class),
                anyInt())).thenReturn(journeys);

        Map<String, Object> result = adminViewService
                .lookUpJourneys(date, page);

        verify(mockAdminDataService, times(1)).maxJourneyPages(eq(moment));
        verify(mockAdminDataService, times(1))
                .getJourneys(eq(moment), eq(page));
        verifyNoMoreInteractions(mockAdminDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(TODAY));
        assertSame(date, result.get(TODAY));
        assertTrue(result.containsKey(PREVIOUS_DAY));
        assertEquals("2020-02-01", result.get(PREVIOUS_DAY));
        assertTrue(result.containsKey(NEXT_DAY));
        assertEquals("2020-02-03", result.get(NEXT_DAY));
        assertTrue(result.containsKey(JOURNEYS));
        assertSame(journeys, result.get(JOURNEYS));
        assertTrue(result.containsKey(NEXT_PAGE));
        assertEquals(3, result.get(NEXT_PAGE));
        assertTrue(result.containsKey(PREVIOUS_PAGE));
        assertEquals(1, result.get(PREVIOUS_PAGE));
    }

    @Test
    void lookUpJourneysLastPageTest() {
        String date = "2020-02-02";
        LocalDateTime moment = LocalDateTime
                .of(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE),
                        LocalTime.MIDNIGHT);
        int page = 2;
        List<JourneyDto> journeys = new ArrayList<>();
        when(mockAdminDataService.maxJourneyPages(any(LocalDateTime.class)))
                .thenReturn(2);
        when(mockAdminDataService.getJourneys(any(LocalDateTime.class),
                anyInt())).thenReturn(journeys);

        Map<String, Object> result = adminViewService
                .lookUpJourneys(date, page);

        verify(mockAdminDataService, times(1)).maxJourneyPages(eq(moment));
        verify(mockAdminDataService, times(1))
                .getJourneys(eq(moment), eq(page));
        verifyNoMoreInteractions(mockAdminDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(TODAY));
        assertSame(date, result.get(TODAY));
        assertTrue(result.containsKey(PREVIOUS_DAY));
        assertEquals("2020-02-01", result.get(PREVIOUS_DAY));
        assertTrue(result.containsKey(NEXT_DAY));
        assertEquals("2020-02-03", result.get(NEXT_DAY));
        assertTrue(result.containsKey(JOURNEYS));
        assertSame(journeys, result.get(JOURNEYS));
        assertTrue(result.containsKey(PREVIOUS_PAGE));
        assertEquals(1, result.get(PREVIOUS_PAGE));
        assertFalse(result.containsKey(NEXT_PAGE));
    }

    @Test
    void listPassengersOnlyPageTest() {
        int journeyId = 0;
        int page = 1;
        JourneyDto journey = new JourneyDto();
        List<TicketDto> tickets = new ArrayList<>();
        when(mockAdminDataService.getJourneyById(anyInt())).thenReturn(journey);
        when(mockAdminDataService.maxPassengerPages(anyInt())).thenReturn(1);
        when(mockAdminDataService.getTickets(anyInt(), anyInt()))
                .thenReturn(tickets);

        Map<String, Object> result = adminViewService
                .listPassengers(journeyId, page);

        verify(mockAdminDataService, times(1)).getJourneyById(eq(journeyId));
        verify(mockAdminDataService, times(1)).maxPassengerPages(eq(journeyId));
        verify(mockAdminDataService, times(1))
                .getTickets(eq(journeyId), eq(page));
        verifyNoMoreInteractions(mockAdminDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(JOURNEY));
        assertSame(journey, result.get(JOURNEY));
        assertTrue(result.containsKey(TICKETS));
        assertSame(tickets, result.get(TICKETS));
        assertFalse(result.containsKey(PREVIOUS_PAGE));
        assertFalse(result.containsKey(NEXT_PAGE));
    }

    @Test
    void listPassengersFirstPageTest() {
        int journeyId = 0;
        int page = 1;
        JourneyDto journey = new JourneyDto();
        List<TicketDto> tickets = new ArrayList<>();
        when(mockAdminDataService.getJourneyById(anyInt())).thenReturn(journey);
        when(mockAdminDataService.maxPassengerPages(anyInt())).thenReturn(2);
        when(mockAdminDataService.getTickets(anyInt(), anyInt()))
                .thenReturn(tickets);

        Map<String, Object> result = adminViewService
                .listPassengers(journeyId, page);

        verify(mockAdminDataService, times(1)).getJourneyById(eq(journeyId));
        verify(mockAdminDataService, times(1)).maxPassengerPages(eq(journeyId));
        verify(mockAdminDataService, times(1))
                .getTickets(eq(journeyId), eq(page));
        verifyNoMoreInteractions(mockAdminDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(JOURNEY));
        assertSame(journey, result.get(JOURNEY));
        assertTrue(result.containsKey(TICKETS));
        assertSame(tickets, result.get(TICKETS));
        assertTrue(result.containsKey(NEXT_PAGE));
        assertEquals(2, result.get(NEXT_PAGE));
        assertFalse(result.containsKey(PREVIOUS_PAGE));

    }

    @Test
    void listPassengersMiddlePageTest() {
        int journeyId = 0;
        int page = 2;
        JourneyDto journey = new JourneyDto();
        List<TicketDto> tickets = new ArrayList<>();
        when(mockAdminDataService.getJourneyById(anyInt())).thenReturn(journey);
        when(mockAdminDataService.maxPassengerPages(anyInt())).thenReturn(3);
        when(mockAdminDataService.getTickets(anyInt(), anyInt()))
                .thenReturn(tickets);

        Map<String, Object> result = adminViewService
                .listPassengers(journeyId, page);

        verify(mockAdminDataService, times(1)).getJourneyById(eq(journeyId));
        verify(mockAdminDataService, times(1)).maxPassengerPages(eq(journeyId));
        verify(mockAdminDataService, times(1))
                .getTickets(eq(journeyId), eq(page));
        verifyNoMoreInteractions(mockAdminDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(JOURNEY));
        assertSame(journey, result.get(JOURNEY));
        assertTrue(result.containsKey(TICKETS));
        assertSame(tickets, result.get(TICKETS));
        assertTrue(result.containsKey(PREVIOUS_PAGE));
        assertEquals(1, result.get(PREVIOUS_PAGE));
        assertTrue(result.containsKey(NEXT_PAGE));
        assertEquals(3, result.get(NEXT_PAGE));

    }

    @Test
    void listPassengersLastPageTest() {
        int journeyId = 0;
        int page = 2;
        JourneyDto journey = new JourneyDto();
        List<TicketDto> tickets = new ArrayList<>();
        when(mockAdminDataService.getJourneyById(anyInt())).thenReturn(journey);
        when(mockAdminDataService.maxPassengerPages(anyInt())).thenReturn(2);
        when(mockAdminDataService.getTickets(anyInt(), anyInt()))
                .thenReturn(tickets);

        Map<String, Object> result = adminViewService
                .listPassengers(journeyId, page);

        verify(mockAdminDataService, times(1)).getJourneyById(eq(journeyId));
        verify(mockAdminDataService, times(1)).maxPassengerPages(eq(journeyId));
        verify(mockAdminDataService, times(1))
                .getTickets(eq(journeyId), eq(page));
        verifyNoMoreInteractions(mockAdminDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(JOURNEY));
        assertSame(journey, result.get(JOURNEY));
        assertTrue(result.containsKey(TICKETS));
        assertSame(tickets, result.get(TICKETS));
        assertTrue(result.containsKey(PREVIOUS_PAGE));
        assertEquals(1, result.get(PREVIOUS_PAGE));
        assertFalse(result.containsKey(NEXT_PAGE));
    }

    @Test
    void prepAdminPanelTest() {
        LineDto line = new LineDto();
        line.setId(0);
        List<LineDto> lines = Arrays.asList(line, line, line);
        List<TrainDto> trainModels = new ArrayList<>();
        float tariff = 0.5f;
        String today = LocalDate.now(mockClock)
                .format(DateTimeFormatter.ISO_LOCAL_DATE);
        RouteDto routeDto = new RouteDto();
        List<RouteDto> routes = Collections.singletonList(routeDto);
        List<RouteDto> expectedRoutes = Arrays
                .asList(routeDto, routeDto, routeDto);
        when(mockRouteDataService.getAllLines()).thenReturn(lines);
        when(mockRouteDataService.getAllRoutes(anyInt())).thenReturn(routes);
        when(mockAdminDataService.getAllTrainModels()).thenReturn(trainModels);
        when(mockAdminDataService.currentTariff()).thenReturn(tariff);

        Map<String, Object> result = adminViewService.prepAdminPanel();

        verify(mockRouteDataService, times(1)).getAllLines();
        verify(mockAdminDataService, times(1)).getAllTrainModels();
        verify(mockAdminDataService, times(1)).currentTariff();
        verify(mockRouteDataService, times(3)).getAllRoutes(eq(0));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyNoMoreInteractions(mockAdminDataService);

        assertTrue(result.containsKey(TODAY));
        assertEquals(today, result.get(TODAY));
        assertTrue(result.containsKey(TARIFF));
        assertEquals(tariff, result.get(TARIFF));
        assertTrue(result.containsKey(LINES));
        assertSame(lines, result.get(LINES));
        assertTrue(result.containsKey(TRAIN_MODELS));
        assertSame(trainModels, result.get(TRAIN_MODELS));
        assertTrue(result.containsKey(ROUTES));
        assertEquals(expectedRoutes, result.get(ROUTES));
    }

    @Test
    void delayNegativeTest() {
        adminViewService.delayJourney(0, -1);
        verifyZeroInteractions(mockAdminDataService);
        verifyZeroInteractions(mockRouteDataService);
    }

    @Test
    void delayZeroTest() {
        adminViewService.delayJourney(0, 0);
        verify(mockAdminDataService, times(1)).delayJourney(eq(0), eq(0));
        verifyNoMoreInteractions(mockAdminDataService);
        verifyZeroInteractions(mockRouteDataService);
    }

    @Test
    void delayPositiveTest() {
        adminViewService.delayJourney(0, 10);
        verify(mockAdminDataService, times(1)).delayJourney(eq(0), eq(10));
        verifyNoMoreInteractions(mockAdminDataService);
        verifyZeroInteractions(mockRouteDataService);
    }
}
