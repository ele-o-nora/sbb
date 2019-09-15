package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

/**
 * Single station on the network.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"x", "y"}))
public class Station extends AbstractEntity {

    /**
     * Name of the station.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Station's x coordinate on the map.
     */
    @Column(nullable = false)
    private int x;

    /**
     * Station's y coordinate on the map.
     */
    @Column(nullable = false)
    private int y;

    /**
     * List of lines this station belongs to.
     */
    @OneToMany(mappedBy = "station")
    private List<LineStation> lines;

    /**
     * List of scheduled stops that represents specific journeys that stop at
     *  this station.
     */
    @OneToMany(mappedBy = "station")
    private List<ScheduledStop> trains;

    /**
     * List of journeys for which this station serves as destination.
     */
    @OneToMany(mappedBy = "destination")
    private List<Journey> routesTo;

    /**
     * Routes this station is linked to.
     */
    @OneToMany(mappedBy = "station")
    private List<RouteStation> routes;

}
