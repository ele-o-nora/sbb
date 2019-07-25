package ru.tsystems.sbb.model.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Ticket {

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class TicketKey implements Serializable {
        static final long serialVersionUID = 1L;
        private int journeyId;
        private int passengerId;
    }

    @EmbeddedId
    private TicketKey id;

    @ManyToOne
    @JoinColumn(name = "journey_id")
    @MapsId("journeyId")
    private Journey journey;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    @MapsId("passengerId")
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "station_from_id")
    private Station from;

    @ManyToOne
    @JoinColumn(name = "station_to_id")
    private Station to;

    @Column
    private float price;
}
