package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ChangeNameDto {

    @NotNull(message = "First name is a required field")
    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @NotNull(message = "Last name is a required field")
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

}
