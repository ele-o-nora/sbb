package ru.tsystems.sbb.services.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tsystems.sbb.model.dao.PassengerDao;
import ru.tsystems.sbb.model.dao.RouteDao;
import ru.tsystems.sbb.model.dao.ScheduleDao;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.ScheduledStopDto;
import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.Passenger;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.Ticket;
import ru.tsystems.sbb.model.entities.Train;
import ru.tsystems.sbb.model.mappers.EntityToDtoMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class PassengerDataServiceTest {

    @InjectMocks
    private PassengerDataServiceImpl passengerDataService;

    @Mock
    private EntityToDtoMapper mockMapper;

    @Mock
    private ScheduleDao mockScheduleDao;

    @Mock
    private PassengerDao mockPassengerDao;

    @Mock
    private RouteDao mockRouteDao;

    @Mock
    private Clock mockClock;

    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 2, 2);
    private static final String NO_TIME = "Not enough time before departure.";
    private static final String NO_SEATS = "No available seats.";
    private static final String SAME_PASSENGER = "You already have a ticket "
            + "for this journey.";

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
    void prepareTicketOrderInboundNotEnoughTimeTest() {
        int journeyId = 0;
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        Journey journey = new Journey();
        journey.setRoute(new Route());
        Line line =  new Line();
        journey.getRoute().setLine(line);
        Station firstStation = new Station();
        Station secondStation = new Station();
        ScheduledStop scheduledStop = new ScheduledStop();
        scheduledStop.setStation(new Station());
        scheduledStop.getStation().setName(stationFrom);
        scheduledStop.setDeparture(LocalDateTime.of(2020, 2, 2, 0, 5));
        journey.setStops(Collections.singletonList(scheduledStop));
        when(mockPassengerDao.getJourneyById(anyInt())).thenReturn(journey);
        when(mockScheduleDao.getStationByName(eq(stationFrom)))
                .thenReturn(firstStation);
        when(mockScheduleDao.getStationByName(eq(stationTo)))
                .thenReturn(secondStation);
        when(mockRouteDao.getStationOrder(same(line), same(firstStation)))
                .thenReturn(5);
        when(mockRouteDao.getStationOrder(same(line), same(secondStation)))
                .thenReturn(3);
        when(mockPassengerDao.getCurrentTariff()).thenReturn(0.5f);
        when(mockRouteDao.inboundDistance(any(Station.class),
                any(Station.class), any(Line.class))).thenReturn(90);
        when(mockMapper.convert(any(Journey.class)))
                .thenReturn(new JourneyDto());
        when(mockMapper.formatPrice(anyFloat())).thenReturn("formattedPrice");

        TicketOrderDto result = passengerDataService
                .prepareTicketOrder(journeyId, stationFrom, stationTo);

        verify(mockPassengerDao, times(2)).getJourneyById(eq(journeyId));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationFrom));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationTo));
        verify(mockRouteDao, times(1))
                .getStationOrder(same(line), same(firstStation));
        verify(mockRouteDao, times(1))
                .getStationOrder(same(line), same(secondStation));
        verify(mockRouteDao, times(1)).inboundDistance(same(firstStation),
                same(secondStation), same(line));
        verify(mockPassengerDao, times(1)).getCurrentTariff();
        verify(mockMapper, times(1)).convert(same(journey));
        verify(mockMapper, times(1)).formatPrice(eq(1.5f));
        verifyNoMoreInteractions(mockRouteDao);
        verifyNoMoreInteractions(mockPassengerDao);
        verifyNoMoreInteractions(mockScheduleDao);
        verifyNoMoreInteractions(mockMapper);

        assertNotNull(result.getStatus());
        assertEquals(NO_TIME, result.getStatus());
    }

    @Test
    void prepareTicketOrderInboundNotEnoughSeatsTest() {
        int journeyId = 0;
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        Journey journey = new Journey();
        journey.setRoute(new Route());
        Line line =  new Line();
        journey.getRoute().setLine(line);
        journey.setTrainType(new Train());
        journey.getTrainType().setSeats(10);
        Station firstStation = new Station();
        Station secondStation = new Station();
        ScheduledStop firstStop = new ScheduledStop();
        firstStop.setStation(new Station());
        firstStop.getStation().setName(stationFrom);
        firstStop.setDeparture(LocalDateTime.of(2020, 2, 2, 0, 20));
        ScheduledStop secondStop = new ScheduledStop();
        secondStop.setStation(new Station());
        secondStop.getStation().setName(stationTo);
        secondStop.setArrival(LocalDateTime.of(2020, 2, 2, 20, 20));
        journey.setStops(Arrays.asList(firstStop, secondStop));
        when(mockPassengerDao.getJourneyById(anyInt())).thenReturn(journey);
        when(mockScheduleDao.getStationByName(eq(stationFrom)))
                .thenReturn(firstStation);
        when(mockScheduleDao.getStationByName(eq(stationTo)))
                .thenReturn(secondStation);
        when(mockRouteDao.getStationOrder(same(line), same(firstStation)))
                .thenReturn(5);
        when(mockRouteDao.getStationOrder(same(line), same(secondStation)))
                .thenReturn(3);
        when(mockPassengerDao.getCurrentTariff()).thenReturn(0.5f);
        when(mockRouteDao.inboundDistance(any(Station.class),
                any(Station.class), any(Line.class))).thenReturn(90);
        when(mockMapper.convert(any(Journey.class)))
                .thenReturn(new JourneyDto());
        when(mockMapper.formatPrice(anyFloat())).thenReturn("formattedPrice");
        when(mockMapper.convert(any(ScheduledStop.class)))
                .thenReturn(new ScheduledStopDto());
        when(mockPassengerDao.currentTickets(any(Journey.class),
                any(ScheduledStop.class), any(ScheduledStop.class)))
                .thenReturn(10);

        TicketOrderDto result = passengerDataService
                .prepareTicketOrder(journeyId, stationFrom, stationTo);

        verify(mockPassengerDao, times(2)).getJourneyById(eq(journeyId));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationFrom));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationTo));
        verify(mockRouteDao, times(1))
                .getStationOrder(same(line), same(firstStation));
        verify(mockRouteDao, times(1))
                .getStationOrder(same(line), same(secondStation));
        verify(mockRouteDao, times(1)).inboundDistance(same(firstStation),
                same(secondStation), same(line));
        verify(mockPassengerDao, times(1)).getCurrentTariff();
        verify(mockMapper, times(1)).convert(same(journey));
        verify(mockMapper, times(1)).formatPrice(eq(1.5f));
        verify(mockMapper, times(1)).convert(same(firstStop));
        verify(mockPassengerDao, times(1)).currentTickets(same(journey),
                same(firstStop), same(secondStop));
        verifyNoMoreInteractions(mockRouteDao);
        verifyNoMoreInteractions(mockPassengerDao);
        verifyNoMoreInteractions(mockScheduleDao);
        verifyNoMoreInteractions(mockMapper);

        assertNotNull(result.getStatus());
        assertEquals(NO_SEATS, result.getStatus());
    }

    @Test
    void prepareTicketOrderOutboundSuccessTest() {
        int journeyId = 0;
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        Journey journey = new Journey();
        journey.setRoute(new Route());
        Line line =  new Line();
        journey.getRoute().setLine(line);
        journey.setTrainType(new Train());
        journey.getTrainType().setSeats(10);
        Station firstStation = new Station();
        Station secondStation = new Station();
        ScheduledStop firstStop = new ScheduledStop();
        firstStop.setStation(new Station());
        firstStop.getStation().setName(stationFrom);
        firstStop.setDeparture(LocalDateTime.of(2020, 2, 2, 0, 20));
        ScheduledStop secondStop = new ScheduledStop();
        secondStop.setStation(new Station());
        secondStop.getStation().setName(stationTo);
        secondStop.setArrival(LocalDateTime.of(2020, 2, 2, 20, 20));
        journey.setStops(Arrays.asList(firstStop, secondStop));
        JourneyDto journeyDto = new JourneyDto();
        ScheduledStopDto firstStopDto = new ScheduledStopDto();
        ScheduledStopDto secondStopDto = new ScheduledStopDto();
        when(mockPassengerDao.getJourneyById(anyInt())).thenReturn(journey);
        when(mockScheduleDao.getStationByName(eq(stationFrom)))
                .thenReturn(firstStation);
        when(mockScheduleDao.getStationByName(eq(stationTo)))
                .thenReturn(secondStation);
        when(mockRouteDao.getStationOrder(same(line), same(firstStation)))
                .thenReturn(3);
        when(mockRouteDao.getStationOrder(same(line), same(secondStation)))
                .thenReturn(5);
        when(mockPassengerDao.getCurrentTariff()).thenReturn(0.5f);
        when(mockRouteDao.outboundDistance(any(Station.class),
                any(Station.class), any(Line.class))).thenReturn(90);
        when(mockMapper.convert(any(Journey.class))).thenReturn(journeyDto);
        when(mockMapper.formatPrice(anyFloat())).thenReturn("formattedPrice");
        when(mockMapper.convert(same(firstStop))).thenReturn(firstStopDto);
        when(mockMapper.convert(same(secondStop))).thenReturn(secondStopDto);
        when(mockPassengerDao.currentTickets(any(Journey.class),
                any(ScheduledStop.class), any(ScheduledStop.class)))
                .thenReturn(15);

        TicketOrderDto result = passengerDataService
                .prepareTicketOrder(journeyId, stationFrom, stationTo);

        verify(mockPassengerDao, times(2)).getJourneyById(eq(journeyId));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationFrom));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationTo));
        verify(mockRouteDao, times(1))
                .getStationOrder(same(line), same(firstStation));
        verify(mockRouteDao, times(1))
                .getStationOrder(same(line), same(secondStation));
        verify(mockRouteDao, times(1)).outboundDistance(same(firstStation),
                same(secondStation), same(line));
        verify(mockPassengerDao, times(1)).getCurrentTariff();
        verify(mockMapper, times(1)).convert(same(journey));
        verify(mockMapper, times(1)).formatPrice(eq(1.5f));
        verify(mockMapper, times(1)).convert(same(firstStop));
        verify(mockPassengerDao, times(1)).currentTickets(same(journey),
                same(firstStop), same(secondStop));
        verify(mockMapper, times(1)).convert(same(secondStop));
        verifyNoMoreInteractions(mockRouteDao);
        verifyNoMoreInteractions(mockPassengerDao);
        verifyNoMoreInteractions(mockScheduleDao);
        verifyNoMoreInteractions(mockMapper);

        assertNull(result.getStatus());
        assertSame(journeyDto, result.getJourney());
        assertSame(firstStopDto, result.getOrigin());
        assertSame(secondStopDto, result.getDestination());
        assertEquals(1.5f, result.getPrice());
        assertEquals("formattedPrice", result.getFormattedPrice());
    }

    @Test
    void returnTicketSuccessTest() {
        int ticketId = 0;
        Ticket ticket = new Ticket();
        ticket.setFrom(new ScheduledStop());
        ticket.getFrom().setDeparture(LocalDateTime.of(2020, 2, 2, 20, 2));
        when(mockPassengerDao.getTicketById(anyInt())).thenReturn(ticket);

        String result = passengerDataService.returnTicket(ticketId);

        verify(mockPassengerDao, times(1)).getTicketById(eq(ticketId));
        verify(mockPassengerDao, times(1)).delete(same(ticket));
        verifyNoMoreInteractions(mockPassengerDao);
        verifyZeroInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertEquals("success", result);
    }

    @Test
    void returnTicketFailTest() {
        int ticketId = 0;
        Ticket ticket = new Ticket();
        ticket.setFrom(new ScheduledStop());
        ticket.getFrom().setDeparture(LocalDateTime.of(2020, 2, 2, 0, 2));
        when(mockPassengerDao.getTicketById(anyInt())).thenReturn(ticket);

        String result = passengerDataService.returnTicket(ticketId);

        verify(mockPassengerDao, times(1)).getTicketById(eq(ticketId));
        verifyNoMoreInteractions(mockPassengerDao);
        verifyZeroInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertEquals(NO_TIME, result);
    }

    @Test
    void buyTicketNoTimeTest() {
        TicketOrderDto ticketOrder = new TicketOrderDto();
        ticketOrder.setJourney(new JourneyDto());
        ticketOrder.setOrigin(new ScheduledStopDto());
        ticketOrder.getOrigin().setId(1);
        ticketOrder.setDestination(new ScheduledStopDto());
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2000, 2, 20);
        ScheduledStop firstStop = new ScheduledStop();
        firstStop.setDeparture(LocalDateTime.of(2020, 2, 2, 0, 5));
        when(mockPassengerDao.getStopById(eq(1))).thenReturn(firstStop);

        String result = passengerDataService.buyTicket(ticketOrder, firstName,
                lastName, dateOfBirth);

        verify(mockPassengerDao, times(1)).getJourneyById(eq(0));
        verify(mockPassengerDao, times(1)).getStopById(eq(1));
        verify(mockPassengerDao, times(1)).getStopById(eq(0));
        verify(mockPassengerDao, times(1)).getPassengerByInfo(same(firstName),
                same(lastName), same(dateOfBirth));
        verifyNoMoreInteractions(mockPassengerDao);
        verifyZeroInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertEquals(NO_TIME, result);
    }

    @Test
    void buyTicketSamePassengerTest() {
        TicketOrderDto ticketOrder = new TicketOrderDto();
        ticketOrder.setJourney(new JourneyDto());
        ticketOrder.setOrigin(new ScheduledStopDto());
        ticketOrder.getOrigin().setId(1);
        ticketOrder.setDestination(new ScheduledStopDto());
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2000, 2, 20);
        Journey journey = new Journey();
        ScheduledStop firstStop = new ScheduledStop();
        firstStop.setDeparture(LocalDateTime.of(2020, 2, 2, 2, 0));
        Passenger passenger = new Passenger();
        List<Ticket> tickets = Collections.singletonList(new Ticket());
        when(mockPassengerDao.getJourneyById(anyInt())).thenReturn(journey);
        when(mockPassengerDao.getStopById(eq(1))).thenReturn(firstStop);
        when(mockPassengerDao.getPassengerByInfo(anyString(), anyString(),
                any(LocalDate.class))).thenReturn(passenger);
        when(mockPassengerDao.getTickets(any(Journey.class),
                any(Passenger.class))).thenReturn(tickets);

        String result = passengerDataService.buyTicket(ticketOrder, firstName,
                lastName, dateOfBirth);

        verify(mockPassengerDao, times(1)).getJourneyById(eq(0));
        verify(mockPassengerDao, times(1)).getStopById(eq(1));
        verify(mockPassengerDao, times(1)).getStopById(eq(0));
        verify(mockPassengerDao, times(1)).getPassengerByInfo(same(firstName),
                same(lastName), same(dateOfBirth));
        verify(mockPassengerDao, times(1))
                .getTickets(same(journey), same(passenger));
        verifyNoMoreInteractions(mockPassengerDao);
        verifyZeroInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertEquals(SAME_PASSENGER, result);
    }

    @Test
    void buyTicketNoSeatsTest() {
        TicketOrderDto ticketOrder = new TicketOrderDto();
        ticketOrder.setJourney(new JourneyDto());
        ticketOrder.setOrigin(new ScheduledStopDto());
        ticketOrder.getOrigin().setId(1);
        ticketOrder.setDestination(new ScheduledStopDto());
        ticketOrder.getDestination().setId(2);
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2000, 2, 20);
        Journey journey = new Journey();
        journey.setTrainType(new Train());
        journey.getTrainType().setSeats(10);
        ScheduledStop firstStop = new ScheduledStop();
        firstStop.setDeparture(LocalDateTime.of(2020, 2, 2, 2, 0));
        ScheduledStop secondStop = new ScheduledStop();
        Passenger passenger = new Passenger();
        when(mockPassengerDao.getJourneyById(anyInt())).thenReturn(journey);
        when(mockPassengerDao.getStopById(eq(1))).thenReturn(firstStop);
        when(mockPassengerDao.getStopById(eq(2))).thenReturn(secondStop);
        when(mockPassengerDao.getPassengerByInfo(anyString(), anyString(),
                any(LocalDate.class))).thenReturn(passenger);
        when(mockPassengerDao.getTickets(any(Journey.class),
                any(Passenger.class))).thenReturn(new ArrayList<>());
        when(mockPassengerDao.currentTickets(any(Journey.class),
                any(ScheduledStop.class), any(ScheduledStop.class)))
                .thenReturn(10);

        String result = passengerDataService.buyTicket(ticketOrder, firstName,
                lastName, dateOfBirth);

        verify(mockPassengerDao, times(1)).getJourneyById(eq(0));
        verify(mockPassengerDao, times(1)).getStopById(eq(1));
        verify(mockPassengerDao, times(1)).getStopById(eq(2));
        verify(mockPassengerDao, times(1)).getPassengerByInfo(same(firstName),
                same(lastName), same(dateOfBirth));
        verify(mockPassengerDao, times(1))
                .getTickets(same(journey), same(passenger));
        verify(mockPassengerDao, times(1)).currentTickets(same(journey),
                same(firstStop), same(secondStop));
        verifyNoMoreInteractions(mockPassengerDao);
        verifyZeroInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertEquals(NO_SEATS, result);
    }

    @Test
    void buyTicketSuccessTest() {
        TicketOrderDto ticketOrder = new TicketOrderDto();
        ticketOrder.setJourney(new JourneyDto());
        ticketOrder.setOrigin(new ScheduledStopDto());
        ticketOrder.getOrigin().setId(1);
        ticketOrder.setDestination(new ScheduledStopDto());
        ticketOrder.getDestination().setId(2);
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2000, 2, 20);
        Journey journey = new Journey();
        journey.setTrainType(new Train());
        journey.getTrainType().setSeats(10);
        ScheduledStop firstStop = new ScheduledStop();
        firstStop.setDeparture(LocalDateTime.of(2020, 2, 2, 2, 0));
        ScheduledStop secondStop = new ScheduledStop();
        Passenger passenger = new Passenger();
        when(mockPassengerDao.getJourneyById(anyInt())).thenReturn(journey);
        when(mockPassengerDao.getStopById(eq(1))).thenReturn(firstStop);
        when(mockPassengerDao.getStopById(eq(2))).thenReturn(secondStop);
        when(mockPassengerDao.getPassengerByInfo(anyString(), anyString(),
                any(LocalDate.class))).thenReturn(passenger);
        when(mockPassengerDao.getTickets(any(Journey.class),
                any(Passenger.class))).thenReturn(new ArrayList<>());
        when(mockPassengerDao.currentTickets(any(Journey.class),
                any(ScheduledStop.class), any(ScheduledStop.class)))
                .thenReturn(5);

        String result = passengerDataService.buyTicket(ticketOrder, firstName,
                lastName, dateOfBirth);

        verify(mockPassengerDao, times(1)).getJourneyById(eq(0));
        verify(mockPassengerDao, times(1)).getStopById(eq(1));
        verify(mockPassengerDao, times(1)).getStopById(eq(2));
        verify(mockPassengerDao, times(1)).getPassengerByInfo(same(firstName),
                same(lastName), same(dateOfBirth));
        verify(mockPassengerDao, times(1))
                .getTickets(same(journey), same(passenger));
        verify(mockPassengerDao, times(1)).currentTickets(same(journey),
                same(firstStop), same(secondStop));
        verify(mockPassengerDao, times(1)).add(any(Ticket.class));
        verifyNoMoreInteractions(mockPassengerDao);
        verifyZeroInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertEquals("success", result);
    }

    @Test
    void buyTicketsNoTimeTest() {
        TransferTicketOrderDto ticketOrder = new TransferTicketOrderDto();
        ticketOrder.setFirstTrain(new TicketOrderDto());
        ticketOrder.setSecondTrain(new TicketOrderDto());
        ticketOrder.getFirstTrain().setJourney(new JourneyDto());
        ticketOrder.getSecondTrain().setJourney(new JourneyDto());
        ticketOrder.getFirstTrain().setOrigin(new ScheduledStopDto());
        ticketOrder.getFirstTrain().getOrigin().setId(1);
        ticketOrder.getFirstTrain().setDestination(new ScheduledStopDto());
        ticketOrder.getSecondTrain().setOrigin(new ScheduledStopDto());
        ticketOrder.getSecondTrain().setDestination(new ScheduledStopDto());
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2000, 2, 20);
        ScheduledStop firstStop = new ScheduledStop();
        firstStop.setDeparture(LocalDateTime.of(2020, 2, 2, 0, 5));
        when(mockPassengerDao.getStopById(eq(1))).thenReturn(firstStop);

        String result = passengerDataService.buyTickets(ticketOrder, firstName,
                lastName, dateOfBirth);

        verify(mockPassengerDao, times(2)).getJourneyById(eq(0));
        verify(mockPassengerDao, times(1)).getStopById(eq(1));
        verify(mockPassengerDao, times(3)).getStopById(eq(0));
        verify(mockPassengerDao, times(1)).getPassengerByInfo(same(firstName),
                same(lastName), same(dateOfBirth));
        verifyNoMoreInteractions(mockPassengerDao);
        verifyZeroInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertEquals(NO_TIME, result);
    }

    @Test
    void buyTicketsSamePassengerFirstTest() {
        TransferTicketOrderDto ticketOrder = new TransferTicketOrderDto();
        ticketOrder.setFirstTrain(new TicketOrderDto());
        ticketOrder.setSecondTrain(new TicketOrderDto());
        ticketOrder.getFirstTrain().setJourney(new JourneyDto());
        ticketOrder.getFirstTrain().getJourney().setId(1);
        ticketOrder.getSecondTrain().setJourney(new JourneyDto());
        ticketOrder.getFirstTrain().setOrigin(new ScheduledStopDto());
        ticketOrder.getFirstTrain().getOrigin().setId(1);
        ticketOrder.getFirstTrain().setDestination(new ScheduledStopDto());
        ticketOrder.getSecondTrain().setOrigin(new ScheduledStopDto());
        ticketOrder.getSecondTrain().setDestination(new ScheduledStopDto());
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2000, 2, 20);
        Journey firstJourney = new Journey();
        ScheduledStop firstStop = new ScheduledStop();
        firstStop.setDeparture(LocalDateTime.of(2020, 2, 2, 0, 20));
        Passenger passenger = new Passenger();
        List<Ticket> tickets = Collections.singletonList(new Ticket());
        when(mockPassengerDao.getJourneyById(eq(1))).thenReturn(firstJourney);
        when(mockPassengerDao.getStopById(eq(1))).thenReturn(firstStop);
        when(mockPassengerDao.getPassengerByInfo(anyString(), anyString(),
                any(LocalDate.class))).thenReturn(passenger);
        when(mockPassengerDao.getTickets(any(Journey.class),
                any(Passenger.class))).thenReturn(tickets);

        String result = passengerDataService.buyTickets(ticketOrder, firstName,
                lastName, dateOfBirth);

        verify(mockPassengerDao, times(1)).getJourneyById(eq(1));
        verify(mockPassengerDao, times(1)).getJourneyById(eq(0));
        verify(mockPassengerDao, times(1)).getStopById(eq(1));
        verify(mockPassengerDao, times(3)).getStopById(eq(0));
        verify(mockPassengerDao, times(1)).getPassengerByInfo(same(firstName),
                same(lastName), same(dateOfBirth));
        verify(mockPassengerDao, times(1))
                .getTickets(same(firstJourney), same(passenger));
        verifyNoMoreInteractions(mockPassengerDao);
        verifyZeroInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertEquals(SAME_PASSENGER, result);
    }

    @Test
    void buyTicketsSamePassengerSecondTest() {
        TransferTicketOrderDto ticketOrder = new TransferTicketOrderDto();
        ticketOrder.setFirstTrain(new TicketOrderDto());
        ticketOrder.setSecondTrain(new TicketOrderDto());
        ticketOrder.getFirstTrain().setJourney(new JourneyDto());
        ticketOrder.getFirstTrain().getJourney().setId(1);
        ticketOrder.getSecondTrain().setJourney(new JourneyDto());
        ticketOrder.getSecondTrain().getJourney().setId(2);
        ticketOrder.getFirstTrain().setOrigin(new ScheduledStopDto());
        ticketOrder.getFirstTrain().getOrigin().setId(1);
        ticketOrder.getFirstTrain().setDestination(new ScheduledStopDto());
        ticketOrder.getSecondTrain().setOrigin(new ScheduledStopDto());
        ticketOrder.getSecondTrain().setDestination(new ScheduledStopDto());
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2000, 2, 20);
        Journey firstJourney = new Journey();
        Journey secondJourney = new Journey();
        ScheduledStop firstStop = new ScheduledStop();
        firstStop.setDeparture(LocalDateTime.of(2020, 2, 2, 0, 20));
        Passenger passenger = new Passenger();
        List<Ticket> tickets = Collections.singletonList(new Ticket());
        when(mockPassengerDao.getJourneyById(eq(1))).thenReturn(firstJourney);
        when(mockPassengerDao.getJourneyById(eq(2))).thenReturn(secondJourney);
        when(mockPassengerDao.getStopById(eq(1))).thenReturn(firstStop);
        when(mockPassengerDao.getPassengerByInfo(anyString(), anyString(),
                any(LocalDate.class))).thenReturn(passenger);
        when(mockPassengerDao.getTickets(same(firstJourney),
                any(Passenger.class))).thenReturn(new ArrayList<>());
        when(mockPassengerDao.getTickets(same(secondJourney),
                any(Passenger.class))).thenReturn(tickets);

        String result = passengerDataService.buyTickets(ticketOrder, firstName,
                lastName, dateOfBirth);

        verify(mockPassengerDao, times(1)).getJourneyById(eq(1));
        verify(mockPassengerDao, times(1)).getJourneyById(eq(2));
        verify(mockPassengerDao, times(1)).getStopById(eq(1));
        verify(mockPassengerDao, times(3)).getStopById(eq(0));
        verify(mockPassengerDao, times(1)).getPassengerByInfo(same(firstName),
                same(lastName), same(dateOfBirth));
        verify(mockPassengerDao, times(1))
                .getTickets(same(firstJourney), same(passenger));
        verify(mockPassengerDao, times(1))
                .getTickets(same(secondJourney), same(passenger));
        verifyNoMoreInteractions(mockPassengerDao);
        verifyZeroInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertEquals(SAME_PASSENGER, result);
    }

    @Test
    void buyTicketsNoSeatsFirstTest() {
        TransferTicketOrderDto ticketOrder = new TransferTicketOrderDto();
        ticketOrder.setFirstTrain(new TicketOrderDto());
        ticketOrder.setSecondTrain(new TicketOrderDto());
        ticketOrder.getFirstTrain().setJourney(new JourneyDto());
        ticketOrder.getFirstTrain().getJourney().setId(1);
        ticketOrder.getSecondTrain().setJourney(new JourneyDto());
        ticketOrder.getSecondTrain().getJourney().setId(2);
        ticketOrder.getFirstTrain().setOrigin(new ScheduledStopDto());
        ticketOrder.getFirstTrain().getOrigin().setId(1);
        ticketOrder.getFirstTrain().setDestination(new ScheduledStopDto());
        ticketOrder.getFirstTrain().getDestination().setId(2);
        ticketOrder.getSecondTrain().setOrigin(new ScheduledStopDto());
        ticketOrder.getSecondTrain().setDestination(new ScheduledStopDto());
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2000, 2, 20);
        Journey firstJourney = new Journey();
        firstJourney.setTrainType(new Train());
        firstJourney.getTrainType().setSeats(10);
        Journey secondJourney = new Journey();
        ScheduledStop firstStop = new ScheduledStop();
        firstStop.setDeparture(LocalDateTime.of(2020, 2, 2, 0, 20));
        ScheduledStop secondStop = new ScheduledStop();
        Passenger passenger = new Passenger();
        when(mockPassengerDao.getJourneyById(eq(1))).thenReturn(firstJourney);
        when(mockPassengerDao.getJourneyById(eq(2))).thenReturn(secondJourney);
        when(mockPassengerDao.getStopById(eq(1))).thenReturn(firstStop);
        when(mockPassengerDao.getStopById(eq(2))).thenReturn(secondStop);
        when(mockPassengerDao.getPassengerByInfo(anyString(), anyString(),
                any(LocalDate.class))).thenReturn(passenger);
        when(mockPassengerDao.getTickets(any(Journey.class),
                any(Passenger.class))).thenReturn(new ArrayList<>());
        when(mockPassengerDao.currentTickets(any(Journey.class),
                any(ScheduledStop.class), any(ScheduledStop.class)))
                .thenReturn(10);

        String result = passengerDataService.buyTickets(ticketOrder, firstName,
                lastName, dateOfBirth);

        verify(mockPassengerDao, times(1)).getJourneyById(eq(1));
        verify(mockPassengerDao, times(1)).getJourneyById(eq(2));
        verify(mockPassengerDao, times(1)).getStopById(eq(1));
        verify(mockPassengerDao, times(1)).getStopById(eq(2));
        verify(mockPassengerDao, times(2)).getStopById(eq(0));
        verify(mockPassengerDao, times(1)).getPassengerByInfo(same(firstName),
                same(lastName), same(dateOfBirth));
        verify(mockPassengerDao, times(1))
                .getTickets(same(firstJourney), same(passenger));
        verify(mockPassengerDao, times(1))
                .getTickets(same(secondJourney), same(passenger));
        verify(mockPassengerDao, times(1)).currentTickets(same(firstJourney),
                same(firstStop), same(secondStop));
        verifyNoMoreInteractions(mockPassengerDao);
        verifyZeroInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertEquals(NO_SEATS, result);
    }

    @Test
    void buyTicketsNoSeatsSecondTest() {
        TransferTicketOrderDto ticketOrder = new TransferTicketOrderDto();
        ticketOrder.setFirstTrain(new TicketOrderDto());
        ticketOrder.setSecondTrain(new TicketOrderDto());
        ticketOrder.getFirstTrain().setJourney(new JourneyDto());
        ticketOrder.getFirstTrain().getJourney().setId(1);
        ticketOrder.getSecondTrain().setJourney(new JourneyDto());
        ticketOrder.getSecondTrain().getJourney().setId(2);
        ticketOrder.getFirstTrain().setOrigin(new ScheduledStopDto());
        ticketOrder.getFirstTrain().getOrigin().setId(1);
        ticketOrder.getFirstTrain().setDestination(new ScheduledStopDto());
        ticketOrder.getFirstTrain().getDestination().setId(2);
        ticketOrder.getSecondTrain().setOrigin(new ScheduledStopDto());
        ticketOrder.getSecondTrain().getOrigin().setId(3);
        ticketOrder.getSecondTrain().setDestination(new ScheduledStopDto());
        ticketOrder.getSecondTrain().getDestination().setId(4);
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2000, 2, 20);
        Journey firstJourney = new Journey();
        firstJourney.setTrainType(new Train());
        firstJourney.getTrainType().setSeats(10);
        Journey secondJourney = new Journey();
        secondJourney.setTrainType(new Train());
        secondJourney.getTrainType().setSeats(10);
        ScheduledStop firstStop = new ScheduledStop();
        firstStop.setDeparture(LocalDateTime.of(2020, 2, 2, 0, 20));
        ScheduledStop secondStop = new ScheduledStop();
        ScheduledStop thirdStop = new ScheduledStop();
        ScheduledStop fourthStop = new ScheduledStop();
        Passenger passenger = new Passenger();
        when(mockPassengerDao.getJourneyById(eq(1))).thenReturn(firstJourney);
        when(mockPassengerDao.getJourneyById(eq(2))).thenReturn(secondJourney);
        when(mockPassengerDao.getStopById(eq(1))).thenReturn(firstStop);
        when(mockPassengerDao.getStopById(eq(2))).thenReturn(secondStop);
        when(mockPassengerDao.getStopById(eq(3))).thenReturn(thirdStop);
        when(mockPassengerDao.getStopById(eq(4))).thenReturn(fourthStop);
        when(mockPassengerDao.getPassengerByInfo(anyString(), anyString(),
                any(LocalDate.class))).thenReturn(passenger);
        when(mockPassengerDao.getTickets(any(Journey.class),
                any(Passenger.class))).thenReturn(new ArrayList<>());
        when(mockPassengerDao.currentTickets(same(firstJourney),
                any(ScheduledStop.class), any(ScheduledStop.class)))
                .thenReturn(5);
        when(mockPassengerDao.currentTickets(same(secondJourney),
                any(ScheduledStop.class), any(ScheduledStop.class)))
                .thenReturn(10);

        String result = passengerDataService.buyTickets(ticketOrder, firstName,
                lastName, dateOfBirth);

        verify(mockPassengerDao, times(1)).getJourneyById(eq(1));
        verify(mockPassengerDao, times(1)).getJourneyById(eq(2));
        verify(mockPassengerDao, times(1)).getStopById(eq(1));
        verify(mockPassengerDao, times(1)).getStopById(eq(2));
        verify(mockPassengerDao, times(1)).getStopById(eq(3));
        verify(mockPassengerDao, times(1)).getStopById(eq(4));
        verify(mockPassengerDao, times(1)).getPassengerByInfo(same(firstName),
                same(lastName), same(dateOfBirth));
        verify(mockPassengerDao, times(1)).getTickets(same(firstJourney),
                same(passenger));
        verify(mockPassengerDao, times(1)).getTickets(same(secondJourney),
                same(passenger));
        verify(mockPassengerDao, times(1)).currentTickets(same(firstJourney),
                same(firstStop), same(secondStop));
        verify(mockPassengerDao, times(1)).currentTickets(same(secondJourney),
                same(thirdStop), same(fourthStop));
        verifyNoMoreInteractions(mockPassengerDao);
        verifyZeroInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertEquals(NO_SEATS, result);
    }

    @Test
    void buyTicketsSuccessTest() {
        TransferTicketOrderDto ticketOrder = new TransferTicketOrderDto();
        ticketOrder.setFirstTrain(new TicketOrderDto());
        ticketOrder.setSecondTrain(new TicketOrderDto());
        ticketOrder.getFirstTrain().setJourney(new JourneyDto());
        ticketOrder.getFirstTrain().getJourney().setId(1);
        ticketOrder.getSecondTrain().setJourney(new JourneyDto());
        ticketOrder.getSecondTrain().getJourney().setId(2);
        ticketOrder.getFirstTrain().setOrigin(new ScheduledStopDto());
        ticketOrder.getFirstTrain().getOrigin().setId(1);
        ticketOrder.getFirstTrain().setDestination(new ScheduledStopDto());
        ticketOrder.getFirstTrain().getDestination().setId(2);
        ticketOrder.getSecondTrain().setOrigin(new ScheduledStopDto());
        ticketOrder.getSecondTrain().getOrigin().setId(3);
        ticketOrder.getSecondTrain().setDestination(new ScheduledStopDto());
        ticketOrder.getSecondTrain().getDestination().setId(4);
        String firstName = "firstName";
        String lastName = "lastName";
        LocalDate dateOfBirth = LocalDate.of(2000, 2, 20);
        Journey firstJourney = new Journey();
        firstJourney.setTrainType(new Train());
        firstJourney.getTrainType().setSeats(10);
        Journey secondJourney = new Journey();
        secondJourney.setTrainType(new Train());
        secondJourney.getTrainType().setSeats(10);
        ScheduledStop firstStop = new ScheduledStop();
        firstStop.setDeparture(LocalDateTime.of(2020, 2, 2, 0, 20));
        ScheduledStop secondStop = new ScheduledStop();
        ScheduledStop thirdStop = new ScheduledStop();
        ScheduledStop fourthStop = new ScheduledStop();
        Passenger passenger = new Passenger();
        when(mockPassengerDao.getJourneyById(eq(1))).thenReturn(firstJourney);
        when(mockPassengerDao.getJourneyById(eq(2))).thenReturn(secondJourney);
        when(mockPassengerDao.getStopById(eq(1))).thenReturn(firstStop);
        when(mockPassengerDao.getStopById(eq(2))).thenReturn(secondStop);
        when(mockPassengerDao.getStopById(eq(3))).thenReturn(thirdStop);
        when(mockPassengerDao.getStopById(eq(4))).thenReturn(fourthStop);
        when(mockPassengerDao.getPassengerByInfo(anyString(), anyString(),
                any(LocalDate.class))).thenReturn(passenger);
        when(mockPassengerDao.getTickets(any(Journey.class),
                any(Passenger.class))).thenReturn(new ArrayList<>());
        when(mockPassengerDao.currentTickets(any(Journey.class),
                any(ScheduledStop.class), any(ScheduledStop.class)))
                .thenReturn(5);

        String result = passengerDataService.buyTickets(ticketOrder, firstName,
                lastName, dateOfBirth);

        verify(mockPassengerDao, times(1)).getJourneyById(eq(1));
        verify(mockPassengerDao, times(1)).getJourneyById(eq(2));
        verify(mockPassengerDao, times(1)).getStopById(eq(1));
        verify(mockPassengerDao, times(1)).getStopById(eq(2));
        verify(mockPassengerDao, times(1)).getStopById(eq(3));
        verify(mockPassengerDao, times(1)).getStopById(eq(4));
        verify(mockPassengerDao, times(1)).getPassengerByInfo(same(firstName),
                same(lastName), same(dateOfBirth));
        verify(mockPassengerDao, times(1)).getTickets(same(firstJourney),
                same(passenger));
        verify(mockPassengerDao, times(1)).getTickets(same(secondJourney),
                same(passenger));
        verify(mockPassengerDao, times(1)).currentTickets(same(firstJourney),
                same(firstStop), same(secondStop));
        verify(mockPassengerDao, times(1)).currentTickets(same(secondJourney),
                same(thirdStop), same(fourthStop));
        verify(mockPassengerDao, times(2)).add(any(Ticket.class));
        verifyNoMoreInteractions(mockPassengerDao);
        verifyZeroInteractions(mockRouteDao);
        verifyZeroInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertEquals("success", result);
    }
}
