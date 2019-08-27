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
import ru.tsystems.sbb.model.dto.BuyerDetailsDto;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.sbb.model.dto.SignUpDto;
import ru.tsystems.dto.StationDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;
import ru.tsystems.sbb.services.data.PassengerDataService;
import ru.tsystems.sbb.services.data.RouteDataService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ViewServiceTestConfig.class},
    loader = AnnotationConfigContextLoader.class)
@WithMockUser(username = "testUser")
class PassengerViewServiceTest {

    @Autowired
    private PassengerViewService passengerViewService;

    @Autowired
    private RouteDataService mockRouteDataService;

    @Autowired
    private PassengerDataService mockPassengerDataService;

    private static final String TICKET_PREP_FAIL = "Couldn't prepare "
            + "ticket sale. ";
    private static final String TICKET_SUCCESS = "Ticket sale successful. "
            + "Thank you for traveling with us.";
    private static final String TICKET_FAIL = "Couldn't complete ticket sale. ";
    private static final String TICKET_RETURN_FAIL = "Couldn't return ticket. ";
    private static final String TICKET_RETURN_SUCCESS = "Ticket successfully "
            + "returned. Your refund will be processed shortly.";

    private static final String STATION_FROM = "stationFrom";
    private static final String STATION_TO = "stationTo";
    private static final String TRANSFER = "transfer";
    private static final String STATUS = "status";
    private static final String BUYER_DETAILS = "buyerDetails";
    private static final String TICKET_ORDER = "ticketOrder";
    private static final String TRANSFER_TICKETS = "transferTickets";
    private static final String PASSENGER = "passenger";
    private static final String FAIL_STATUS = "failStatus";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String STATIONS = "stations";
    private static final String SUCCESS = "success";
    private static final String TICKETS = "tickets";
    private static final String NEXT_PAGE = "nextPage";
    private static final String PREVIOUS_PAGE = "previousPage";
    private static final String USERNAME = "testUser";
    private static final String SIGN_UP_DTO = "signUpDto";

    @AfterEach
    void resetMocks() {
        reset(mockRouteDataService);
        reset(mockPassengerDataService);
    }

