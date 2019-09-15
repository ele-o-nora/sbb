package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains necessary information for the sale of two tickets at once
 *  for a journey with a stopover.
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public final class TransferTicketOrderDto {

    /**
     * Necessary information about the first of two journeys.
     */
    private TicketOrderDto firstTrain;

    /**
     * Necessary information about the second of two journeys.
     */
    private TicketOrderDto secondTrain;

}
