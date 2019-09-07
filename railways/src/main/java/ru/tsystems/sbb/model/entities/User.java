package ru.tsystems.sbb.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

/**
 * Registered user account.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class User extends AbstractEntity {

    /**
     * User's e-mail. Serves as unique identifier.
     */
    @Column
    private String email;

    /**
     * User's password.
     */
    @Column
    private String password;

    /**
     * Passenger entity linked to this user.
     */
    @OneToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    /**
     * Set of roles this user has.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}
