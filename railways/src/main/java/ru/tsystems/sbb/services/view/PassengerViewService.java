package ru.tsystems.sbb.services.view;

import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;

import java.time.LocalDate;
import java.util.Map;

/**
 * Interaction with data service regarding users, passengers and tickets
 *  and filling necessary attribute maps.
 */
public interface PassengerViewService {

    /**
     * Calls to data service to process registration. Returns map containing
     *  confirmation of successful registration.
     * @param firstName first name of the passenger to register
     * @param lastName last name of the passenger to register
     * @param dateOfBirth date of birth of the passenger to register
     * @param email email of the user to register
     * @param password password of the user to register
     * @return map that contains confirmation of successful registration
     */
    Map<String, Object> register(String firstName, String lastName,
                                 LocalDate dateOfBirth, String email,
                                 String password);

    /**
     * Calls to data service to get an object containing necessary travel
     *  information to process ticket sale. Returns map containing either
     *  this object or the reason of failure.
     * @param journeyId id indicating for which journey the ticket will be
     * @param stationFrom name of the origin station for the ticket
     * @param stationTo name of the destination station for the ticket
     * @return map that contains TicketOrderDto or reason why sale can't happen
     */
    Map<String, Object> prepTicketSale(int journeyId,
                                               String stationFrom,
                                               String stationTo);

    /**
     * Calls to data service to get an object containing necessary travel
     *  information to process the sale of two tickets simultaneously.
     *  Returns map containing this object or the reason of failure.
     * @param firstJourneyId id of the journey that the first ticket will be for
     * @param secondJourneyId id of the journey for the second ticket
     * @param stationFrom name of the origin station for the first ticket
     * @param stationTo name of the destination station for the second ticket
     * @param transfer name of first destination and second origin
     * @return map containing TransferTicketOrderDto or the reason of failure
     */
    Map<String, Object> prepTicketsSale(int firstJourneyId,
                                                int secondJourneyId,
                                                String stationFrom,
                                                String stationTo,
                                                String transfer);

    /**
     * Calls to data service to process ticket sale. Returns either confirmation
     *  of successful sale or the reason why the sale failed to complete.
     * @param ticketOrder object containing travel info for the ticket
     * @param firstName first name of the passenger
     * @param lastName last name of the passenger
     * @param dateOfBirth date of birth of the passenger
     * @return map containing the result of the sale (success or failure)
     */
    Map<String, Object> finalizeTicketSale(TicketOrderDto ticketOrder,
                                               String firstName,
                                               String lastName,
                                               LocalDate dateOfBirth);

    /**
     * Calls to data service to process the sale of two tickets simultaneously.
     *  Returns either confirmation of successful sale or the reason why
     *  the sale couldn't have been completed.
     * @param order object containing travel info for both tickets
     * @param firstName first name of the passenger
     * @param lastName last name of the passenger
     * @param dateOfBirth date of birth of the passenger
     * @return map containing the result of the sale (success or failure)
     */
    Map<String, Object> finalizeTicketsSale(TransferTicketOrderDto order,
                                                String firstName,
                                                String lastName,
                                            LocalDate dateOfBirth);

    /**
     * Gets from data service passenger details for the current user. Returns
     *  map containing said info and empty objects for filling out forms to
     *  either update passenger details or change password.
     * @return map containing passenger info and two auxiliary model attributes
     *  for filling out forms
     * @see ru.tsystems.sbb.model.dto.ChangeNameDto
     * @see ru.tsystems.sbb.model.dto.PasswordDto
     */
    Map<String, Object> editUserInfo();

    /**
     * Calls to data service to process updating passenger name for current
     *  user. Returns map containing updated passenger details, two empty
     *  objects for filling out forms to change name or password and
     *  confirmation of successful update.
     * @param firstName new first name of the passenger
     * @param lastName new last name of the passenger
     * @return map containing passenger info and two auxiliary model attributes
     *  for filling out forms
     */
    Map<String, Object> changeName(String firstName, String lastName);

    /**
     * Calls to data service to process changing current user's password.
     *  Returns map containing passenger details, two empty objects for
     *  filling out forms to change name or password and confirmation of
     *  successful password change.
     * @param newPassword new password
     * @return map containing passenger info and two auxiliary model attributes
     *  for filling out forms
     */
    Map<String, Object> changePassword(String newPassword);

    /**
     * Gets from data service list of tickets that belong to current user and
     *  fall onto specific page with limited number of entries on each page.
     *  Returns map containing tickets and links to previous and next pages
     *  if necessary.
     * @param page page on which the tickets must fall
     * @return map containing list of tickets and links to previous/next pages
     */
    Map<String, Object> getUserTickets(int page);

    /**
     * Calls to data service to process the return of the ticket with specific
     *  id. Returns map containing the confirmation of successful return or
     *  the reason for the failure.
     * @param ticketId id of the ticket to be returned
     * @return map containing the result of the ticket return
     */
    Map<String, Object> returnTicket(int ticketId);

    /**
     * Returns map containing the list of stations and auxiliary object that
     *  indicates unsuccessful sign up attempt.
     * @return map containing list of stations and error indication
     */
    Map<String, Object> failedSignUp();

    /**
     * If possible, returns passenger details for the current user.
     * @return map with passenger info or model attribute for sign up form
     */
    Map<String, Object> prepBuyerInfo();
}