    @Test
    void prepTicketSaleFailTest() {
        int journeyId = 0;
        TicketOrderDto ticketOrder = new TicketOrderDto();
        ticketOrder.setStatus(FAIL_STATUS);
        String expectedStatus = TICKET_PREP_FAIL + FAIL_STATUS;
        when(mockPassengerDataService.prepareTicketOrder(anyInt(),
                anyString(), anyString())).thenReturn(ticketOrder);

        Map<String, Object> result = passengerViewService
                .prepTicketSale(journeyId, STATION_FROM, STATION_TO);

        verify(mockPassengerDataService, times(1))
                .prepareTicketOrder(eq(journeyId), same(STATION_FROM),
                        same(STATION_TO));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(STATUS));
        assertEquals(expectedStatus, result.get(STATUS));
        assertFalse(result.containsKey(BUYER_DETAILS));
        assertFalse(result.containsKey(TICKET_ORDER));
        assertFalse(result.containsKey(PASSENGER));
    }

    @Test
    void prepTicketSaleSuccessTest() {
        int journeyId = 0;
        TicketOrderDto ticketOrder = new TicketOrderDto();

        when(mockPassengerDataService.prepareTicketOrder(anyInt(),
                anyString(), anyString())).thenReturn(ticketOrder);

        Map<String, Object> result = passengerViewService
                .prepTicketSale(journeyId, STATION_FROM, STATION_TO);

        verify(mockPassengerDataService, times(1))
                .prepareTicketOrder(eq(journeyId), same(STATION_FROM),
                        same(STATION_TO));
        verify(mockPassengerDataService, times(1))
                .getPassenger(eq(USERNAME));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(BUYER_DETAILS));
        assertEquals(new BuyerDetailsDto(), result.get(BUYER_DETAILS));
        assertTrue(result.containsKey(TICKET_ORDER));
        assertSame(ticketOrder, result.get(TICKET_ORDER));
        assertFalse(result.containsKey(STATUS));
    }

    @Test
    void prepTicketsSaleFirstFailTest() {
        int firstJourneyId = 1;
        int secondJourneyId = 2;
        TransferTicketOrderDto transferTickets = new TransferTicketOrderDto();
        transferTickets.setFirstTrain(new TicketOrderDto());
        transferTickets.getFirstTrain().setStatus(FAIL_STATUS);
        String expectedStatus = TICKET_PREP_FAIL + FAIL_STATUS;
        when(mockPassengerDataService.prepareTicketsOrder(anyInt(), anyInt(),
                anyString(), anyString(), anyString()))
                .thenReturn(transferTickets);

        Map<String, Object> result = passengerViewService
                .prepTicketsSale(firstJourneyId, secondJourneyId,
                        STATION_FROM, STATION_TO, TRANSFER);

        verify(mockPassengerDataService, times(1))
                .prepareTicketsOrder(eq(firstJourneyId), eq(secondJourneyId),
                        same(STATION_FROM), same(STATION_TO), same(TRANSFER));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(STATUS));
        assertEquals(expectedStatus, result.get(STATUS));
        assertFalse(result.containsKey(BUYER_DETAILS));
        assertFalse(result.containsKey(TRANSFER_TICKETS));
        assertFalse(result.containsKey(PASSENGER));
    }

    @Test
    void prepTicketsSaleSecondFailTest() {
        int firstJourneyId = 1;
        int secondJourneyId = 2;
        TransferTicketOrderDto transferTickets = new TransferTicketOrderDto();
        transferTickets.setFirstTrain(new TicketOrderDto());
        transferTickets.setSecondTrain(new TicketOrderDto());
        transferTickets.getSecondTrain().setStatus(FAIL_STATUS);
        String expectedStatus = TICKET_PREP_FAIL + FAIL_STATUS;
        when(mockPassengerDataService.prepareTicketsOrder(anyInt(), anyInt(),
                anyString(), anyString(), anyString()))
                .thenReturn(transferTickets);

        Map<String, Object> result = passengerViewService
                .prepTicketsSale(firstJourneyId, secondJourneyId,
                        STATION_FROM, STATION_TO, TRANSFER);

        verify(mockPassengerDataService, times(1))
                .prepareTicketsOrder(eq(firstJourneyId), eq(secondJourneyId),
                        same(STATION_FROM), same(STATION_TO), same(TRANSFER));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(STATUS));
        assertEquals(expectedStatus, result.get(STATUS));
        assertFalse(result.containsKey(BUYER_DETAILS));
        assertFalse(result.containsKey(TRANSFER_TICKETS));
        assertFalse(result.containsKey(PASSENGER));
    }

    @Test
    void prepTicketsSaleSuccessTest() {
        int firstJourneyId = 1;
        int secondJourneyId = 2;
        TransferTicketOrderDto transferTickets = new TransferTicketOrderDto();
        transferTickets.setFirstTrain(new TicketOrderDto());
        transferTickets.setSecondTrain(new TicketOrderDto());
        when(mockPassengerDataService.prepareTicketsOrder(anyInt(), anyInt(),
                anyString(), anyString(), anyString()))
                .thenReturn(transferTickets);

        Map<String, Object> result = passengerViewService
                .prepTicketsSale(firstJourneyId, secondJourneyId,
                        STATION_FROM, STATION_TO, TRANSFER);

        verify(mockPassengerDataService, times(1))
                .prepareTicketsOrder(eq(firstJourneyId), eq(secondJourneyId),
                        same(STATION_FROM), same(STATION_TO), same(TRANSFER));
        verify(mockPassengerDataService, times(1))
                .getPassenger(eq(USERNAME));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(BUYER_DETAILS));
        assertEquals(new BuyerDetailsDto(), result.get(BUYER_DETAILS));
        assertTrue(result.containsKey(TRANSFER_TICKETS));
        assertSame(transferTickets, result.get(TRANSFER_TICKETS));
        assertFalse(result.containsKey(STATUS));
    }

    @Test
    void finalizeTicketSaleFailTest() {
        TicketOrderDto order = new TicketOrderDto();
        LocalDate dateOfBirth = LocalDate.of(2002, 2, 20);
        List<StationDto> stations = new ArrayList<>();
        when(mockPassengerDataService.buyTicket(any(TicketOrderDto.class),
                anyString(), anyString(), any(LocalDate.class)))
                .thenReturn(FAIL_STATUS);
        when(mockRouteDataService.allStations()).thenReturn(stations);

        Map<String, Object> result = passengerViewService
                .finalizeTicketSale(order, FIRST_NAME, LAST_NAME, dateOfBirth);

        verify(mockRouteDataService, times(1)).allStations();
        verify(mockPassengerDataService, times(1)).buyTicket(same(order),
                same(FIRST_NAME), same(LAST_NAME), same(dateOfBirth));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyNoMoreInteractions(mockPassengerDataService);

        assertTrue(result.containsKey(STATIONS));
        assertSame(stations, result.get(STATIONS));
        assertTrue(result.containsKey(STATUS));
        assertEquals(TICKET_FAIL + FAIL_STATUS, result.get(STATUS));
    }

    @Test
    void finalizeTicketSaleSuccessTest() {
        TicketOrderDto order = new TicketOrderDto();
        order.setJourney(new JourneyDto());
        LocalDate dateOfBirth = LocalDate.of(2002, 2, 20);
        List<StationDto> stations = new ArrayList<>();
        List<TicketDto> tickets = new ArrayList<>();
        when(mockPassengerDataService.buyTicket(any(TicketOrderDto.class),
                anyString(), anyString(), any(LocalDate.class)))
                .thenReturn(SUCCESS);
        when(mockRouteDataService.allStations()).thenReturn(stations);
        when(mockPassengerDataService.getPassengerTickets(anyInt(), anyString(),
                anyString(), any(LocalDate.class))).thenReturn(tickets);

        Map<String, Object> result = passengerViewService
                .finalizeTicketSale(order, FIRST_NAME, LAST_NAME, dateOfBirth);

        verify(mockRouteDataService, times(1)).allStations();
        verify(mockPassengerDataService, times(1)).buyTicket(same(order),
                same(FIRST_NAME), same(LAST_NAME), same(dateOfBirth));
        verify(mockPassengerDataService, times(1)).getPassengerTickets(eq(0),
                same(FIRST_NAME), same(LAST_NAME), same(dateOfBirth));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyNoMoreInteractions(mockPassengerDataService);

        assertTrue(result.containsKey(STATIONS));
        assertSame(stations, result.get(STATIONS));
        assertTrue(result.containsKey(STATUS));
        assertEquals(TICKET_SUCCESS, result.get(STATUS));
        assertTrue(result.containsKey(TICKETS));
        assertSame(tickets, result.get(TICKETS));
    }

    @Test
    void finalizeTicketsSaleFailTest() {
        TransferTicketOrderDto order = new TransferTicketOrderDto();
        LocalDate dateOfBirth = LocalDate.of(2002, 2, 20);
        List<StationDto> stations = new ArrayList<>();
        when(mockPassengerDataService
                .buyTickets(any(TransferTicketOrderDto.class),
                anyString(), anyString(), any(LocalDate.class)))
                .thenReturn(FAIL_STATUS);
        when(mockRouteDataService.allStations()).thenReturn(stations);

        Map<String, Object> result = passengerViewService
                .finalizeTicketsSale(order, FIRST_NAME, LAST_NAME, dateOfBirth);

        verify(mockRouteDataService, times(1)).allStations();
        verify(mockPassengerDataService, times(1)).buyTickets(same(order),
                same(FIRST_NAME), same(LAST_NAME), same(dateOfBirth));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyNoMoreInteractions(mockPassengerDataService);

        assertTrue(result.containsKey(STATIONS));
        assertSame(stations, result.get(STATIONS));
        assertTrue(result.containsKey(STATUS));
        assertEquals(TICKET_FAIL + FAIL_STATUS, result.get(STATUS));
    }

    @Test
    void finalizeTicketsSaleSuccessTest() {
        TransferTicketOrderDto order = new TransferTicketOrderDto();
        order.setFirstTrain(new TicketOrderDto());
        order.setSecondTrain(new TicketOrderDto());
        order.getFirstTrain().setJourney(new JourneyDto());
        order.getSecondTrain().setJourney(new JourneyDto());
        LocalDate dateOfBirth = LocalDate.of(2002, 2, 20);
        List<StationDto> stations = new ArrayList<>();
        TicketDto ticket = new TicketDto();
        List<TicketDto> tickets = Collections.singletonList(ticket);
        List<TicketDto> expected = Arrays.asList(ticket, ticket);
        when(mockPassengerDataService
                .buyTickets(any(TransferTicketOrderDto.class),
                        anyString(), anyString(), any(LocalDate.class)))
                .thenReturn(SUCCESS);
        when(mockRouteDataService.allStations()).thenReturn(stations);
        when(mockPassengerDataService.getPassengerTickets(anyInt(), anyString(),
                anyString(), any(LocalDate.class))).thenReturn(tickets);

        Map<String, Object> result = passengerViewService
                .finalizeTicketsSale(order, FIRST_NAME, LAST_NAME, dateOfBirth);

        verify(mockRouteDataService, times(1)).allStations();
        verify(mockPassengerDataService, times(1)).buyTickets(same(order),
                same(FIRST_NAME), same(LAST_NAME), same(dateOfBirth));
        verify(mockPassengerDataService, times(2)).getPassengerTickets(eq(0),
                same(FIRST_NAME), same(LAST_NAME), same(dateOfBirth));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyNoMoreInteractions(mockPassengerDataService);

        assertTrue(result.containsKey(STATIONS));
        assertSame(stations, result.get(STATIONS));
        assertTrue(result.containsKey(STATUS));
        assertEquals(TICKET_SUCCESS, result.get(STATUS));
        assertTrue(result.containsKey(TICKETS));
        assertEquals(expected, result.get(TICKETS));
    }

    @Test
    void getUserTicketsOnlyPageTest() {
        int page = 1;
        List<TicketDto> tickets = new ArrayList<>();
        when(mockPassengerDataService.getUserTickets(anyString(), anyInt()))
                .thenReturn(tickets);
        when(mockPassengerDataService.maxUserTicketPages(anyString()))
                .thenReturn(1);

        Map<String, Object> result = passengerViewService.getUserTickets(page);

        verify(mockPassengerDataService, times(1))
                .getUserTickets(eq(USERNAME), eq(page));
        verify(mockPassengerDataService, times(1))
                .maxUserTicketPages(eq(USERNAME));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(TICKETS));
        assertSame(tickets, result.get(TICKETS));
        assertFalse(result.containsKey(PREVIOUS_PAGE));
        assertFalse(result.containsKey(NEXT_PAGE));
    }

    @Test
    void getUserTicketsFirstPageTest() {
        int page = 1;
        List<TicketDto> tickets = new ArrayList<>();
        when(mockPassengerDataService.getUserTickets(anyString(), anyInt()))
                .thenReturn(tickets);
        when(mockPassengerDataService.maxUserTicketPages(anyString()))
                .thenReturn(2);

        Map<String, Object> result = passengerViewService.getUserTickets(page);

        verify(mockPassengerDataService, times(1))
                .getUserTickets(eq(USERNAME), eq(page));
        verify(mockPassengerDataService, times(1))
                .maxUserTicketPages(eq(USERNAME));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(TICKETS));
        assertSame(tickets, result.get(TICKETS));
        assertTrue(result.containsKey(NEXT_PAGE));
        assertEquals(2, result.get(NEXT_PAGE));
        assertFalse(result.containsKey(PREVIOUS_PAGE));
    }

    @Test
    void getUserTicketsMiddlePageTest() {
        int page = 2;
        List<TicketDto> tickets = new ArrayList<>();
        when(mockPassengerDataService.getUserTickets(anyString(), anyInt()))
                .thenReturn(tickets);
        when(mockPassengerDataService.maxUserTicketPages(anyString()))
                .thenReturn(3);

        Map<String, Object> result = passengerViewService.getUserTickets(page);

        verify(mockPassengerDataService, times(1))
                .getUserTickets(eq(USERNAME), eq(page));
        verify(mockPassengerDataService, times(1))
                .maxUserTicketPages(eq(USERNAME));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(TICKETS));
        assertSame(tickets, result.get(TICKETS));
        assertTrue(result.containsKey(PREVIOUS_PAGE));
        assertEquals(1, result.get(PREVIOUS_PAGE));
        assertTrue(result.containsKey(NEXT_PAGE));
        assertEquals(3, result.get(NEXT_PAGE));
    }

    @Test
    void getUserTicketsLastPageTest() {
        int page = 2;
        List<TicketDto> tickets = new ArrayList<>();
        when(mockPassengerDataService.getUserTickets(anyString(), anyInt()))
                .thenReturn(tickets);
        when(mockPassengerDataService.maxUserTicketPages(anyString()))
                .thenReturn(2);

        Map<String, Object> result = passengerViewService.getUserTickets(page);

        verify(mockPassengerDataService, times(1))
                .getUserTickets(eq(USERNAME), eq(page));
        verify(mockPassengerDataService, times(1))
                .maxUserTicketPages(eq(USERNAME));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(TICKETS));
        assertSame(tickets, result.get(TICKETS));
        assertTrue(result.containsKey(PREVIOUS_PAGE));
        assertEquals(1, result.get(PREVIOUS_PAGE));
        assertFalse(result.containsKey(NEXT_PAGE));
    }

    @Test
    void returnTicketFailTest() {
        int ticketId = 0;
        when(mockPassengerDataService.returnTicket(anyInt()))
                .thenReturn(FAIL_STATUS);

        Map<String, Object> result = passengerViewService
                .returnTicket(ticketId);

        verify(mockPassengerDataService, times(1)).returnTicket(eq(ticketId));
        verify(mockPassengerDataService, times(1))
                .getUserTickets(eq(USERNAME), eq(1));
        verify(mockPassengerDataService, times(1))
                .maxUserTicketPages(eq(USERNAME));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(STATUS));
        assertEquals(TICKET_RETURN_FAIL + FAIL_STATUS, result.get(STATUS));
    }

    @Test
    void returnTicketSuccessTest() {
        int ticketId = 0;
        when(mockPassengerDataService.returnTicket(anyInt()))
                .thenReturn(SUCCESS);

        Map<String, Object> result = passengerViewService
                .returnTicket(ticketId);

        verify(mockPassengerDataService, times(1)).returnTicket(eq(ticketId));
        verify(mockPassengerDataService, times(1))
                .getUserTickets(eq(USERNAME), eq(1));
        verify(mockPassengerDataService, times(1))
                .maxUserTicketPages(eq(USERNAME));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(STATUS));
        assertEquals(TICKET_RETURN_SUCCESS, result.get(STATUS));
    }

    @Test
    void prepBuyerInfoLoggedInTest() {
        PassengerDto passengerDto = new PassengerDto();
        when(mockPassengerDataService.getPassenger(anyString()))
                .thenReturn(passengerDto);

        Map<String, Object> result = passengerViewService.prepBuyerInfo();

        verify(mockPassengerDataService, times(1)).getPassenger(eq(USERNAME));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(PASSENGER));
        assertSame(passengerDto, result.get(PASSENGER));
        assertFalse(result.containsKey(SIGN_UP_DTO));
    }

    @Test
    @WithAnonymousUser
    void prepBuyerInfoAnonymousTest() {
        Map<String, Object> result = passengerViewService.prepBuyerInfo();

        verifyZeroInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey(SIGN_UP_DTO));
        assertEquals(new SignUpDto(), result.get(SIGN_UP_DTO));
        assertFalse(result.containsKey(PASSENGER));
    }
}
