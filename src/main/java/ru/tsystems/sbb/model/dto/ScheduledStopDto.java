package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ScheduledStopDto {
    private int id;
    private String station;
    @ToString.Exclude
    private int journeyId;
    @ToString.Exclude
    private String route;
    @ToString.Exclude
    private String direction;
    private String arrival;
    private String departure;
    private String actualArrival;
    private String actualDeparture;
    private int delay;
    private String status;

    public void setArrivals(final LocalDateTime arrivalTime, final int delay) {
        if (arrivalTime != null && !status.equals("Cancelled")) {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm");
            actualArrival = formatter.format(arrivalTime.plusMinutes(delay));
            arrival = formatter.format(arrivalTime);
        } else {
            actualArrival = "-";
            arrival = "-";
        }
    }

    public void setDepartures(final LocalDateTime departureTime,
                              final int delay) {
        if (departureTime != null && !status.equals("Cancelled")) {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm");
            departure = formatter.format(departureTime);
            actualDeparture = formatter.format(departureTime
                    .plusMinutes(delay));
        } else {
            departure = "-";
            actualDeparture = "-";
        }
    }
}
