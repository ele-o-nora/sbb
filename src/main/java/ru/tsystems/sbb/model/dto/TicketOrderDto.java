package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketOrderDto {

    private JourneyDto journey;
    private ScheduledStopDto origin;
    private ScheduledStopDto destination;
    private String formattedPrice;
    private String status;

}
