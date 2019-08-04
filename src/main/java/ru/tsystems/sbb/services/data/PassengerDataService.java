package ru.tsystems.sbb.services.data;

import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;

import java.time.LocalDate;

public interface PassengerDataService {
    void register(String firstName, String lastName, LocalDate dateOfBirth,
                  String email, String password);
    boolean buyTicket(int journeyId, int stopFromId, int stopToId, float price,
                   String firstName, String lastName, LocalDate dateOfBirth);
    boolean buyTicket(int journeyId, int stopFromId, int stopToId, float price,
                   String email);
    PassengerDto getPassenger(String email);
    boolean buyTickets(int firstJourneyId, int secondJourneyId, int stopFromId,
                       int stopToId, int transferArrivalId,
                       int transferDepartId, float firstPrice,
                       float secondPrice, String firstName, String lastName,
                       LocalDate dateOfBirth);
    boolean buyTickets(int firstJourneyId, int secondJourneyId, int stopFromId,
                       int stopToId, int transferArrivalId,
                       int transferDepartId, float firstPrice,
                       float secondPrice, String email);
    TicketOrderDto prepareTicketOrder(int journeyId, String stationFrom,
                                      String stationTo);
    TransferTicketOrderDto prepareTicketsOrder(int firstJourneyId,
                                               int secondJourneyId,
                                               String stationFrom,
                                               String stationTo,
                                               String transfer);
}
