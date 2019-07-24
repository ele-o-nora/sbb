package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StationDto {
    private int id;
    private String name;
    private int zone;
    private List<ScheduleDto> trains;
}
