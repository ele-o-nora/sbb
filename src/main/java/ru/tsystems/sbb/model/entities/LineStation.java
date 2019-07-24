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
public class LineStation {

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class LineStationKey implements Serializable {
        static final long serialVersionUID = 1L;
        private int lineId;
        private int stationId;
    }

    @EmbeddedId
    private LineStationKey id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    @MapsId("lineId")
    private Line line;

    @ManyToOne
    @JoinColumn(name = "station_id")
    @MapsId("stationId")
    private Station station;

    @Column
    private int order;
}
