package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Station extends AbstractEntity {

    @Column
    private String name;

    @Column
    private int x;

    @Column
    private int y;

    @OneToMany(mappedBy = "station")
    private List<LineStation> lines;

    @OneToMany(mappedBy = "station")
    private List<ScheduledStop> trains;

    @OneToMany(mappedBy = "destination")
    private List<Journey> routesTo;

    @OneToMany(mappedBy = "station")
    private List<RouteStation> routes;

    @OneToMany(mappedBy = "firstStation")
    private List<StationsDistance> distancesFrom;

    @OneToMany(mappedBy = "secondStation")
    private List<StationsDistance> distancesTo;

}
