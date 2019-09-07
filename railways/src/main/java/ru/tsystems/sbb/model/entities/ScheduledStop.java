package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing one stop of a specific scheduled journey.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "scheduled_stop")
public class ScheduledStop extends AbstractEntity {

    /**
     * The journey to which this stop belongs.
     */
    @ManyToOne
    @JoinColumn(name = "journey_id")
    private Journey journey;

    /**
     * Station which this stop represents.
     */
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    /**
     * Arrival at the station. Can be null for journey's origin.
     */
    @Column
    private LocalDateTime arrival;

    /**
     * Departure from the station. Can be null for journey's destination.
     */
    @Column
    private LocalDateTime departure;

    /**
     * List of tickets for which this stop serves as origin.
     */
    @OneToMany(mappedBy = "from")
    private List<Ticket> ticketsFrom;

    /**
     * List of tickets for which this stop serves as destination.
     */
    @OneToMany(mappedBy = "to")
    private List<Ticket> ticketsTo;

    /**
     * Arrival adjusted for delay. Null if the journey has never been delayed.
     */
    private LocalDateTime eta;

    /**
     * Departure adjusted for delay. Null if the journey has never been delayed.
     */
    private LocalDateTime etd;

}
