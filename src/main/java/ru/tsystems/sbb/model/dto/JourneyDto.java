package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class JourneyDto {
    private int id;
    private String route;
    private String destination;
    @ToString.Exclude
    private TrainDto trainType;
    @ToString.Exclude
    private List<ScheduledStopDto> stops;
    private String status;
}
