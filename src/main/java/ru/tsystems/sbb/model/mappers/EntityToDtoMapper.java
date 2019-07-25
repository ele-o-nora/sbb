package ru.tsystems.sbb.model.mappers;

import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.sbb.model.dto.RouteDto;
import ru.tsystems.sbb.model.dto.ScheduleDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.dto.TrainDto;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.Passenger;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.Schedule;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.Ticket;
import ru.tsystems.sbb.model.entities.Train;

public interface EntityToDtoMapper {
    JourneyDto convert(Journey journey);
    LineDto convert(Line line);
    PassengerDto convert(Passenger passenger);
    RouteDto convert(Route route);
    ScheduleDto convert(Schedule schedule);
    StationDto convert(Station station);
    TicketDto convert(Ticket ticket);
    TrainDto convert(Train train);
}
