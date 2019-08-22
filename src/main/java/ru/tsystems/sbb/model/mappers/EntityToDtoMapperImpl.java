package ru.tsystems.sbb.model.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.sbb.model.dto.RouteDto;
import ru.tsystems.sbb.model.dto.RouteStationDto;
import ru.tsystems.sbb.model.dto.ScheduledStopDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.dto.TrainDto;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.Passenger;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.RouteStation;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.Ticket;
import ru.tsystems.sbb.model.entities.Train;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityToDtoMapperImpl implements EntityToDtoMapper {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Clock clock;

    private static final String OLD = "old";
    private static final String CURRENT = "current";
    private static final String FUTURE = "future";

    @Override
    public JourneyDto convert(final Journey journey) {
        JourneyDto journeyDto = mapper.map(journey, JourneyDto.class);
        journeyDto.setRoute(journey.getRoute().getNumber());
        journeyDto.setDestination(journey.getDestination().getName());
        List<ScheduledStopDto> stopsDto = journey.getStops().stream()
                .map(this::convert).collect(Collectors.toList());
        journeyDto.setStops(stopsDto);
        if (journey.isCancelled()) {
            journeyDto.setStatus("Cancelled");
        } else if (journey.getDelay() > 0) {
            journeyDto.setStatus("Delayed " + journey.getDelay() + " min");
        } else {
            journeyDto.setStatus("On time");
        }
        return journeyDto;
    }

    @Override
    public LineDto convert(final Line line) {
        return mapper.map(line, LineDto.class);
    }

    @Override
    public PassengerDto convert(final Passenger passenger) {
        PassengerDto passengerDto = mapper.map(passenger, PassengerDto.class);
        passengerDto.setDateOfBirth(passenger.getDateOfBirth());
        return passengerDto;
    }

    @Override
    public RouteDto convert(final Route route) {
        RouteDto routeDto = mapper.map(route, RouteDto.class);
        routeDto.setLine(route.getLine().getName());
        List<RouteStationDto> stations = route.getStations().stream()
                .map(this::convert).collect(Collectors.toList());
        routeDto.setStations(stations);
        return routeDto;
    }

    @Override
    public ScheduledStopDto convert(final ScheduledStop scheduledStop) {
        ScheduledStopDto scheduledStopDto = mapper.map(scheduledStop,
                ScheduledStopDto.class);
        scheduledStopDto.setStation(scheduledStop.getStation().getName());
        scheduledStopDto.setRoute(scheduledStop
                .getJourney().getRoute().getNumber());
        scheduledStopDto.setDirection(scheduledStop
                .getJourney().getDestination().getName());
        scheduledStopDto.setJourneyId(scheduledStop.getJourney().getId());
        scheduledStopDto.setDelay(scheduledStop.getJourney().getDelay());
        if (scheduledStop.getJourney().isCancelled()) {
            scheduledStopDto.setStatus("Cancelled");
        } else if (scheduledStop.getJourney().getDelay() > 0) {
            scheduledStopDto.setStatus("Delayed "
                    + scheduledStop.getJourney().getDelay() + " min");
        } else {
            scheduledStopDto.setStatus("On time");
        }
        scheduledStopDto.setArrivals(scheduledStop.getArrival(),
                scheduledStop.getJourney().getDelay());
        scheduledStopDto.setDepartures(scheduledStop.getDeparture(),
                scheduledStop.getJourney().getDelay());
        return scheduledStopDto;
    }

    @Override
    public StationDto convert(final Station station) {
        return mapper.map(station, StationDto.class);
    }

    @Override
    public TicketDto convert(final Ticket ticket) {
        TicketDto ticketDto = mapper.map(ticket, TicketDto.class);
        ticketDto.setJourney(convert(ticket.getJourney()));
        ticketDto.setStationFrom(convert(ticket.getFrom()));
        ticketDto.setStationTo(convert(ticket.getTo()));
        ticketDto.setPassenger(convert(ticket.getPassenger()));
        ticketDto.setFormattedPrice(formatPrice(ticket.getPrice()));
        if (ticket.getTo().getArrival().isBefore(LocalDateTime.now(clock))) {
            ticketDto.setCategory(OLD);
        } else if (ticket.getFrom().getDeparture()
                .isAfter(LocalDateTime.now(clock))) {
            ticketDto.setCategory(FUTURE);
        } else {
            ticketDto.setCategory(CURRENT);
        }
        return ticketDto;
    }

    @Override
    public TrainDto convert(final Train train) {
        return mapper.map(train, TrainDto.class);
    }

    @Override
    public RouteStationDto convert(RouteStation routeStation) {
        RouteStationDto routeStationDto = mapper.map(routeStation,
                RouteStationDto.class);
        routeStationDto.setName(routeStation.getStation().getName());
        return routeStationDto;
    }

    public String formatPrice(final float price) {
        String singularStag = "silver stag";
        String pluralStags = "silver stags";
        String singularStar = "copper star";
        String pluralStars = "copper stars";
        int stags = (int) price;
        int stars = (int) ((price - stags) / 0.15);
        if (stags > 0 && stars > 0) {
            return String.format("%d %s, %d %s",
                    stags, (stags == 1 ? singularStag : pluralStags),
                    stars, (stars == 1 ? singularStar : pluralStars));
        } else if (stags > 0) {
            return String.format("%d %s", stags,
                    (stags == 1 ? singularStag : pluralStags));
        } else {
            return String.format("%d %s", stars,
                    (stars == 1 ? singularStar : pluralStars));
        }
    }

}
