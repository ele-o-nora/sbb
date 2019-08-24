package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PassengerDetailsDto {

    @NotNull(message = "First name is a required field")
    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotNull(message = "Last name is a required field")
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @NotNull(message = "Date of birth is a required field")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "Date of birth cannot be in the future")
    private LocalDate dateOfBirth;

}
