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
    private PassengerDto passenger;

    public void setFormattedPrice(final float price) {
        int stags = (int) price;
        int stars = (int) ((price - stags) / 0.14) * 100;
        formattedPrice = String.format("%d silver stags, %d copper stars", stags, stars);
    }

}
