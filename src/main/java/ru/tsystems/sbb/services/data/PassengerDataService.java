package ru.tsystems.sbb.services.data;

import java.time.LocalDate;

public interface PassengerDataService {
    void register(String firstName, String lastName, LocalDate dateOfBirth,
                  String email, String password);
}
