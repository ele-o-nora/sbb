package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SignUpDto {

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

    @NotNull(message = "E-mail is a required field")
    @NotBlank(message = "E-mail cannot be empty")
    @Email(message = "E-mail must be valid")
    private String email;

    @Valid
    private PasswordDto password;
}
