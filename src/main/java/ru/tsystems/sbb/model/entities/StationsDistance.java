package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stations_distance")
public class StationsDistance extends AbstractEntity {

    @Column
    private int distance;

    @ManyToOne
    @JoinColumn(name = "first_station")
    private Station firstStation;

    @ManyToOne
    @JoinColumn(name = "second_station")
    private Station secondStation;

}
