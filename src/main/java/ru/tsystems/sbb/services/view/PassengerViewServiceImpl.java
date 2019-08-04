package ru.tsystems.sbb.services.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.SignUpDto;
import ru.tsystems.sbb.services.data.PassengerDataService;
import ru.tsystems.sbb.services.data.RouteDataService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PassengerViewServiceImpl implements PassengerViewService {

    @Autowired
    private PassengerDataService passengerDataService;

    @Autowired
    private RouteDataService routeDataService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ISO_LOCAL_DATE;

    private static final String SUCCESS = "Registration successful. You may now sign in.";
    private static final String FAIL = "Registration failed. Please try again.";

    @Override
    public Map<String, Object> register(final @Valid SignUpDto signUpDto) {
        Map<String, Object> objects = new HashMap<>();
        List<LineDto> lines = routeDataService.getAllLines();
        objects.put("lines", lines);
        try {
            LocalDate dob = LocalDate.parse(signUpDto.getDateOfBirth(),
                    DATE_FORMATTER);
            passengerDataService.register(signUpDto.getFirstName(),
                    signUpDto.getLastName(), dob, signUpDto.getEmail(),
                    signUpDto.getPassword());
            objects.put("status", SUCCESS);
        } catch (Exception e) {
            objects.put("status", FAIL);
        }
        return objects;
    }

}
