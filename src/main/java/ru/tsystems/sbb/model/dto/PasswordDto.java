package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tsystems.sbb.model.validators.PasswordMatches;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@PasswordMatches
public class PasswordDto {

    @NotNull(message = "Password is a required field")
    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}",
    message = "Password must be at least 6 symbols long, containing at least "
            + "one digit, one UPPERCASE and one lowercase letter")
    private String password;
    private String matchingPassword;
}
