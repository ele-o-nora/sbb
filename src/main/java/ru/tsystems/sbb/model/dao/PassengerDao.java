package ru.tsystems.sbb.model.dao;

import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Passenger;
import ru.tsystems.sbb.model.entities.Role;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Ticket;
import ru.tsystems.sbb.model.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    List<Ticket> getTickets(Journey journey, Passenger passenger);
    List<Journey> getJourneys(LocalDateTime start, int page);
    List<Ticket> getTickets(Journey journey, int page);
    int journeysCount(LocalDateTime start);
}
