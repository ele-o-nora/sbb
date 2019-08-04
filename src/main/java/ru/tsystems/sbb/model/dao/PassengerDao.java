package ru.tsystems.sbb.model.dao;

import ru.tsystems.sbb.model.entities.Passenger;
import ru.tsystems.sbb.model.entities.Role;
import ru.tsystems.sbb.model.entities.User;

public interface PassengerDao {
    User getUserByEmail(String email);
    void add(User user);
    void add(Passenger passenger);
    Role getRoleByName(String name);
}
