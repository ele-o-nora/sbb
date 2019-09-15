package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ru.tsystems.dto.ScheduledStopDto;

@Getter
@Setter
@NoArgsConstructor
public final class TicketDto {

    private int id;
    private JourneyDto journey;
    private PassengerDto passenger;
    private ScheduledStopDto stationFrom;
    private ScheduledStopDto stationTo;
    private String formattedPrice;
    private String category;

}
