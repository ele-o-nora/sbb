package ru.tsystems.sbb.services.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.tsystems.sbb.model.dto.ChangeNameDto;
import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.sbb.model.dto.PasswordDto;
import ru.tsystems.sbb.model.dto.SignUpDto;
import ru.tsystems.sbb.model.dto.StationDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;
import ru.tsystems.sbb.services.data.PassengerDataService;
import ru.tsystems.sbb.services.data.RouteDataService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ISO_LOCAL_DATE;
    private static final int MIN_PASSENGER_AGE = 10;

    private static final String SIGN_UP_SUCCESS = "Registration successful. "
            + "You may now sign in.";
    private static final String UPDATE_SUCCESS = "Your account "
            + "was successfully updated.";
    private static final String UPDATE_FAIL = "Failed to update your account. ";
    private static final String BAD_INPUT = "Please make sure"
            + "you fill all the required fields correctly.";
    private static final String TICKET_SUCCESS = "Ticket sale successful. "
            + "Thank you for traveling with us.";
    private static final String TICKET_FAIL = "Couldn't complete ticket sale. ";
    private static final String TICKET_PREP_FAIL = "Couldn't prepare "
            + "ticket sale. ";
    private static final String TICKET_RETURN_FAIL = "Couldn't return ticket. ";
    private static final String TICKET_RETURN_SUCCESS = "Ticket successfully "
            + "returned. Your refund will be processed shortly.";
    private static final String SUCCESS = "success";
    private static final String STATUS = "status";
    private static final String PASSENGER = "passenger";

    @Override
    public Map<String, Object> register(final String firstName,
                                        final String lastName,
                                        final LocalDate dateOfBirth,
                                        final String email,
                                        final String password) {
        Map<String, Object> objects = getStations();
        passengerDataService.register(firstName, lastName, dateOfBirth,
                email, password);
        objects.put(STATUS, SIGN_UP_SUCCESS);
        return objects;
    }

    @Override
    public Map<String, Object> failedSignUp() {
        Map<String, Object> objects = new HashMap<>();
        List<StationDto> stations = routeDataService.allStations();
        objects.put("stations", stations);
        objects.put("failedSignUp", "failedSignUp");
        return objects;
    }

    @Override
    public Map<String, Object> prepTicketSale(final int journeyId,
                                              final String stationFrom,
                                              final String stationTo) {
        Map<String, Object> objects = prepSignUp();
        TicketOrderDto ticketOrder = passengerDataService
                .prepareTicketOrder(journeyId, stationFrom, stationTo);
        if (ticketOrder.getStatus() != null && !ticketOrder
                .getStatus().isEmpty()) {
            objects.put(STATUS, TICKET_PREP_FAIL + ticketOrder.getStatus());
            return objects;
        }
        objects.put("ticketOrder", ticketOrder);
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            PassengerDto passenger = passengerDataService
                    .getPassenger(auth.getName());
            objects.put(PASSENGER, passenger);
        }
        return objects;
    }

    @Override
    public Map<String, Object> prepTicketsSale(final int firstJourneyId,
                                               final int secondJourneyId,
                                               final String stationFrom,
                                               final String stationTo,
                                               final String transfer) {
        Map<String, Object> objects = prepSignUp();
        TransferTicketOrderDto transferTickets = passengerDataService
                .prepareTicketsOrder(firstJourneyId, secondJourneyId,
                        stationFrom, stationTo, transfer);
        if (transferTickets.getFirstTrain().getStatus() != null
                && !transferTickets.getFirstTrain().getStatus().isEmpty()) {
            objects.put(STATUS, TICKET_PREP_FAIL + transferTickets
                    .getFirstTrain().getStatus());
            return objects;
        } else if (transferTickets.getSecondTrain().getStatus() != null
                && !transferTickets.getSecondTrain().getStatus().isEmpty()) {
            objects.put(STATUS, TICKET_PREP_FAIL + transferTickets
                    .getSecondTrain().getStatus());
            return objects;
        }
        objects.put("transferTickets", transferTickets);
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            PassengerDto passenger = passengerDataService
                    .getPassenger(auth.getName());
            objects.put(PASSENGER, passenger);
        }
        return objects;
    }

    @Override
    public Map<String, Object> finalizeTicketSale(
            final TicketOrderDto ticketOrder, final String firstName,
            final String lastName, final String dateOfBirth) {
        Map<String, Object> objects = getStations();
        if (!validateName(firstName) || !validateName(lastName)) {
            objects.put(STATUS, TICKET_FAIL + BAD_INPUT);
            return objects;
        }
        try {
            LocalDate dob = LocalDate.parse(dateOfBirth, DATE_FORMATTER);
            if (!validateDateOfBirth(dob)) {
                objects.put(STATUS, TICKET_FAIL + BAD_INPUT);
                return objects;
            }
            String saleResult = passengerDataService.buyTicket(ticketOrder,
                    firstName, lastName, dob);
            if (saleResult.equalsIgnoreCase(SUCCESS)) {
                objects.put(STATUS, TICKET_SUCCESS);
            } else {
                objects.put(STATUS, TICKET_FAIL + saleResult);
            }
        } catch (Exception e) {
            objects.put(STATUS, TICKET_FAIL);
        }
        return objects;
    }

    @Override
    public Map<String, Object> finalizeTicketsSale(
            final TransferTicketOrderDto order, final String firstName,
            final String lastName, final String dateOfBirth) {
        Map<String, Object> objects = getStations();
        if (!validateName(firstName) || !validateName(lastName)) {
            objects.put(STATUS, TICKET_FAIL + BAD_INPUT);
            return objects;
        }
        try {
            LocalDate dob = LocalDate.parse(dateOfBirth, DATE_FORMATTER);
            if (!validateDateOfBirth(dob)) {
                objects.put(STATUS, TICKET_FAIL + BAD_INPUT);
                return objects;
            }
            String saleResult = passengerDataService.buyTickets(order,
                    firstName, lastName, dob);
            if (saleResult.equalsIgnoreCase(SUCCESS)) {
                objects.put(STATUS, TICKET_SUCCESS);
            } else {
                objects.put(STATUS, TICKET_FAIL + saleResult);
            }
        } catch (Exception e) {
            objects.put(STATUS, TICKET_FAIL);
        }
        return objects;
    }

    @Override
    public Map<String, Object> editUserInfo() {
        Map<String, Object> objects = new HashMap<>();
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        PassengerDto passenger = passengerDataService
                .getPassenger(auth.getName());
        objects.put(PASSENGER, passenger);
        objects.put("changeNameDto", new ChangeNameDto());
        objects.put("passwordDto", new PasswordDto());
        return objects;
    }

    @Override
    public Map<String, Object> changeName(final String firstName,
                                          final String lastName) {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        Map<String, Object> objects = editUserInfo();
        if (!validateName(firstName) || !validateName(lastName)) {
            objects.put(STATUS, UPDATE_FAIL + BAD_INPUT);
            return objects;
        }
        try {
            PassengerDto passenger = passengerDataService
                    .changePassengerInfo(firstName, lastName, auth.getName());
            objects.put(PASSENGER, passenger);
        } catch (Exception e) {
            objects.put(STATUS, UPDATE_FAIL);
            return objects;
        }
        objects.put(STATUS, UPDATE_SUCCESS);
        return objects;
    }

    @Override
    public Map<String, Object> changePassword(final String newPassword) {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        Map<String, Object> objects = editUserInfo();
        passengerDataService.changePassword(auth.getName(), newPassword);
        objects.put(STATUS, UPDATE_SUCCESS);
        return objects;
    }

    @Override
    public Map<String, Object> getUserTickets(final int page) {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        Map<String, Object> objects = new HashMap<>();
        List<TicketDto> tickets = passengerDataService
                .getUserTickets(auth.getName(), page);
        objects.put("tickets", tickets);
        if (page > 1) {
            objects.put("previousPage", (page - 1));
        }
        int maxPages = passengerDataService.maxUserTicketPages(auth.getName());
        if (page < maxPages) {
            objects.put("nextPage", (page + 1));
        }
        return objects;
    }

    @Override
    public Map<String, Object> returnTicket(final int ticketId) {
        String returnResult = passengerDataService.returnTicket(ticketId);
        Map<String, Object> objects = getUserTickets(1);
        if (returnResult.equalsIgnoreCase(SUCCESS)) {
            objects.put(STATUS, TICKET_RETURN_SUCCESS);
        } else {
            objects.put(STATUS, TICKET_RETURN_FAIL + returnResult);
        }
        return objects;
    }

    private Map<String, Object> prepSignUp() {
        Map<String, Object> objects = new HashMap<>();
        objects.put("signUpDto", new SignUpDto());
        return objects;
    }

    private boolean validateName(final String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        Pattern p = Pattern.compile("[A-Za-z\\s-']{2,30}");
        Matcher m = p.matcher(name);
        return m.find();
    }

    private boolean validateDateOfBirth(final LocalDate dateOfBirth) {
        return ChronoUnit.YEARS.between(dateOfBirth,
                LocalDate.now()) >= MIN_PASSENGER_AGE;
    }

    private Map<String, Object> getStations() {
        Map<String, Object> objects = prepSignUp();
        List<StationDto> stations = routeDataService.allStations();
        objects.put("stations", stations);
        return objects;
    }

}
