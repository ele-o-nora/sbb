package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class SignUpDto {

    @Valid
    private PassengerDetailsDto passengerDetails;

    @NotNull(message = "E-mail is a required field")
    @NotBlank(message = "E-mail cannot be empty")
    @Email(message = "E-mail must be valid")
    private String email;

    @Valid
    private PasswordDto password;
}
