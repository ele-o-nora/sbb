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

/**
 * Direct interaction with database regarding users, passengers and tickets.
 */
public interface PassengerDao {

    /**
     * Gets from database user with specific email.
     * @param email email indicating which user will be returned
     * @return User with specified email
     */
    User getUserByEmail(String email);

    /**
     * Adds new user to database.
     * @param user user to add to the database
     */
    void add(User user);

    /**
     * Adds new passenger to database.
     * @param passenger passenger to add to the database
     */
    void add(Passenger passenger);

    /**
     * Gets from database role with specific name.
     * @param name name indicating which role to return
     * @return Role with specified name
     */
    Role getRoleByName(String name);

    /**
     * Adds new ticket to database.
     * @param ticket ticket to add to the database
     */
    void add(Ticket ticket);

    /**
     * Gets from database the tariff with the latest timestamp.
     * @return float value of latest tariff in the database
     */
    float getCurrentTariff();

    /**
     * Gets from database scheduled journey with specific id.
     * @param id id indicating which journey to return
     * @return Journey identified by specified id
     */
    Journey getJourneyById(int id);

    /**
     * Gets from database the quantity of the tickets for specific journey
     *  which cover partially or fully the section of the journey
     *  from specific stop to specific stop.
     * @param journey journey for which the tickets are counted
     * @param from stop indicating the beginning of the section in question
     * @param to stop indicating the end of the section
     * @return integer value of number of the tickets that cover specified
     *  section of the specified journey
     */
    int currentTickets(Journey journey, ScheduledStop from, ScheduledStop to);

    /**
     * Gets from database scheduled stop with specific id.
     * @param id id indicating which scheduled stop to return
     * @return ScheduledStop identified by specified id
     */
    ScheduledStop getStopById(int id);

    /**
     * Gets from database passenger with specific first name, last name
     *  and date of birth.
     * @param firstName first name of the passenger to return
     * @param lastName last name of the passenger to return
     * @param dateOfBirth date of birth of the passenger to return
     * @return Passenger identified by specified parameters
     */
    Passenger getPassengerByInfo(String firstName, String lastName,
                                 LocalDate dateOfBirth);

    /**
     * Gets from database passenger associated with specific user.
     * @param user user for whom passenger will be returned
     * @return Passenger linked to specified user
     */
    Passenger getUserPassenger(User user);

    /**
     * Gets from database list of tickets for specific journey
     *  and specific passenger.
     * @param journey journey for which tickets will be returned
     * @param passenger passenger for whom tickets will be returned
     * @return list of Ticket for specified journey and specified passenger
     */
    List<Ticket> getTickets(Journey journey, Passenger passenger);

    /**
     * Gets from database the list of scheduled journeys that depart during
     *  one calendar day starting with specific moment in time, those that fall
     *  onto specific page with limited number of entries on one page.
     * @param start moment in time indicating the beginning of the search period
     * @param page page on which the journeys that will be returned must fall
     * @param searchStep number of entries on one page
     * @return list of Journey that depart during one calendar day starting with
     *  specified moment in time and fall onto specified page
     */
    List<Journey> getJourneys(LocalDateTime start, int page, int searchStep);

    /**
     * Gets from database the list of tickets that are associated with specific
     *  journey, those that fall onto specific page with limited number
     *  of entries on one page.
     * @param journey journey for which the tickets will be returned
     * @param page page on which the tickets in question must fall
     * @param searchStep number of entries on one page
     * @return list of Ticket for specified journey that fall onto specified
     *  page
     */
    List<Ticket> getTickets(Journey journey, int page, int searchStep);

    /**
     * Gets from database the quantity of scheduled journeys that depart during
     *  one calendar day starting with specific moment in time.
     * @param start moment in time indicating the beginning of the search period
     * @return integer value of number of the journeys that depart during one
     *  calendar day starting with specified moment in time
     */
    int journeysCount(LocalDateTime start);

    /**
     * Updates specific passenger in the database.
     * @param passenger passenger to be updated
     */
    void update(Passenger passenger);

    /**
     * Updates specific user in the database.
     * @param user user to be updated
     */
    void update(User user);

    /**
     * Gets from database the list of tickets that are linked to specific
     *  passenger, those that fall onto specific page with limited number
     *  of entries on one page.
     * @param passenger passenger for whom tickets will be returned
     * @param page page on which the tickets in question must fall
     * @param searchStep number of entries on one page
     * @return list of Ticket that belong to the specified passenger and fall
     *  onto specified page
     */
    List<Ticket> getPassengerTickets(Passenger passenger, int page,
                                     int searchStep);

    /**
     * Gets from database the quantity of the tickets that are linked to
     *  specific passenger.
     * @param passenger passenger for whom the tickets will be counted
     * @return integer value of number of the tickets that belong to the
     *  specified passenger
     */
    int ticketsCount(Passenger passenger);

    /**
     * Gets from database ticket identified by specific id.
     * @param id id indicating the ticket that will be returned
     * @return Ticket with specified id
     */
    Ticket getTicketById(int id);

    /**
     * Removes from database specific ticket.
     * @param ticket ticket to be removed
     */
    void delete(Ticket ticket);
}
