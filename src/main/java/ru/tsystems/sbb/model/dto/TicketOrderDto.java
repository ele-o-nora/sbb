package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TicketOrderDto {

    private JourneyDto journey;
    private ScheduledStopDto origin;
    private ScheduledStopDto destination;
    @ToString.Exclude
    private String formattedPrice;
    private float price;
    @ToString.Exclude
    private String status;

}
