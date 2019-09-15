package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public final class RouteDto {
    private int id;
    private String number;
    private String line;
    private List<RouteStationDto> stations;
}
