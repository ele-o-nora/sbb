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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class ScheduledStop {

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ScheduleKey implements Serializable {
        static final long serialVersionUID = 1L;
        private int journeyId;
        private int stationId;
    }

    @EmbeddedId
    private ScheduleKey id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "journey_id")
    @MapsId("journeyId")
    private Journey journey;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "station_id")
    @MapsId("stationId")
    private Station station;

    @Column
    private LocalDateTime arrival;

    @Column
    private LocalDateTime departure;

}
