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
}
