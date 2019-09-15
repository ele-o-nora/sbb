package ru.tsystems.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public final class StationDto {
    private int id;
    private String name;
    private int x;
    private int y;
}
