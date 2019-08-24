package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Journey extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train trainType;

    @OneToMany(mappedBy = "journey")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "journey")
    @OrderBy("id asc")
    private List<ScheduledStop> stops;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Station destination;

    @Column
    private int delay;

    @Column
    private boolean cancelled;

}
