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
 * Helper entity describing the order of the stations on the line.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "line_station",
        uniqueConstraints = @UniqueConstraint(columnNames = {"line_id",
                "station_id"}))
public class LineStation extends AbstractEntity {

    /**
     * Line on which the station is situated.
     */
    @ManyToOne
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    /**
     * Station for which the order is determined.
     */
    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    /**
     * Ordinal number of the station on the line, starting from the
     *  geographical center of the map.
     */
    @Column(name = "order_from_centre", nullable = false)
    private int order;
}
