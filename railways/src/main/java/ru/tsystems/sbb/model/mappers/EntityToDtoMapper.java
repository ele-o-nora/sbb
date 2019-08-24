package ru.tsystems.sbb.model.mappers;

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

public interface EntityToDtoMapper {
    JourneyDto convert(Journey journey);
    LineDto convert(Line line);
    PassengerDto convert(Passenger passenger);
    RouteDto convert(Route route);
    ScheduledStopDto convert(ScheduledStop scheduledStop);
    StationDto convert(Station station);
    TicketDto convert(Ticket ticket);
    TrainDto convert(Train train);
    RouteStationDto convert(RouteStation routeStation);
    String formatPrice(float price);
}
