package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

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
public class Journey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train trainType;

    @OneToMany(mappedBy = "journey")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "journey")
    private List<Schedule> stops;

    @Formula("select s.name from Station s join Schedule sch on s.id = sch.station_id where sch.journey_id = id order by sch.arrival desc limit 1")
    private String destination;

    @Formula("(select t.seats from Train t where t.id = train_id) - (select count(*) from Ticket t where t.journey_id = id)")
    private int availableSeats;
}
