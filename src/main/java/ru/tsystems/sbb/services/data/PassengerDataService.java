package ru.tsystems.sbb.services.data;

import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;

import java.time.LocalDate;
import java.util.List;

public interface PassengerDataService {
    void register(String firstName, String lastName, LocalDate dateOfBirth,
                  String email, String password);
    String buyTicket(TicketOrderDto ticket, String firstName, String lastName,
                      LocalDate dateOfBirth);
    PassengerDto getPassenger(String email);
    String buyTickets(TransferTicketOrderDto tickets, String firstName,
                      String lastName, LocalDate dateOfBirth);
    TicketOrderDto prepareTicketOrder(int journeyId, String stationFrom,
                                      String stationTo);
    TransferTicketOrderDto prepareTicketsOrder(int firstJourneyId,
                                               int secondJourneyId,
                                               String stationFrom,
                                               String stationTo,
                                               String transfer);
    PassengerDto changePassengerInfo(String firstName, String lastName, String email);
    void changePassword(String email, String password);
    List<TicketDto> getUserTickets(String email, int page);
    int maxUserTicketPages(String email);
    String returnTicket(int ticketId);
}
