package ru.tsystems.sbb.services.data;

import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Interaction with DAO regarding users, passengers and tickets.
 */
public interface PassengerDataService {

    /**
     * Creates new passenger with specified first name, last name,
     *  and date of birth or finds an existing one with the same parameters
     *  and creates new user with specified email and encoded password
     *  and links said user to newly created or found passenger.
     * @param firstName first name of the passenger to be found or created
     * @param lastName last name of the passenger to be found or created
     * @param dateOfBirth date of birth of the passenger to be found or created
     * @param email email of the user to be created
     * @param password password of the user to be created
     */
    void register(String firstName, String lastName, LocalDate dateOfBirth,
                  String email, String password);

    /**
     * Creates new ticket for new or existing passenger with specific
     *  first name, last name, and date of birth as long as there is enough
     *  time before departure and seats and passenger in question has not yet
     *  bought a ticket for this specific journey.
     * @param ticket object containing travel information for the ticket
     * @param firstName first name of the passenger to be found or created
     * @param lastName last name of the passenger to be found or created
     * @param dateOfBirth date of birth of the passenger to be found or created
     * @return String indicating either success or reason of failure of the sale
     */
    String buyTicket(TicketOrderDto ticket, String firstName, String lastName,
                      LocalDate dateOfBirth);

    /**
     * Gets from DAO passenger that is linked to the user with specific email.
     *  Returns corresponding DTO.
     * @param email email of user whose passenger details will be returned
     * @return PassengerDto linked to the user with specified email
     */
    PassengerDto getPassenger(String email);

    /**
     * Creates new tickets for both journeys in the ticket order for new or
     *  existing passenger with specific first name, last name and date of birth
     *  as long as there is enough time before departure of the first journey,
     *  enough seats on both journeys and passenger in question has not yet
     *  bought a ticket for any of these two journeys.
     * @param tickets object containing travel information for both tickets
     * @param firstName first name of the passenger to be found or created
     * @param lastName last name of the passenger to be found or created
     * @param dateOfBirth date of birth of the passenger to be found or created
     * @return String indicating either success or reason of failure of the sale
     */
    String buyTickets(TransferTicketOrderDto tickets, String firstName,
                      String lastName, LocalDate dateOfBirth);

    /**
     * Creates and returns object containing travel information for the ticket
     *  for specific journey starting at specific station and ending at
     *  another specific station as long as there is enough time before
     *  departure and enough seats.
     * @param journeyId id of the journey for which ticket is to be bought
     * @param stationFrom name of the origin station for the ticket
     * @param stationTo name of the destination station for the ticket
     * @return object containing either info for the ticket or reason of failure
     * @see TicketOrderDto
     */
    TicketOrderDto prepareTicketOrder(int journeyId, String stationFrom,
                                      String stationTo);

    /**
     * Creates and returns object containing travel information for two tickets
     *  where first journey connects specific station and the transfer station
     *  and second connects the transfer station and another specific station
     *  as long as there is enough time before departure of the first journey
     *  and enough seats on both journeys.
     * @param firstJourneyId id of the first of two journeys
     * @param secondJourneyId id of the second of two journeys
     * @param stationFrom name of the origin station for the first ticket
     * @param stationTo name of the destination station for the second ticket
     * @param transfer name of the station where passenger will change trains
     * @return object containing either travel info or reason of failure
     * @see TransferTicketOrderDto
     */
    TransferTicketOrderDto prepareTicketsOrder(int firstJourneyId,
                                               int secondJourneyId,
                                               String stationFrom,
                                               String stationTo,
                                               String transfer);

    /**
     * Updates passenger details for user with specific email. Returns
     *  DTO representing updated passenger.
     * @param firstName new first name for the passenger
     * @param lastName new last name for the passenger
     * @param email email indicating user for whom details will be updated
     * @return PassengerDto for updated passenger
     */
    PassengerDto changePassengerInfo(String firstName, String lastName, String email);

    /**
     * Sets new encoded password for user with specific email.
     * @param email email indicating whose password is to be changed
     * @param password new password
     */
    void changePassword(String email, String password);

    /**
     * Gets from DAO list of tickets for user with specific email, those that
     *  fall onto specific page with limited number of entries on each page.
     *  Returns list of corresponding DTOs.
     * @param email email indicating for whom tickets will be returned
     * @param page page on which tickets in question must fall
     * @return list of TicketDto for user with specified email that fall onto
     *  specified page
     */
    List<TicketDto> getUserTickets(String email, int page);

    /**
     * Gets from DAO total quantity of tickets for user with specific email
     *  and calculates and returns total number of pages the tickets can fill.
     * @param email email indicating for whom tickets will be counted
     * @return integer value of the number of pages that tickets fill
     */
    int maxUserTicketPages(String email);

    /**
     * Processes ticket return (removal from the database) as long as
     *  there is enough time before departure.
     * @param ticketId id of the ticket to be returned
     * @return String indicating either success or the reason of failure
     */
    String returnTicket(int ticketId);
}
