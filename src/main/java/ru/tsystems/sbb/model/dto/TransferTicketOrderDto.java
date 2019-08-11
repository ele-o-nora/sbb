package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class TransferTicketOrderDto {

    private TicketOrderDto firstTrain;
    private TicketOrderDto secondTrain;

}
