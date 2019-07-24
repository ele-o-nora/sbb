package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketDto {
    private JourneyDto journey;
    private PassengerDto passenger;
    private StationDto from;
    private StationDto to;
    private int travelClass;
    private float price;
}
