package ru.tsystems.sbb.services.view;

import ru.tsystems.sbb.model.dto.SignUpDto;

import java.util.Map;

public interface PassengerViewService {
    Map<String, Object> register(SignUpDto signUpDto);
}
