package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JourneyDto {
    private int id;
    private String route;
    private String destination;
    private TrainDto trainType;
    private List<ScheduledStopDto> stops;
    private int availableSeats;
    private String timeEnRoute;

    public void setTimeEnRoute(long minutes) {
        timeEnRoute = String.format("%d h %2d min", minutes / 60, minutes % 60);
    }
}
