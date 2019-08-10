package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class BuyerDetailsDto {

    @Valid
    private PassengerDetailsDto passenger;

    @Valid
    private PaymentDetailsDto payment;

}
