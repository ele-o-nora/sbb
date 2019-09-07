package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

/**
 * Passenger that can use railway services.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Passenger extends AbstractEntity {

    /**
     * Passenger's legal first name.
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Passenger's legal last name.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Passenger's date of birth.
     */
    @Column(name = "date_of_birth")
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
