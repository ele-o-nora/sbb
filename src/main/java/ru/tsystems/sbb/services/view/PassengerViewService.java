package ru.tsystems.sbb.services.view;

import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;

import java.time.LocalDate;
import java.util.Map;

public interface PassengerViewService {
    Map<String, Object> register(String firstName, String lastName,
                                 LocalDate dateOfBirth, String email,
                                 String password);
    Map<String, Object> prepTicketSale(int journeyId,
                                               String stationFrom,
                                               String stationTo);
    Map<String, Object> prepTicketsSale(int firstJourneyId,
                                                int secondJourneyId,
                                                String stationFrom,
                                                String stationTo,
                                                String transfer);
    Map<String, Object> finalizeTicketSale(TicketOrderDto ticketOrder,
                                               String firstName,
                                               String lastName,
                                               String dateOfBirth);
    Map<String, Object> finalizeTicketsSale(TransferTicketOrderDto order,
                                                String firstName,
                                                String lastName,
                                                String dateOfBirth);
    Map<String, Object> editUserInfo();
    Map<String, Object> changeName(String firstName, String lastName);
    Map<String, Object> changePassword(String newPassword);
    Map<String, Object> getUserTickets(int page);
    Map<String, Object> returnTicket(int ticketId);
    Map<String, Object> failedSignUp();
}
