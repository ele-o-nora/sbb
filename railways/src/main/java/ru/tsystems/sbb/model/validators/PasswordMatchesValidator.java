package ru.tsystems.sbb.model.validators;

import ru.tsystems.sbb.model.dto.PasswordDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        PasswordDto passwordDto = (PasswordDto) obj;
        return passwordDto.getPassword()
                .equals(passwordDto.getMatchingPassword());
    }
}
