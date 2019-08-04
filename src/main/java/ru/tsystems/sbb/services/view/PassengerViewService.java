package ru.tsystems.sbb.services.view;

import java.util.Map;

public interface PassengerViewService {
    Map<String, Object> register(String firstName, String lastName, String dateOfBirth,
                                 String email, String password);
}
