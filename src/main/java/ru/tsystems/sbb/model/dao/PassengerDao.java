package ru.tsystems.sbb.model.dao;

import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Passenger;
import ru.tsystems.sbb.model.entities.Role;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Ticket;
import ru.tsystems.sbb.model.entities.User;

import java.time.LocalDate;

public interface PassengerDao {
    User getUserByEmail(String email);
    void add(User user);
    void add(Passenger passenger);
    Role getRoleByName(String name);
    void add(Ticket ticket);
    float getCurrentTariff();
    Journey getJourneyById(int id);
    int currentTickets(Journey journey, ScheduledStop from, ScheduledStop to);
    ScheduledStop getStopById(int id);
    Passenger getPassengerByInfo(String firstName, String lastName,
                                 LocalDate dateOfBirth);
    Passenger getUserPassenger(User user);
    Ticket getTicket(Journey journey, Passenger passenger);
}
