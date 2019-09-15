package ru.tsystems.sbb.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * Role associated with particular set of permissions.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Role extends AbstractEntity {

    /**
     * Name of the role.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * List of registered users that have this role.
     */
    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;
}
