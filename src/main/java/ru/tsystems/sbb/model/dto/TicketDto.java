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
    private String stationFrom;
    private String stationTo;
    private float price;
}
