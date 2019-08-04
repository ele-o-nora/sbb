package ru.tsystems.sbb.services.view;

import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;

import java.util.Map;

public interface PassengerViewService {
    Map<String, Object> register(String firstName, String lastName,
                                 String dateOfBirth, String email,
                                 String password);
    Map<String, Object> prepTicketSaleLogged(int journeyId,
                                               String stationFrom,
                                               String stationTo,
                                               String email);
    Map<String, Object> prepTicketsSaleLogged(int firstJourneyId,
                                                int secondJourneyId,
                                                String stationFrom,
                                                String stationTo,
                                                String transfer,
                                                String email);
    Map<String, Object> prepTicketSaleAnon(int journeyId,
                                               String stationFrom,
                                               String stationTo);
    Map<String, Object> prepTicketsSaleAnon(int firstJourneyId,
                                                int secondJourneyId,
                                                String stationFrom,
                                                String stationTo,
                                                String transfer);
    Map<String, Object> finalizeTicketSaleLogged(TicketOrderDto ticketOrder,
                                                 String email);
    Map<String, Object> finalizeTicketsSaleLogged(TransferTicketOrderDto order,
                                                  String email);
    Map<String, Object> finalizeTicketSaleAnon(TicketOrderDto ticketOrder,
                                               String firstName,
                                               String lastName,
                                               String dateOfBirth);
    Map<String, Object> finalizeTicketsSaleAnon(TransferTicketOrderDto order,
                                                String firstName,
                                                String lastName,
                                                String dateOfBirth);
}
