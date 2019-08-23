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

    public void setArrivals(final LocalDateTime arrivalTime, final int delay,
                            final boolean cancelled) {
        if (arrivalTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm");
            if (!cancelled) {
                actualArrival = formatter.format(arrivalTime
                        .plusMinutes(delay));
            } else {
                actualArrival = "-";
            }
            arrival = formatter.format(arrivalTime);
        } else {
            actualArrival = "-";
            arrival = "-";
        }
    }

    public void setDepartures(final LocalDateTime departureTime,
                              final int delay, final boolean cancelled) {
        if (departureTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm");
            departure = formatter.format(departureTime);
            if (!cancelled) {
                actualDeparture = formatter.format(departureTime
                        .plusMinutes(delay));
            } else {
                actualDeparture = "-";
            }
        } else {
            departure = "-";
            actualDeparture = "-";
        }
    }
}
