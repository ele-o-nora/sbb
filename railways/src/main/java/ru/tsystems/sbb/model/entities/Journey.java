package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.List;

/**
 * Single scheduled journey.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Journey extends AbstractEntity {

    /**
     * Route which this journey follows.
     */
    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    /**
     * Model of physical train that carries out this journey.
     */
    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train trainType;

    /**
     * List of tickets bought for this journey.
     */
    @OneToMany(mappedBy = "journey")
    private List<Ticket> tickets;

    /**
     * List of stops that contains information about this journey's
     *  detailed schedule.
     */
    @OneToMany(mappedBy = "journey")
    @OrderBy("id asc")
    private List<ScheduledStop> stops;

    /**
     * Last station of the journey, specifying its direction on the route.
     */
    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private Station destination;

    /**
     * Number of minutes this journey is delayed.
     */
    @Column(nullable = false)
    private int delay;

    /**
     * Flag indicating if this journey is cancelled.
     */
    @Column(nullable = false)
    private boolean cancelled;

}
