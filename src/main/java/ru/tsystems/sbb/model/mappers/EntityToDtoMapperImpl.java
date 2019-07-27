package ru.tsystems.sbb.model.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.sbb.model.dto.RouteDto;
import ru.tsystems.sbb.model.dto.ScheduledStopDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.dto.TrainDto;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Passenger;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.Ticket;
import ru.tsystems.sbb.model.entities.Train;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityToDtoMapperImpl implements EntityToDtoMapper {

    @Autowired
    private ModelMapper mapper;

    @Override
    public JourneyDto convert(final Journey journey) {
        JourneyDto journeyDto = mapper.map(journey, JourneyDto.class);
        journeyDto.setRoute(journey.getRoute().getNumber());
        journeyDto.setDestination(journey.getDestination().getName());
        List<ScheduledStopDto> stopsDto = journey.getStops().stream()
                .map(this::convert).collect(Collectors.toList());
        journeyDto.setStops(stopsDto);
        return journeyDto;
    }

    @Override
    public LineDto convert(final Line line) {
        LineDto lineDto = mapper.map(line, LineDto.class);
        for (LineStation ls : line.getStations()) {
            StationDto station = convert(ls.getStation());
            lineDto.getStations().add(station);
        }
        return lineDto;
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
        List<StationDto> stationsDto = route.getStations().stream()
                .map(this::convert).collect(Collectors.toList());
        routeDto.setStations(stationsDto);
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
        scheduledStopDto.setArrival(scheduledStop.getArrival());
        scheduledStopDto.setDeparture(scheduledStop.getDeparture());
        return scheduledStopDto;
    }

    @Override
    public StationDto convert(final Station station) {
        return mapper.map(station, StationDto.class);
    }

    @Override
    public TicketDto convert(final Ticket ticket) {
        TicketDto ticketDto = mapper.map(ticket, TicketDto.class);
        ticketDto.setRoute(ticket.getJourney().getRoute().getNumber());
        ticketDto.setStationFrom(convert(ticket.getFrom()));
        ticketDto.setStationTo(convert(ticket.getTo()));
        return ticketDto;
    }

    @Override
    public TrainDto convert(final Train train) {
        return mapper.map(train, TrainDto.class);
    }
}
