package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketDto {
    private String route;
    private PassengerDto passenger;
    private ScheduledStopDto stationFrom;
    private ScheduledStopDto stationTo;
    private float price;
}
