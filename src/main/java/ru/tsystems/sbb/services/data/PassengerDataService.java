package ru.tsystems.sbb.services.data;

import java.time.LocalDate;

public interface PassengerDataService {
    void register(String firstName, String lastName, LocalDate dateOfBirth,
                  String email, String password);
    float getTicketPrice(int journeyId, String stationFrom, String stationTo);
    void buyTicket(int journeyId, int stopFromId, int stopToId, float price,
                   String firstName, String lastName, LocalDate dateOfBirth);
    void buyTicket(int journeyId, int stopFromId, int stopToId, float price,
                   int passengerId);
}
