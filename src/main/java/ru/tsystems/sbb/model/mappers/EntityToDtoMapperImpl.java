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

@Component
public class EntityToDtoMapperImpl implements EntityToDtoMapper{

    @Autowired
    private ModelMapper mapper;

    @Override
    public JourneyDto convert(Journey journey) {
        JourneyDto journeyDto = mapper.map(journey, JourneyDto.class);
        journeyDto.setRoute(journey.getRoute().getNumber());
        journeyDto.setTimeEnRoute(journey.getMinutesEnRoute());
        return journeyDto;
    }

    @Override
    public LineDto convert(Line line) {
        LineDto lineDto = mapper.map(line, LineDto.class);
        for (LineStation ls : line.getStations()) {
            StationDto station = convert(ls.getStation());
            lineDto.getStations().add(station);
        }
        return lineDto;
    }

    @Override
    public PassengerDto convert(Passenger passenger) {
        return mapper.map(passenger, PassengerDto.class);
    }

    @Override
    public RouteDto convert(Route route) {
        RouteDto routeDto = mapper.map(route, RouteDto.class);
        routeDto.setLine(route.getLine().getName());
        return routeDto;
    }

    @Override
    public ScheduledStopDto convert(ScheduledStop scheduledStop) {
        ScheduledStopDto scheduledStopDto = mapper.map(scheduledStop,
                ScheduledStopDto.class);
        scheduledStopDto.setStation(scheduledStop.getStation().getName());
        scheduledStopDto.setRoute(scheduledStop
                .getJourney().getRoute().getNumber());
        scheduledStopDto.setDirection(scheduledStop
                .getJourney().getDestination());
        return scheduledStopDto;
    }

    @Override
    public StationDto convert(Station station) {
        return mapper.map(station, StationDto.class);
    }

    @Override
    public TicketDto convert(Ticket ticket) {
        TicketDto ticketDto = mapper.map(ticket, TicketDto.class);
        ticketDto.setRoute(ticket.getJourney().getRoute().getNumber());
        ticketDto.setStationFrom(ticket.getFrom().getName());
        ticketDto.setStationTo(ticket.getTo().getName());
        return ticketDto;
    }

    @Override
    public TrainDto convert(Train train) {
        return mapper.map(train, TrainDto.class);
    }
}
