package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains necessary information for the sale of a ticket.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TicketOrderDto {

    /**
     * Journey for which the ticket is valid.
     */
    private JourneyDto journey;

    /**
     * Stop from which the ticket is valid.
     */
    private ScheduledStopDto origin;

    /**
     * Stop until which the ticket is valid.
     */
    private ScheduledStopDto destination;

    /**
     * Price in format convenient for the client.
     */
    @ToString.Exclude
    private String formattedPrice;

    /**
     * Price in format convenient for the database.
     */
    private float price;

    /**
     * Status to indicate if anything went wrong during the preparation
     *  of the ticket sale.
     */
    @ToString.Exclude
    private String status;

}
