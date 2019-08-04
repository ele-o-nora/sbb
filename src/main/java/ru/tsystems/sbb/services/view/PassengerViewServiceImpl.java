package ru.tsystems.sbb.services.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;
import ru.tsystems.sbb.services.data.PassengerDataService;
import ru.tsystems.sbb.services.data.RouteDataService;
import ru.tsystems.sbb.services.data.ScheduleDataService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PassengerViewServiceImpl implements PassengerViewService {

    @Autowired
    private PassengerDataService passengerDataService;

    @Autowired
    private RouteDataService routeDataService;

    @Autowired
    private ScheduleDataService scheduleDataService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ISO_LOCAL_DATE;

    private static final String SUCCESS = "Registration successful. You may now sign in.";
    private static final String FAIL = "Registration failed. Please try again.";
    private static final String STATUS = "status";

    @Override
    public Map<String, Object> register(final String firstName,
                                        final String lastName,
                                        final String dateOfBirth,
                                        final String email,
                                        final String password) {
        Map<String, Object> objects = new HashMap<>();
        List<LineDto> lines = routeDataService.getAllLines();
        objects.put("lines", lines);
        if (!validateName(firstName) || !validateName(lastName) ||
                !validatePassword(password) || !validateEmail(email)) {
            objects.put(STATUS, FAIL);
            return objects;
        }
        try {
            LocalDate dob = LocalDate.parse(dateOfBirth,
                    DATE_FORMATTER);
            passengerDataService.register(firstName, lastName, dob,
                    email, password);
            objects.put(STATUS, SUCCESS);
        } catch (Exception e) {
            objects.put(STATUS, FAIL);
        }
        return objects;
    }

    @Override
    public Map<String, Object> prepTicketSaleLogged(final int journeyId,
                                                    final String stationFrom,
                                                    final String stationTo,
                                                    final String email) {
        Map<String, Object> objects = prepTicketSaleAnon(journeyId,
                stationFrom, stationTo);
        PassengerDto passenger = passengerDataService.getPassenger(email);
        objects.put("passenger", passenger);
        return objects;
    }

    @Override
    public Map<String, Object> prepTicketsSaleLogged(final int firstJourneyId,
                                                     final int secondJourneyId,
                                                     final String stationFrom,
                                                     final String stationTo,
                                                     final String transfer,
                                                     final String email) {
        Map<String, Object> objects = prepTicketsSaleAnon(firstJourneyId,
                secondJourneyId, stationFrom, stationTo, transfer);
        PassengerDto passenger = passengerDataService.getPassenger(email);
        objects.put("passenger", passenger);
        return objects;
    }

    @Override
    public Map<String, Object> prepTicketSaleAnon(final int journeyId,
                                                  final String stationFrom,
                                                  final String stationTo) {
        Map<String, Object> objects = new HashMap<>();
        TicketOrderDto ticketOrder = passengerDataService
                .prepareTicketOrder(journeyId, stationFrom, stationTo);
        objects.put("ticketOrder", ticketOrder);
        return objects;
    }

    @Override
    public Map<String, Object> prepTicketsSaleAnon(final int firstJourneyId,
                                                   final int secondJourneyId,
                                                   final String stationFrom,
                                                   final String stationTo,
                                                   final String transfer) {
        Map<String, Object> objects = new HashMap<>();
        TransferTicketOrderDto transferTickets = passengerDataService
                .prepareTicketsOrder(firstJourneyId, secondJourneyId,
                        stationFrom, stationTo, transfer);
        objects.put("transferTickets", transferTickets);
        return objects;
    }

    private boolean validateName(final String name) {
        return name != null && !name.isEmpty();
    }

    private boolean validatePassword(final String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}");
        Matcher m = p.matcher(password);
        return m.find();
    }

    private boolean validateEmail(final String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile("^(.+)@(.+)$");
        Matcher m = p.matcher(email);
        return m.find();
    }

}
