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
@Table(name = "route_station")
public class RouteStation {

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class RouteStationKey implements Serializable {
        static final long serialVersionUID = 1L;
        private int routeId;
        private int stationId;
    }

    @EmbeddedId
    private RouteStationKey id;

    @ManyToOne
    @JoinColumn(name = "route_id")
    @MapsId("routeId")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "station_id")
    @MapsId("stationId")
    private Station station;

    @Column(name = "time_en_route")
    private int timeEnRoute;

    @Column(name = "wait_time")
    private int waitTime;
}
