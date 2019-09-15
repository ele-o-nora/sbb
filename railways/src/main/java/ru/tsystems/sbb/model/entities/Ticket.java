package ru.tsystems.sbb.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Ticket that serves as a travel document.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"journey_id",
        "passenger_id"}))
@Builder
public class Ticket extends AbstractEntity {

    /**
     * Journey for which the ticket was sold.
     */
    @ManyToOne
    @JoinColumn(name = "journey_id", nullable = false)
    private Journey journey;

    /**
     * Passenger to whom the ticket was sold.
     */
    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    /**
     * Stop from which this ticket's passenger should board the train.
     */
    @ManyToOne
    @JoinColumn(name = "stop_from_id", nullable = false)
    private ScheduledStop from;

    /**
     * Stop at which this ticket's passenger should alight from the train.
     */
    @ManyToOne
    @JoinColumn(name = "stop_to_id", nullable = false)
    private ScheduledStop to;

    /**
     * Ticket's price.
     */
    @Column(nullable = false)
    private float price;
}
