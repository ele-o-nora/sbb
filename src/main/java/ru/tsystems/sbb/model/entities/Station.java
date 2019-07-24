package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String name;

    @Column
    private int zone;

    @ManyToMany
    @JoinTable(name = "RouteStation",
            joinColumns = @JoinColumn(name = "station_id"),
            inverseJoinColumns = @JoinColumn(name = "route_id"))
    private Set<Route> routes;

    @OneToMany(mappedBy = "station")
    private List<LineStation> lines;

    @OneToMany(mappedBy = "station", fetch = FetchType.EAGER)
    private List<Schedule> trains;

    @OneToMany(mappedBy = "from")
    private List<Ticket> ticketsFrom;

    @OneToMany(mappedBy = "to")
    private List<Ticket> ticketsTo;
}
