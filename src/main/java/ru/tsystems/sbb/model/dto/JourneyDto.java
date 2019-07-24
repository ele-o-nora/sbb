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
    private int firstClassSeats;
    private int secondClassSeats;
    private RouteDto route;
    private List<ScheduleDto> stops;
}
