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
 * Line representing multiple stations linked by train tracks.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Line extends AbstractEntity {

    /**
     * Unique name that identifies this line.
     */
    @Column
    private String name;

    /**
     * List of routes linked to this line.
     */
    @OneToMany(mappedBy = "line")
    private List<Route> routes;

    /**
     * List of stations the line consists of.
     */
    @OneToMany(mappedBy = "line")
    private List<LineStation> stations;

}
