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
import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.sbb.model.dto.SignUpDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;
import ru.tsystems.sbb.services.data.PassengerDataService;
import ru.tsystems.sbb.services.data.RouteDataService;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @AfterEach
    void resetMocks() {
        reset(mockRouteDataService);
        reset(mockPassengerDataService);
    }

    @Test
    void prepTicketSaleFailTest() {
        int journeyId = 0;
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        TicketOrderDto ticketOrder = new TicketOrderDto();
        ticketOrder.setStatus("testStatus");
        String expectedStatus = TICKET_PREP_FAIL + ticketOrder.getStatus();
        when(mockPassengerDataService.prepareTicketOrder(anyInt(),
                anyString(), anyString())).thenReturn(ticketOrder);

        Map<String, Object> result = passengerViewService
                .prepTicketSale(journeyId, stationFrom, stationTo);

        verify(mockPassengerDataService, times(1))
                .prepareTicketOrder(eq(journeyId), same(stationFrom),
                        same(stationTo));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("status"));
        assertEquals(expectedStatus, result.get("status"));
        assertFalse(result.containsKey("buyerDetails"));
        assertFalse(result.containsKey("ticketOrder"));
        assertFalse(result.containsKey("passenger"));
    }

    @Test
    void prepTicketSaleSuccessTest() {
        int journeyId = 0;
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        TicketOrderDto ticketOrder = new TicketOrderDto();

        when(mockPassengerDataService.prepareTicketOrder(anyInt(),
                anyString(), anyString())).thenReturn(ticketOrder);

        Map<String, Object> result = passengerViewService
                .prepTicketSale(journeyId, stationFrom, stationTo);

        verify(mockPassengerDataService, times(1))
                .prepareTicketOrder(eq(journeyId), same(stationFrom),
                        same(stationTo));
        verify(mockPassengerDataService, times(1))
                .getPassenger(eq("testUser"));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("buyerDetails"));
        assertEquals(new BuyerDetailsDto(), result.get("buyerDetails"));
        assertTrue(result.containsKey("ticketOrder"));
        assertSame(ticketOrder, result.get("ticketOrder"));
        assertFalse(result.containsKey("status"));
    }

    @Test
    void prepTicketsSaleFirstFailTest() {
        int firstJourneyId = 1;
        int secondJourneyId = 2;
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        String transfer = "transfer";
        TransferTicketOrderDto transferTickets = new TransferTicketOrderDto();
        transferTickets.setFirstTrain(new TicketOrderDto());
        transferTickets.getFirstTrain().setStatus("testStatus");
        String expectedStatus = TICKET_PREP_FAIL + transferTickets
                .getFirstTrain().getStatus();
        when(mockPassengerDataService.prepareTicketsOrder(anyInt(), anyInt(),
                anyString(), anyString(), anyString()))
                .thenReturn(transferTickets);

        Map<String, Object> result = passengerViewService
                .prepTicketsSale(firstJourneyId, secondJourneyId,
                        stationFrom, stationTo, transfer);

        verify(mockPassengerDataService, times(1))
                .prepareTicketsOrder(eq(firstJourneyId), eq(secondJourneyId),
                        same(stationFrom), same(stationTo), same(transfer));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("status"));
        assertEquals(expectedStatus, result.get("status"));
        assertFalse(result.containsKey("buyerDetails"));
        assertFalse(result.containsKey("transferTickets"));
        assertFalse(result.containsKey("passenger"));
    }

    @Test
    void prepTicketsSaleSecondFailTest() {
        int firstJourneyId = 1;
        int secondJourneyId = 2;
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        String transfer = "transfer";
        TransferTicketOrderDto transferTickets = new TransferTicketOrderDto();
        transferTickets.setFirstTrain(new TicketOrderDto());
        transferTickets.setSecondTrain(new TicketOrderDto());
        transferTickets.getSecondTrain().setStatus("testStatus");
        String expectedStatus = TICKET_PREP_FAIL + transferTickets
                .getSecondTrain().getStatus();
        when(mockPassengerDataService.prepareTicketsOrder(anyInt(), anyInt(),
                anyString(), anyString(), anyString()))
                .thenReturn(transferTickets);

        Map<String, Object> result = passengerViewService
                .prepTicketsSale(firstJourneyId, secondJourneyId,
                        stationFrom, stationTo, transfer);

        verify(mockPassengerDataService, times(1))
                .prepareTicketsOrder(eq(firstJourneyId), eq(secondJourneyId),
                        same(stationFrom), same(stationTo), same(transfer));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("status"));
        assertEquals(expectedStatus, result.get("status"));
        assertFalse(result.containsKey("buyerDetails"));
        assertFalse(result.containsKey("transferTickets"));
        assertFalse(result.containsKey("passenger"));
    }

    @Test
    void prepTicketsSaleSuccessTest() {
        int firstJourneyId = 1;
        int secondJourneyId = 2;
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        String transfer = "transfer";
        TransferTicketOrderDto transferTickets = new TransferTicketOrderDto();
        transferTickets.setFirstTrain(new TicketOrderDto());
        transferTickets.setSecondTrain(new TicketOrderDto());
        when(mockPassengerDataService.prepareTicketsOrder(anyInt(), anyInt(),
                anyString(), anyString(), anyString()))
                .thenReturn(transferTickets);

        Map<String, Object> result = passengerViewService
                .prepTicketsSale(firstJourneyId, secondJourneyId,
                        stationFrom, stationTo, transfer);

        verify(mockPassengerDataService, times(1))
                .prepareTicketsOrder(eq(firstJourneyId), eq(secondJourneyId),
                        same(stationFrom), same(stationTo), same(transfer));
        verify(mockPassengerDataService, times(1))
                .getPassenger(eq("testUser"));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("buyerDetails"));
        assertEquals(new BuyerDetailsDto(), result.get("buyerDetails"));
        assertTrue(result.containsKey("transferTickets"));
        assertSame(transferTickets, result.get("transferTickets"));
        assertFalse(result.containsKey("status"));
    }

    @Test
    void finalizeTicketSaleFailTest() {
        TicketOrderDto order = new TicketOrderDto();
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2002, 2, 20);
        List<StationDto> stations = new ArrayList<>();
        when(mockPassengerDataService.buyTicket(any(TicketOrderDto.class),
                anyString(), anyString(), any(LocalDate.class)))
                .thenReturn("fail");
        when(mockRouteDataService.allStations()).thenReturn(stations);

        Map<String, Object> result = passengerViewService
                .finalizeTicketSale(order, firstName, lastName, dateOfBirth);

        verify(mockRouteDataService, times(1)).allStations();
        verify(mockPassengerDataService, times(1)).buyTicket(same(order),
                same(firstName), same(lastName), same(dateOfBirth));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyNoMoreInteractions(mockPassengerDataService);

        assertTrue(result.containsKey("stations"));
        assertSame(stations, result.get("stations"));
        assertTrue(result.containsKey("status"));
        assertEquals(TICKET_FAIL + "fail", result.get("status"));
    }

    @Test
    void finalizeTicketSaleSuccessTest() {
        TicketOrderDto order = new TicketOrderDto();
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2002, 2, 20);
        List<StationDto> stations = new ArrayList<>();
        when(mockPassengerDataService.buyTicket(any(TicketOrderDto.class),
                anyString(), anyString(), any(LocalDate.class)))
                .thenReturn("success");
        when(mockRouteDataService.allStations()).thenReturn(stations);

        Map<String, Object> result = passengerViewService
                .finalizeTicketSale(order, firstName, lastName, dateOfBirth);

        verify(mockRouteDataService, times(1)).allStations();
        verify(mockPassengerDataService, times(1)).buyTicket(same(order),
                same(firstName), same(lastName), same(dateOfBirth));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyNoMoreInteractions(mockPassengerDataService);

        assertTrue(result.containsKey("stations"));
        assertSame(stations, result.get("stations"));
        assertTrue(result.containsKey("status"));
        assertEquals(TICKET_SUCCESS, result.get("status"));
    }

    @Test
    void finalizeTicketsSaleFailTest() {
        TransferTicketOrderDto order = new TransferTicketOrderDto();
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2002, 2, 20);
        List<StationDto> stations = new ArrayList<>();
        when(mockPassengerDataService
                .buyTickets(any(TransferTicketOrderDto.class),
                anyString(), anyString(), any(LocalDate.class)))
                .thenReturn("fail");
        when(mockRouteDataService.allStations()).thenReturn(stations);

        Map<String, Object> result = passengerViewService
                .finalizeTicketsSale(order, firstName, lastName, dateOfBirth);

        verify(mockRouteDataService, times(1)).allStations();
        verify(mockPassengerDataService, times(1)).buyTickets(same(order),
                same(firstName), same(lastName), same(dateOfBirth));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyNoMoreInteractions(mockPassengerDataService);

        assertTrue(result.containsKey("stations"));
        assertSame(stations, result.get("stations"));
        assertTrue(result.containsKey("status"));
        assertEquals(TICKET_FAIL + "fail", result.get("status"));
    }

    @Test
    void finalizeTicketsSaleSuccessTest() {
        TransferTicketOrderDto order = new TransferTicketOrderDto();
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2002, 2, 20);
        List<StationDto> stations = new ArrayList<>();
        when(mockPassengerDataService
                .buyTickets(any(TransferTicketOrderDto.class),
                        anyString(), anyString(), any(LocalDate.class)))
                .thenReturn("success");
        when(mockRouteDataService.allStations()).thenReturn(stations);

        Map<String, Object> result = passengerViewService
                .finalizeTicketsSale(order, firstName, lastName, dateOfBirth);

        verify(mockRouteDataService, times(1)).allStations();
        verify(mockPassengerDataService, times(1)).buyTickets(same(order),
                same(firstName), same(lastName), same(dateOfBirth));
        verifyNoMoreInteractions(mockRouteDataService);
        verifyNoMoreInteractions(mockPassengerDataService);

        assertTrue(result.containsKey("stations"));
        assertSame(stations, result.get("stations"));
        assertTrue(result.containsKey("status"));
        assertEquals(TICKET_SUCCESS, result.get("status"));
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
                .getUserTickets(eq("testUser"), eq(page));
        verify(mockPassengerDataService, times(1))
                .maxUserTicketPages(eq("testUser"));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("tickets"));
        assertSame(tickets, result.get("tickets"));
        assertFalse(result.containsKey("previousPage"));
        assertFalse(result.containsKey("nextPage"));
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
                .getUserTickets(eq("testUser"), eq(page));
        verify(mockPassengerDataService, times(1))
                .maxUserTicketPages(eq("testUser"));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("tickets"));
        assertSame(tickets, result.get("tickets"));
        assertTrue(result.containsKey("nextPage"));
        assertEquals(2, result.get("nextPage"));
        assertFalse(result.containsKey("previousPage"));
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
                .getUserTickets(eq("testUser"), eq(page));
        verify(mockPassengerDataService, times(1))
                .maxUserTicketPages(eq("testUser"));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("tickets"));
        assertSame(tickets, result.get("tickets"));
        assertTrue(result.containsKey("previousPage"));
        assertEquals(1, result.get("previousPage"));
        assertTrue(result.containsKey("nextPage"));
        assertEquals(3, result.get("nextPage"));
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
                .getUserTickets(eq("testUser"), eq(page));
        verify(mockPassengerDataService, times(1))
                .maxUserTicketPages(eq("testUser"));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("tickets"));
        assertSame(tickets, result.get("tickets"));
        assertTrue(result.containsKey("previousPage"));
        assertEquals(1, result.get("previousPage"));
        assertFalse(result.containsKey("nextPage"));
    }

    @Test
    void returnTicketFailTest() {
        int ticketId = 0;
        when(mockPassengerDataService.returnTicket(anyInt()))
                .thenReturn("fail");

        Map<String, Object> result = passengerViewService
                .returnTicket(ticketId);

        verify(mockPassengerDataService, times(1)).returnTicket(eq(ticketId));
        verify(mockPassengerDataService, times(1))
                .getUserTickets(eq("testUser"), eq(1));
        verify(mockPassengerDataService, times(1))
                .maxUserTicketPages(eq("testUser"));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("status"));
        assertEquals(TICKET_RETURN_FAIL + "fail", result.get("status"));
    }

    @Test
    void returnTicketSuccessTest() {
        int ticketId = 0;
        when(mockPassengerDataService.returnTicket(anyInt()))
                .thenReturn("success");

        Map<String, Object> result = passengerViewService
                .returnTicket(ticketId);

        verify(mockPassengerDataService, times(1)).returnTicket(eq(ticketId));
        verify(mockPassengerDataService, times(1))
                .getUserTickets(eq("testUser"), eq(1));
        verify(mockPassengerDataService, times(1))
                .maxUserTicketPages(eq("testUser"));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("status"));
        assertEquals(TICKET_RETURN_SUCCESS, result.get("status"));
    }

    @Test
    void prepBuyerInfoLoggedInTest() {
        PassengerDto passengerDto = new PassengerDto();
        when(mockPassengerDataService.getPassenger(anyString()))
                .thenReturn(passengerDto);

        Map<String, Object> result = passengerViewService.prepBuyerInfo();

        verify(mockPassengerDataService, times(1)).getPassenger(eq("testUser"));
        verifyNoMoreInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("passenger"));
        assertSame(passengerDto, result.get("passenger"));
        assertFalse(result.containsKey("signUpDto"));
    }

    @Test
    @WithAnonymousUser
    void prepBuyerInfoAnonymousTest() {
        Map<String, Object> result = passengerViewService.prepBuyerInfo();

        verifyZeroInteractions(mockPassengerDataService);
        verifyZeroInteractions(mockRouteDataService);

        assertTrue(result.containsKey("signUpDto"));
        assertEquals(new SignUpDto(), result.get("signUpDto"));
        assertFalse(result.containsKey("passenger"));
    }
}
