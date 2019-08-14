package ru.tsystems.sbb.services.view;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.RouteDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.services.data.AdminDataService;
import ru.tsystems.sbb.services.data.RouteDataService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class AdminViewServiceImplTest {

    @InjectMocks
    private AdminViewServiceImpl adminViewService;

    @Mock
    private RouteDataService mockRouteDataService;

    @Mock
    private AdminDataService mockAdminDataService;

    @BeforeAll
    static void setup(@Mock Authentication auth,
                      @Mock SecurityContext securityContext) {
        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn("testEmployee");
        SecurityContextHolder.setContext(securityContext);
    }

    @BeforeEach
    void initMocks(){
        MockitoAnnotations.initMocks(this);
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
    void newRouteStopPatternZeroRouteIdTest() {
        String routeNumber = "routeNumber";
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

        assertTrue(result.containsKey("routeNumber"));
        assertSame(routeNumber, result.get("routeNumber"));
        assertTrue(result.containsKey("line"));
        assertSame(line, result.get("line"));
        assertTrue(result.containsKey("routeStations"));
        assertSame(stations, result.get("routeStations"));
        assertFalse(result.containsKey("route"));
    }

    @Test
    void newRouteStopPatternNonZeroRouteIdTest() {
        String routeNumber = "routeNumber";
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

        assertTrue(result.containsKey("routeNumber"));
        assertSame(routeNumber, result.get("routeNumber"));
        assertTrue(result.containsKey("line"));
        assertSame(line, result.get("line"));
        assertTrue(result.containsKey("routeStations"));
        assertSame(stations, result.get("routeStations"));
        assertTrue(result.containsKey("route"));
        assertSame(route, result.get("route"));
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

        assertTrue(result.containsKey("today"));
        assertSame(date, result.get("today"));
        assertTrue(result.containsKey("previousDay"));
        assertEquals("2020-02-01", result.get("previousDay"));
        assertTrue(result.containsKey("nextDay"));
        assertEquals("2020-02-03", result.get("nextDay"));
        assertTrue(result.containsKey("journeys"));
        assertSame(journeys, result.get("journeys"));
        assertFalse(result.containsKey("previousPage"));
        assertFalse(result.containsKey("nextPage"));
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

        assertTrue(result.containsKey("today"));
        assertSame(date, result.get("today"));
        assertTrue(result.containsKey("previousDay"));
        assertEquals("2020-02-01", result.get("previousDay"));
        assertTrue(result.containsKey("nextDay"));
        assertEquals("2020-02-03", result.get("nextDay"));
        assertTrue(result.containsKey("journeys"));
        assertSame(journeys, result.get("journeys"));
        assertTrue(result.containsKey("nextPage"));
        assertEquals(2, result.get("nextPage"));
        assertFalse(result.containsKey("previousPage"));
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

        assertTrue(result.containsKey("today"));
        assertSame(date, result.get("today"));
        assertTrue(result.containsKey("previousDay"));
        assertEquals("2020-02-01", result.get("previousDay"));
        assertTrue(result.containsKey("nextDay"));
        assertEquals("2020-02-03", result.get("nextDay"));
        assertTrue(result.containsKey("journeys"));
        assertSame(journeys, result.get("journeys"));
        assertTrue(result.containsKey("nextPage"));
        assertEquals(3, result.get("nextPage"));
        assertTrue(result.containsKey("previousPage"));
        assertEquals(1, result.get("previousPage"));
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

        assertTrue(result.containsKey("today"));
        assertSame(date, result.get("today"));
        assertTrue(result.containsKey("previousDay"));
        assertEquals("2020-02-01", result.get("previousDay"));
        assertTrue(result.containsKey("nextDay"));
        assertEquals("2020-02-03", result.get("nextDay"));
        assertTrue(result.containsKey("journeys"));
        assertSame(journeys, result.get("journeys"));
        assertTrue(result.containsKey("previousPage"));
        assertEquals(1, result.get("previousPage"));
        assertFalse(result.containsKey("nextPage"));
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

        assertTrue(result.containsKey("journey"));
        assertSame(journey, result.get("journey"));
        assertTrue(result.containsKey("tickets"));
        assertSame(tickets, result.get("tickets"));
        assertFalse(result.containsKey("previousPage"));
        assertFalse(result.containsKey("nextPage"));
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

        assertTrue(result.containsKey("journey"));
        assertSame(journey, result.get("journey"));
        assertTrue(result.containsKey("tickets"));
        assertSame(tickets, result.get("tickets"));
        assertTrue(result.containsKey("nextPage"));
        assertEquals(2, result.get("nextPage"));
        assertFalse(result.containsKey("previousPage"));

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

        assertTrue(result.containsKey("journey"));
        assertSame(journey, result.get("journey"));
        assertTrue(result.containsKey("tickets"));
        assertSame(tickets, result.get("tickets"));
        assertTrue(result.containsKey("previousPage"));
        assertEquals(1, result.get("previousPage"));
        assertTrue(result.containsKey("nextPage"));
        assertEquals(3, result.get("nextPage"));

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

        assertTrue(result.containsKey("journey"));
        assertSame(journey, result.get("journey"));
        assertTrue(result.containsKey("tickets"));
        assertSame(tickets, result.get("tickets"));
        assertTrue(result.containsKey("previousPage"));
        assertEquals(1, result.get("previousPage"));
        assertFalse(result.containsKey("nextPage"));
    }
}
