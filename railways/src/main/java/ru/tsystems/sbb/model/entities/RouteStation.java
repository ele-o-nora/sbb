package ru.tsystems.sbb.model.entities;

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
 * Helper entity linking routes and their stations.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "route_station",
        uniqueConstraints = @UniqueConstraint(columnNames = {"route_id",
                "station_id"}))
public class RouteStation extends AbstractEntity {

    /**
     * Route for which the station is described.
     */
    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    /**
     * Station that is linked to the route.
     */
    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    /**
     * Number of minutes each journey is supposed to spend on this station.
     */
    @Column(name = "wait_time")
    private Integer waitTime;
}
