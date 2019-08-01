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
public class Train extends AbstractEntity {

    @Column
    private String model;

    @Column
    private int seats;

    @Column
    private int speed;

    @OneToMany(mappedBy = "trainType")
    private List<Journey> journeys;
}
