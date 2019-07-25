package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TicketDto {
    private String route;
    private PassengerDto passenger;
    private String StationFrom;
    private LocalDateTime departure;
    private String stationTo;
    private float price;
}
