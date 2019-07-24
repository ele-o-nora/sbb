package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleDto {
    private StationDto station;
    private JourneyDto journey;
    private LocalDateTime arrival;
    private LocalDateTime departure;
}
