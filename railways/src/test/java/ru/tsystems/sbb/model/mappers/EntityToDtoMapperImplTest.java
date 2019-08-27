package ru.tsystems.sbb.model.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.dto.ScheduledStopDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Passenger;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.Ticket;
import ru.tsystems.sbb.model.entities.Train;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class EntityToDtoMapperImplTest {

    @InjectMocks
    private EntityToDtoMapperImpl entityToDtoMapper;

    @Mock
    private ModelMapper mockMapper;

    @Mock
    private Clock mockClock;

    private static final LocalDate LOCAL_DATE = LocalDate.of(2020, 2, 2);
    private static final String ROUTE_NUMBER = "routeNumber";
    private static final String DESTINATION = "destination";
    private static final String STATION_NAME = "stationName";
    private static final String EXPECTED_DATE_TIME = "2020-02-02 00:00";

    @BeforeEach
    void initMocks(){
        MockitoAnnotations.initMocks(this);
        Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId
                .systemDefault()).toInstant(), ZoneId.systemDefault());
        Mockito.lenient().when(mockClock.instant())
                .thenReturn(fixedClock.instant());
        Mockito.lenient().when(mockClock.getZone())
                .thenReturn(fixedClock.getZone());
    }

    @Test
    void formatPriceIntegerSingularTest() {
        float price = 1;
        String result = entityToDtoMapper.formatPrice(price);
        assertEquals("1 silver stag", result);
    }

    @Test
    void formatPriceIntegerPluralTest() {
        float price = 2;
        String result = entityToDtoMapper.formatPrice(price);
        assertEquals("2 silver stags", result);
    }

    @Test
    void formatPriceLessThanOneSingularTest() {
        float price = 0.2f;
        String result = entityToDtoMapper.formatPrice(price);
        assertEquals("1 copper star", result);
    }

    @Test
    void formatPriceLessThanOnePluralTest() {
        float price = 0.5f;
        String result = entityToDtoMapper.formatPrice(price);
        assertEquals("3 copper stars", result);
    }

    @Test
    void formatPriceFloatAboveOneBothSingularTest() {
        float price = 1.2f;
        String result = entityToDtoMapper.formatPrice(price);
        assertEquals("1 silver stag, 1 copper star", result);
    }

    @Test
    void formatPriceFloatAboveOneBothPluralTest() {
        float price = 2.5f;
        String result = entityToDtoMapper.formatPrice(price);
        assertEquals("2 silver stags, 3 copper stars", result);
    }

    @Test
    void formatPriceFloatAboveOneSingularPluralTest() {
        float price = 1.5f;
        String result = entityToDtoMapper.formatPrice(price);
        assertEquals("1 silver stag, 3 copper stars", result);
    }

    @Test
    void formatPriceFloatAboveOnePluralSingularTest() {
        float price = 2.2f;
        String result = entityToDtoMapper.formatPrice(price);
        assertEquals("2 silver stags, 1 copper star", result);
    }

    @Test
    void convertOldTicketTest() {
        Ticket ticket = new Ticket();
        Journey journey = new Journey();
        journey.setRoute(new Route());
        journey.getRoute().setNumber(ROUTE_NUMBER);
        journey.setDestination(new Station());
        journey.getDestination().setName(DESTINATION);
        ScheduledStop stop = new ScheduledStop();
        stop.setStation(new Station());
        stop.getStation().setName(STATION_NAME);
        stop.setJourney(journey);
        stop.setArrival(LOCAL_DATE.minusDays(1).atStartOfDay());
        journey.setStops(Collections.singletonList(stop));
        ticket.setFrom(stop);
        ticket.setTo(stop);
        ticket.setJourney(journey);
        ticket.setPassenger(new Passenger());
        ticket.getPassenger().setDateOfBirth(LOCAL_DATE.minusYears(20));
        ticket.setPrice(10);
        when(mockMapper.map(any(Ticket.class), any()))
                .thenReturn(new TicketDto());
        when(mockMapper.map(any(Journey.class), any()))
                .thenReturn(new JourneyDto());
        when(mockMapper.map(any(ScheduledStop.class), any()))
                .thenReturn(new ScheduledStopDto());
        when(mockMapper.map(any(Passenger.class), any()))
                .thenReturn(new PassengerDto());

        TicketDto result = entityToDtoMapper.convert(ticket);

        assertEquals("old", result.getCategory());
    }

    @Test
    void convertCancelledJourneyTicketTest() {
        Ticket ticket = new Ticket();
        Journey journey = new Journey();
        journey.setRoute(new Route());
        journey.getRoute().setNumber(ROUTE_NUMBER);
        journey.setDestination(new Station());
        journey.getDestination().setName(DESTINATION);
        journey.setCancelled(true);
        ScheduledStop stop = new ScheduledStop();
        stop.setStation(new Station());
        stop.getStation().setName(STATION_NAME);
        stop.setJourney(journey);
        stop.setArrival(LOCAL_DATE.minusDays(1).atStartOfDay());
        journey.setStops(Collections.singletonList(stop));
        ticket.setFrom(stop);
        ticket.setTo(stop);
        ticket.setJourney(journey);
        ticket.setPassenger(new Passenger());
        ticket.getPassenger().setDateOfBirth(LOCAL_DATE.minusYears(20));
        ticket.setPrice(10);
        when(mockMapper.map(any(Ticket.class), any()))
                .thenReturn(new TicketDto());
        when(mockMapper.map(any(Journey.class), any()))
                .thenReturn(new JourneyDto());
        when(mockMapper.map(any(ScheduledStop.class), any()))
                .thenReturn(new ScheduledStopDto());
        when(mockMapper.map(any(Passenger.class), any()))
                .thenReturn(new PassengerDto());

        TicketDto result = entityToDtoMapper.convert(ticket);

        assertEquals("old", result.getCategory());
    }

    @Test
    void convertCurrentTicketTest() {
        Ticket ticket = new Ticket();
        Journey journey = new Journey();
        journey.setRoute(new Route());
        journey.getRoute().setNumber(ROUTE_NUMBER);
        journey.setDestination(new Station());
        journey.getDestination().setName(DESTINATION);
        ScheduledStop stop = new ScheduledStop();
        stop.setStation(new Station());
        stop.getStation().setName(STATION_NAME);
        stop.setJourney(journey);
        stop.setArrival(LOCAL_DATE.plusDays(1).atStartOfDay());
        stop.setDeparture(LOCAL_DATE.minusDays(1).atStartOfDay());
        journey.setStops(Collections.singletonList(stop));
        ticket.setFrom(stop);
        ticket.setTo(stop);
        ticket.setJourney(journey);
        ticket.setPassenger(new Passenger());
        ticket.getPassenger().setDateOfBirth(LOCAL_DATE.minusYears(20));
        ticket.setPrice(10);
        when(mockMapper.map(any(Ticket.class), any()))
                .thenReturn(new TicketDto());
        when(mockMapper.map(any(Journey.class), any()))
                .thenReturn(new JourneyDto());
        when(mockMapper.map(any(ScheduledStop.class), any()))
                .thenReturn(new ScheduledStopDto());
        when(mockMapper.map(any(Passenger.class), any()))
                .thenReturn(new PassengerDto());

        TicketDto result = entityToDtoMapper.convert(ticket);

        assertEquals("current", result.getCategory());
    }

    @Test
    void convertFutureTicketTest() {
        Ticket ticket = new Ticket();
        Journey journey = new Journey();
        journey.setRoute(new Route());
        journey.getRoute().setNumber(ROUTE_NUMBER);
        journey.setDestination(new Station());
        journey.getDestination().setName(DESTINATION);
        ScheduledStop stop = new ScheduledStop();
        stop.setStation(new Station());
        stop.getStation().setName(STATION_NAME);
        stop.setJourney(journey);
        stop.setDeparture(LOCAL_DATE.plusDays(1).atStartOfDay());
        stop.setArrival(LOCAL_DATE.plusDays(2).atStartOfDay());
        journey.setStops(Collections.singletonList(stop));
        ticket.setFrom(stop);
        ticket.setTo(stop);
        ticket.setJourney(journey);
        ticket.setPassenger(new Passenger());
        ticket.getPassenger().setDateOfBirth(LOCAL_DATE.minusYears(20));
        ticket.setPrice(10);
        when(mockMapper.map(any(Ticket.class), any()))
                .thenReturn(new TicketDto());
        when(mockMapper.map(any(Journey.class), any()))
                .thenReturn(new JourneyDto());
        when(mockMapper.map(any(ScheduledStop.class), any()))
                .thenReturn(new ScheduledStopDto());
        when(mockMapper.map(any(Passenger.class), any()))
                .thenReturn(new PassengerDto());

        TicketDto result = entityToDtoMapper.convert(ticket);

        assertEquals("future", result.getCategory());
    }

    @Test
    void convertScheduledStopOnTimeTest() {
        ScheduledStop scheduledStop = new ScheduledStop();
        scheduledStop.setStation(new Station());
        scheduledStop.getStation().setName(STATION_NAME);
        scheduledStop.setJourney(new Journey());
        scheduledStop.getJourney().setRoute(new Route());
        scheduledStop.getJourney().getRoute().setNumber(ROUTE_NUMBER);
        scheduledStop.getJourney().setDestination(new Station());
        scheduledStop.getJourney().getDestination().setName(DESTINATION);
        scheduledStop.setArrival(LocalDateTime.now(mockClock));
        when(mockMapper.map(any(ScheduledStop.class), any()))
                .thenReturn(new ScheduledStopDto());

        ScheduledStopDto result = entityToDtoMapper.convert(scheduledStop);

        assertEquals("On time", result.getStatus());
        assertEquals(EXPECTED_DATE_TIME, result.getArrival());
        assertEquals(EXPECTED_DATE_TIME, result.getActualArrival());
        assertEquals("-", result.getDeparture());
        assertEquals("-", result.getActualDeparture());
    }

    @Test
    void convertScheduledStopDelayedTest() {
        ScheduledStop scheduledStop = new ScheduledStop();
        scheduledStop.setStation(new Station());
        scheduledStop.getStation().setName(STATION_NAME);
        scheduledStop.setJourney(new Journey());
        scheduledStop.getJourney().setRoute(new Route());
        scheduledStop.getJourney().getRoute().setNumber(ROUTE_NUMBER);
        scheduledStop.getJourney().setDestination(new Station());
        scheduledStop.getJourney().getDestination().setName(DESTINATION);
        scheduledStop.setDeparture(LocalDateTime.now(mockClock));
        scheduledStop.getJourney().setDelay(10);
        String expectedActualDeparture = "2020-02-02 00:10";
        when(mockMapper.map(any(ScheduledStop.class), any()))
                .thenReturn(new ScheduledStopDto());

        ScheduledStopDto result = entityToDtoMapper.convert(scheduledStop);

        assertEquals("Delayed", result.getStatus());
        assertEquals(EXPECTED_DATE_TIME, result.getDeparture());
        assertEquals(expectedActualDeparture, result.getActualDeparture());
        assertEquals("-", result.getArrival());
        assertEquals("-", result.getActualArrival());
    }

    @Test
    void convertScheduledStopCancelledTest() {
        ScheduledStop scheduledStop = new ScheduledStop();
        scheduledStop.setStation(new Station());
        scheduledStop.getStation().setName(STATION_NAME);
        scheduledStop.setJourney(new Journey());
        scheduledStop.getJourney().setRoute(new Route());
        scheduledStop.getJourney().getRoute().setNumber(ROUTE_NUMBER);
        scheduledStop.getJourney().setDestination(new Station());
        scheduledStop.getJourney().getDestination().setName(DESTINATION);
        scheduledStop.setArrival(LocalDateTime.now(mockClock));
        scheduledStop.setDeparture(LocalDateTime.now(mockClock));
        scheduledStop.getJourney().setCancelled(true);
        when(mockMapper.map(any(ScheduledStop.class), any()))
                .thenReturn(new ScheduledStopDto());

        ScheduledStopDto result = entityToDtoMapper.convert(scheduledStop);

        assertEquals("Cancelled", result.getStatus());
        assertEquals(EXPECTED_DATE_TIME, result.getDeparture());
        assertEquals(EXPECTED_DATE_TIME, result.getArrival());
        assertEquals("-", result.getActualArrival());
        assertEquals("-", result.getActualDeparture());
    }

    @Test
    void convertJourneyOnTimeNotEnRouteTest() {
        Journey journey = new Journey();
        journey.setRoute(new Route());
        journey.getRoute().setNumber(ROUTE_NUMBER);
        journey.setDestination(new Station());
        journey.getDestination().setName(DESTINATION);
        journey.setTrainType(new Train());
        ScheduledStop scheduledStop = new ScheduledStop();
        scheduledStop.setStation(new Station());
        scheduledStop.getStation().setName(STATION_NAME);
        scheduledStop.setJourney(journey);
        scheduledStop.setArrival(LocalDateTime.now(mockClock).minusMinutes(5));
        journey.setStops(Collections.singletonList(scheduledStop));
        when(mockMapper.map(any(Journey.class), any()))
                .thenReturn(new JourneyDto());
        when(mockMapper.map(any(ScheduledStop.class), any()))
                .thenReturn(new ScheduledStopDto());

        JourneyDto result = entityToDtoMapper.convert(journey);

        assertEquals("On time", result.getStatus());
        assertFalse(result.isEnRoute());
    }

    @Test
    void convertJourneyDelayedEnRouteTest() {
        Journey journey = new Journey();
        journey.setRoute(new Route());
        journey.getRoute().setNumber(ROUTE_NUMBER);
        journey.setDestination(new Station());
        journey.getDestination().setName(DESTINATION);
        journey.setTrainType(new Train());
        ScheduledStop scheduledStop = new ScheduledStop();
        scheduledStop.setStation(new Station());
        scheduledStop.getStation().setName(STATION_NAME);
        scheduledStop.setJourney(journey);
        scheduledStop.setArrival(LocalDateTime.now(mockClock));
        journey.setStops(Collections.singletonList(scheduledStop));
        journey.setDelay(10);
        when(mockMapper.map(any(Journey.class), any()))
                .thenReturn(new JourneyDto());
        when(mockMapper.map(any(ScheduledStop.class), any()))
                .thenReturn(new ScheduledStopDto());

        JourneyDto result = entityToDtoMapper.convert(journey);

        assertEquals("Delayed 10 min", result.getStatus());
        assertTrue(result.isEnRoute());
    }

    @Test
    void convertJourneyCancelledTest() {
        Journey journey = new Journey();
        journey.setRoute(new Route());
        journey.getRoute().setNumber(ROUTE_NUMBER);
        journey.setDestination(new Station());
        journey.getDestination().setName(DESTINATION);
        journey.setTrainType(new Train());
        ScheduledStop scheduledStop = new ScheduledStop();
        scheduledStop.setStation(new Station());
        scheduledStop.getStation().setName(STATION_NAME);
        scheduledStop.setJourney(journey);
        scheduledStop.setArrival(LocalDateTime.now(mockClock));
        journey.setStops(Collections.singletonList(scheduledStop));
        journey.setCancelled(true);
        when(mockMapper.map(any(Journey.class), any()))
                .thenReturn(new JourneyDto());
        when(mockMapper.map(any(ScheduledStop.class), any()))
                .thenReturn(new ScheduledStopDto());

        JourneyDto result = entityToDtoMapper.convert(journey);

        assertEquals("Cancelled", result.getStatus());
        assertFalse(result.isEnRoute());
    }
}
