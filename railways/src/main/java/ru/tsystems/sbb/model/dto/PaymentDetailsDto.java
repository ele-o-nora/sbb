package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
public final class PaymentDetailsDto {

    @NotNull(message = "Card number is a required field")
    @NotEmpty(message = "Card number cannot be empty")
    @Pattern(regexp = "\\d{16,18}",
            message = "Card number must consist of 16 or 18 digits")
    private String cardNumber;

    @NotNull(message = "Cardholder name is a required field")
    @NotEmpty(message = "Cardholder name cannot be empty")
    private String cardHolderName;

    @NotNull(message = "Expiration date is a required field")
    @DateTimeFormat(pattern = "MM/yyyy")
    @FutureOrPresent(message = "Expiration date cannot be in the past")
    private YearMonth validUntil;

    @NotNull(message = "Card verification code is a required field")
    @NotEmpty(message = "Card verification code cannot be empty")
    @Pattern(regexp = "\\d{3}",
            message = "Card verification code must consist of 3 digits")
    private String cvc;
}
