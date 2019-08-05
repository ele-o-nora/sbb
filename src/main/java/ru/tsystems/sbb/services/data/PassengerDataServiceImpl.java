package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.model.dao.AdminDao;
import ru.tsystems.sbb.model.dao.PassengerDao;
import ru.tsystems.sbb.model.dao.ScheduleDao;
import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.Passenger;
import ru.tsystems.sbb.model.entities.Role;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.Ticket;
import ru.tsystems.sbb.model.entities.User;
import ru.tsystems.sbb.model.mappers.EntityToDtoMapper;

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

    @Autowired
    private EntityToDtoMapper mapper;

    @Override
    public void register(final String firstName, final String lastName,
                         final LocalDate dateOfBirth, final String email,
                         final String password) {
        Passenger passenger = passengerDao
                .getPassengerByInfo(firstName, lastName, dateOfBirth);
        if (passenger == null) {
            passenger = new Passenger();
            passenger.setFirstName(firstName);
            passenger.setLastName(lastName);
            passenger.setDateOfBirth(dateOfBirth);
            passengerDao.add(passenger);
        }
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

    private float getTicketPrice(final int journeyId,
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
    public boolean buyTicket(final TicketOrderDto ticketOrder,
                          final String firstName, final String lastName,
                          final LocalDate dateOfBirth) {
        Journey journey = passengerDao.getJourneyById(ticketOrder
                .getJourney().getId());
        ScheduledStop from = passengerDao.getStopById(ticketOrder
                .getOrigin().getId());
        ScheduledStop to = passengerDao.getStopById(ticketOrder
                .getDestination().getId());
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
            ticket.setPrice(ticketOrder.getPrice());
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
            return true;
        } else {
            return false;
        }
    }

    @Override
    public TicketOrderDto prepareTicketOrder(final int journeyId,
                                             final String stationFrom,
                                             final String stationTo) {
        Journey journey = passengerDao.getJourneyById(journeyId);
        TicketOrderDto ticketOrder = new TicketOrderDto();
        ticketOrder.setJourney(mapper.convert(journey));
        float price = getTicketPrice(journeyId, stationFrom, stationTo);
        ticketOrder.setPrice(price);
        ticketOrder.setFormattedPrice(price);
        ScheduledStop stopFrom = journey.getStops().stream()
                .filter(scheduledStop -> scheduledStop.getStation()
                        .getName().equals(stationFrom)).findFirst()
                .orElse(new ScheduledStop());
        ticketOrder.setOrigin(mapper.convert(stopFrom));
        ScheduledStop stopTo = journey.getStops().stream()
                .filter(scheduledStop -> scheduledStop.getStation()
                        .getName().equals(stationTo)).findFirst()
                .orElse(new ScheduledStop());
        ticketOrder.setDestination(mapper.convert(stopTo));
        return ticketOrder;
    }

    @Override
    public TransferTicketOrderDto prepareTicketsOrder(final int firstJourneyId,
                                                      final int secondJourneyId,
                                                      final String stationFrom,
                                                      final String stationTo,
                                                      final String transfer) {
        TransferTicketOrderDto transferTickets = new TransferTicketOrderDto();
        transferTickets.setFirstTrain(prepareTicketOrder(firstJourneyId,
                stationFrom, transfer));
        transferTickets.setSecondTrain(prepareTicketOrder(secondJourneyId,
                transfer, stationTo));
        return transferTickets;
    }

    @Override
    public PassengerDto getPassenger(final String email) {
        User user = passengerDao.getUserByEmail(email);
        return mapper.convert(passengerDao.getUserPassenger(user));
    }

    @Override
    public boolean buyTickets(final TransferTicketOrderDto tickets,
                              final String firstName,
                              final String lastName,
                              final LocalDate dateOfBirth) {
        return buyTicket(tickets.getFirstTrain(), firstName, lastName,
                dateOfBirth) && buyTicket(tickets.getSecondTrain(),
                firstName, lastName, dateOfBirth);
    }

}
