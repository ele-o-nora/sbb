package ru.tsystems.sbb.services.data;

import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;

import java.time.LocalDate;

public interface PassengerDataService {
    void register(String firstName, String lastName, LocalDate dateOfBirth,
                  String email, String password);
    boolean buyTicket(TicketOrderDto ticket, String firstName, String lastName,
                      LocalDate dateOfBirth);
    PassengerDto getPassenger(String email);
    boolean buyTickets(TransferTicketOrderDto tickets, String firstName, String lastName,
                       LocalDate dateOfBirth);
    TicketOrderDto prepareTicketOrder(int journeyId, String stationFrom,
                                      String stationTo);
    TransferTicketOrderDto prepareTicketsOrder(int firstJourneyId,
                                               int secondJourneyId,
                                               String stationFrom,
                                               String stationTo,
                                               String transfer);
}
