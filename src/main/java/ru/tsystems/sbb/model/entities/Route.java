package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String number;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @OneToMany(mappedBy = "route")
    private List<Journey> journeys;

    @OneToMany(mappedBy = "route")
    private List<RouteStation> stations;
}
