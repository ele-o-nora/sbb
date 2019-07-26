package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class PassengerDto {
    private int id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;

    public void setDateOfBirth(final LocalDate dateOfBirth) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        this.dateOfBirth = formatter.format(dateOfBirth);
    }
}
