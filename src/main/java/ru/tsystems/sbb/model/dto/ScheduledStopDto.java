package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ScheduledStopDto {
    private int id;
    private String station;
    private int journeyId;
    private String route;
    private String direction;
    private LocalDateTime arrival;
    private LocalDateTime departure;
}
