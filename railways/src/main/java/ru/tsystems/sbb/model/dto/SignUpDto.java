package ru.tsystems.sbb.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public final class SignUpDto {

    @Valid
    private @NonNull PassengerDetailsDto passengerDetails;

    @NotNull(message = "E-mail is a required field")
    @NotBlank(message = "E-mail cannot be empty")
    @Email(message = "E-mail must be valid")
    private String email;

    @Valid
    private @NonNull PasswordDto password;
}
