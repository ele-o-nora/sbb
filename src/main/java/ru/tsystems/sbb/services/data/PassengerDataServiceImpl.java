package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.model.dao.AdminDao;
import ru.tsystems.sbb.model.dao.PassengerDao;
import ru.tsystems.sbb.model.dao.ScheduleDao;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.Passenger;
import ru.tsystems.sbb.model.entities.Role;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.Ticket;
import ru.tsystems.sbb.model.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class PassengerDataServiceImpl implements PassengerDataService {

    @Autowired
    private PassengerDao passengerDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(final String firstName, final String lastName,
                         final LocalDate dateOfBirth, final String email,
                         final String password) {
        Passenger passenger = new Passenger();
        passenger.setFirstName(firstName);
        passenger.setLastName(lastName);
        passenger.setDateOfBirth(dateOfBirth);
        passengerDao.add(passenger);
        User user = new User();
        user.setEmail(email);
        user.setPassenger(passenger);
        user.setPassword(passwordEncoder.encode(password));
        Set<Role> roles = new HashSet<>();
        Role userRole = passengerDao.getRoleByName("USER");
        roles.add(userRole);
        user.setRoles(roles);
        passengerDao.add(user);
    }

    @Override
    public float getTicketPrice(final int journeyId,
                                final String stationFrom,
                                final String stationTo) {
        Journey journey = passengerDao.getJourneyById(journeyId);
        Station from = scheduleDao.getStationByName(stationFrom);
        Station to = scheduleDao.getStationByName(stationTo);
        Line line = journey.getRoute().getLine();
        int fromOrder = adminDao.getStationOrder(line, from);
        int toOrder = adminDao.getStationOrder(line, to);
        int distance;
        if (fromOrder > toOrder) {
            distance = adminDao.inboundDistance(from, to, line);
        } else {
            distance = adminDao.outboundDistance(from, to, line);
        }
        float tariff = passengerDao.getCurrentTariff();
        int tenLeaguesSections = distance / 30 + 1;
        return tenLeaguesSections * tariff;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void buyTicket(final int journeyId, final int stopFromId,
                          final int stopToId, final float price,
                          final String firstName, final String lastName,
                          final LocalDate dateOfBirth) {
        Journey journey = passengerDao.getJourneyById(journeyId);
        ScheduledStop from = passengerDao.getStopById(stopFromId);
        ScheduledStop to = passengerDao.getStopById(stopToId);
        Passenger passenger = passengerDao
                .getPassengerByInfo(firstName, lastName, dateOfBirth);
        if (passengerDao.currentTickets(journey, from, to)
                < journey.getTrainType().getSeats() &&
                ChronoUnit.MINUTES.between(LocalDateTime.now(),
                        from.getDeparture()) >= 10  &&
                (passenger == null || passengerDao
                        .getTicket(journey, passenger) == null)) {
            Ticket ticket = new Ticket();
            ticket.setJourney(journey);
            ticket.setFrom(from);
            ticket.setTo(to);
            ticket.setPrice(price);
            if (passenger != null) {
                ticket.setPassenger(passenger);
            } else {
                Passenger newPassenger = new Passenger();
                newPassenger.setFirstName(firstName);
                newPassenger.setLastName(lastName);
                newPassenger.setDateOfBirth(dateOfBirth);
                passengerDao.add(newPassenger);
                ticket.setPassenger(newPassenger);
            }
            passengerDao.add(ticket);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void buyTicket(final int journeyId, final int stopFromId,
                          final int stopToId, final float price,
                          final int passengerId) {
        Journey journey = passengerDao.getJourneyById(journeyId);
        ScheduledStop from = passengerDao.getStopById(stopFromId);
        ScheduledStop to = passengerDao.getStopById(stopToId);
        Passenger passenger = passengerDao.getPassengerById(passengerId);
        if (passengerDao.currentTickets(journey, from, to)
                < journey.getTrainType().getSeats() &&
                ChronoUnit.MINUTES.between(LocalDateTime.now(),
                        from.getDeparture()) >= 10  &&
                passengerDao.getTicket(journey, passenger) == null) {
            Ticket ticket = new Ticket();
            ticket.setJourney(journey);
            ticket.setFrom(from);
            ticket.setTo(to);
            ticket.setPrice(price);
            ticket.setPassenger(passenger);
            passengerDao.add(ticket);
        }
    }


}
