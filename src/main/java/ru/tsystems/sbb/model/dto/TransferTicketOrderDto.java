package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TransferTicketOrderDto {

    private TicketOrderDto firstTrain;
    private TicketOrderDto secondTrain;

}
