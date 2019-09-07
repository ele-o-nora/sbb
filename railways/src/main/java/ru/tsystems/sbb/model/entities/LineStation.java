package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Helper entity describing the order of the stations on the line.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "line_station")
public class LineStation extends AbstractEntity {

    /**
     * Line on which the station is situated.
     */
    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    /**
     * Station for which the order is determined.
     */
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    /**
     * Ordinal number of the station on the line, starting from the
     *  geographical center of the map.
     */
    @Column(name = "order_from_centre")
    private int order;
}
