package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.List;

/**
 * Passenger that can use railway services.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"first_name",
        "last_name", "date_of_birth"}))
public class Passenger extends AbstractEntity {

    /**
     * Passenger's legal first name.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * Passenger's legal last name.
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * Passenger's date of birth.
     */
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    /**
     * Registered account linked to this passenger.
     */
    @OneToOne(mappedBy = "passenger")
    private User user;

    /**
     * List of tickets bought by this passenger.
     */
    @OneToMany(mappedBy = "passenger")
    private List<Ticket> tickets;
}
