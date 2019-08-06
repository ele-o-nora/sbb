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
    private String status;

    public void setFormattedPrice(final float price) {
        int stags = (int) price;
        int stars = (int) ((price - stags) / 0.15);
        if (stags > 0 && stars > 0) {
            formattedPrice = String.format("%d silver stags, %d copper stars", stags, stars);
        } else if (stags > 0) {
            formattedPrice = String.format("%d silver stags", stags);
        } else {
            formattedPrice = String.format("%d copper stars", stars);
        }
    }

}
