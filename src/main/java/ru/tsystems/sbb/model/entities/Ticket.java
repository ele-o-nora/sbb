package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Ticket extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "journey_id")
    private Journey journey;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "stop_from_id")
    private ScheduledStop from;

    @ManyToOne
    @JoinColumn(name = "stop_to_id")
    private ScheduledStop to;

    @Column
    private float price;
}
