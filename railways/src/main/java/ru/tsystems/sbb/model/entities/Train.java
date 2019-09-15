package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Model of a train.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Train extends AbstractEntity {

    /**
     * Unique identifier for this train type.
     */
    @Column(nullable = false, unique = true)
    private String model;

    /**
     * Number of seats this train has.
     */
    @Column(nullable = false)
    private int seats;

    /**
     * Maximum speed this train can travel with.
     */
    @Column(nullable = false)
    private int speed;

    /**
     * List of journeys that are served by this train type.
     */
    @OneToMany(mappedBy = "trainType")
    private List<Journey> journeys;
}
