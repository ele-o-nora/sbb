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
 * Route that is comprised of particular set of stations for the journey
 *  to pass through.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Route extends AbstractEntity {

    /**
     * Route's unique identifier.
     */
    @Column(nullable = false, unique = true)
    private String number;

    /**
     * Line to which the route is linked.
     */
    @ManyToOne
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    /**
     * List of scheduled journeys that run on this route.
     */
    @OneToMany(mappedBy = "route")
    private List<Journey> journeys;

    /**
     * List of stations the route consists of.
     */
    @OneToMany(mappedBy = "route")
    @OrderBy("id asc")
    private List<RouteStation> stations;
}
