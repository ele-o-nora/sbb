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
    private float price;
    private String formattedPrice;

    public String setFormattedPrice(final float price) {
        int stags = (int) price;
        int stars = (int) ((price - stags) / 0.14) * 100;
        return String.format("%d silver stags, %d copper stars", stags, stars);
    }

}
