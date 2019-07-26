package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduledStopDto {
    private int id;
    private String station;
    private int journeyId;
    private String route;
    private String direction;
    private String arrival;
    private String departure;

    public void setArrival(final LocalDateTime arrivalTime) {
        if (arrivalTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm");
            arrival = formatter.format(arrivalTime);
        } else {
            arrival = "-";
        }
    }

    public void setDeparture(final LocalDateTime departureTime) {
        if (departureTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm");
            departure = formatter.format(departureTime);
        } else {
            departure = "-";
        }
    }
}
