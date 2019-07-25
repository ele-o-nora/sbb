package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PassengerDto {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
}
