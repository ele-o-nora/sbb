package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Line extends AbstractEntity {

    @Column
    private String name;

    @OneToMany(mappedBy = "line")
    private List<Route> routes;

    @OneToMany(mappedBy = "line")
    private List<LineStation> stations;

}
