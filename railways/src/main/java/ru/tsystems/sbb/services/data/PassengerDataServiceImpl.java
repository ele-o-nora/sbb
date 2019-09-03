package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.exceptions.TicketSaleException;
import ru.tsystems.sbb.model.dao.PassengerDao;
import ru.tsystems.sbb.model.dao.RouteDao;
import ru.tsystems.sbb.model.dao.ScheduleDao;
import ru.tsystems.sbb.model.dto.PassengerDto;
import ru.tsystems.sbb.model.dto.TicketDto;
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

import javax.persistence.NoResultException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PassengerDataServiceImpl implements PassengerDataService {

    @Autowired
    private PassengerDao passengerDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private RouteDao routeDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityToDtoMapper mapper;

    @Autowired
    private Clock clock;

    private static final String NO_TIME = "Not enough time before departure.";
    private static final String NO_SEATS = "No available seats.";
    private static final String SAME_PASSENGER = "You already have a ticket "
            + "for this journey.";
    private static final String SUCCESS = "success";
    private static final int SEARCH_RESULTS_STEP = 5;
    private static final double BASE_TARIFF_DISTANCE = 50;
    private static final int MIN_MINUTES = 10;

    @Override
    public void register(final String firstName, final String lastName,
                         final LocalDate dateOfBirth, final String email,
                         final String password) {
        Passenger passenger = getOrCreatePassenger(firstName, lastName,
                dateOfBirth);
        Set<Role> roles = new HashSet<>();
        Role userRole = passengerDao.getRoleByName("USER");
        roles.add(userRole);
        User user = User.builder().email(email).passenger(passenger)
                .password(passwordEncoder.encode(password)).roles(roles)
                .build();
        passengerDao.add(user);
    }

    private float getTicketPrice(final int journeyId,
                                final String stationFrom,
                                final String stationTo) {
        Journey journey = passengerDao.getJourneyById(journeyId);
        Station from = scheduleDao.getStationByName(stationFrom);
        Station to = scheduleDao.getStationByName(stationTo);
        Line line = journey.getRoute().getLine();
        int fromOrder = routeDao.getStationOrder(line, from);
        int toOrder = routeDao.getStationOrder(line, to);
        double distance = 0;
        List<Station> stationsToPass = routeDao
                .getStations(Math.min(fromOrder, toOrder),
                        Math.max(fromOrder, toOrder), line);
        for (int i = 1; i < stationsToPass.size(); i++) {
            distance += calcDistanceBetweenPoints(
                    stationsToPass.get(i - 1).getX(),
                    stationsToPass.get(i - 1).getY(),
                    stationsToPass.get(i).getX(),
                    stationsToPass.get(i).getY());
        }
        float tariff = passengerDao.getCurrentTariff();
        int tenLeagueSections = (int) Math.ceil(distance / BASE_TARIFF_DISTANCE);
        return tenLeagueSections * tariff;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.SERIALIZABLE)
    public boolean buyTicket(final TicketOrderDto ticketOrder,
                          final String firstName, final String lastName,
                          final LocalDate dateOfBirth) {
        Journey journey = passengerDao.getJourneyById(ticketOrder
                .getJourney().getId());
        ScheduledStop from = passengerDao.getStopById(ticketOrder
                .getOrigin().getId());
        ScheduledStop to = passengerDao.getStopById(ticketOrder
                .getDestination().getId());
        Passenger passenger = getOrCreatePassenger(firstName, lastName,
                dateOfBirth);
        if (ChronoUnit.MINUTES.between(LocalDateTime.now(clock),
                from.getDeparture()) < MIN_MINUTES) {
            throw new TicketSaleException(NO_TIME);
        } else if (!passengerDao.getTickets(journey, passenger).isEmpty()) {
            throw new TicketSaleException(SAME_PASSENGER);
        } else if (passengerDao.currentTickets(journey, from, to)
                < journey.getTrainType().getSeats()) {
            Ticket ticket = Ticket.builder().from(from).to(to).journey(journey)
                    .price(ticketOrder.getPrice()).passenger(passenger).build();
            passengerDao.add(ticket);
            return true;
        } else {
            throw new TicketSaleException(NO_SEATS);
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
        ticketOrder.setFormattedPrice(mapper.formatPrice(price));
        ScheduledStop stopFrom = journey.getStops().stream()
                .filter(scheduledStop -> scheduledStop.getStation()
                        .getName().equals(stationFrom)).findFirst()
                .orElse(new ScheduledStop());
        if (ChronoUnit.MINUTES.between(LocalDateTime.now(clock),
                stopFrom.getDeparture()) < MIN_MINUTES) {
            ticketOrder.setStatus(NO_TIME);
            return ticketOrder;
        }
        ticketOrder.setOrigin(mapper.convert(stopFrom));
        ScheduledStop stopTo = journey.getStops().stream()
                .filter(scheduledStop -> scheduledStop.getStation()
                        .getName().equals(stationTo)).findFirst()
                .orElse(new ScheduledStop());
        if (passengerDao.currentTickets(journey, stopFrom, stopTo)
                == journey.getTrainType().getSeats()) {
            ticketOrder.setStatus(NO_SEATS);
            return ticketOrder;
        }
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
    @Transactional(propagation = Propagation.REQUIRES_NEW,
    isolation = Isolation.SERIALIZABLE)
    public boolean buyTickets(final TransferTicketOrderDto tickets,
                              final String firstName,
                              final String lastName,
                              final LocalDate dateOfBirth) {
        return buyTicket(tickets.getFirstTrain(), firstName, lastName,
                dateOfBirth) && buyTicket(tickets.getSecondTrain(), firstName,
                lastName, dateOfBirth);
    }

    @Override
    public PassengerDto changePassengerInfo(final String firstName,
                                            final String lastName,
                                            final String email) {
        User user = passengerDao.getUserByEmail(email);
        Passenger passenger = passengerDao.getUserPassenger(user);
        passenger.setFirstName(firstName);
        passenger.setLastName(lastName);
        passengerDao.update(passenger);
        return mapper.convert(passenger);
    }

    @Override
    public void changePassword(final String email, final String password) {
        User user = passengerDao.getUserByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        passengerDao.update(user);
    }

    @Override
    public int maxUserTicketPages(final String email) {
        User user = passengerDao.getUserByEmail(email);
        Passenger passenger = passengerDao.getUserPassenger(user);
        int ticketsCount = passengerDao.ticketsCount(passenger);
        return (ticketsCount + SEARCH_RESULTS_STEP - 1) / SEARCH_RESULTS_STEP;
    }

    @Override
    public List<TicketDto> getUserTickets(final String email, final int page) {
        User user = passengerDao.getUserByEmail(email);
        Passenger passenger = passengerDao.getUserPassenger(user);
        return passengerDao.getPassengerTickets(passenger, page,
                SEARCH_RESULTS_STEP)
                .stream().map(ticket -> mapper.convert(ticket))
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDto> getPassengerTickets(final int journeyId,
                                               final String firstName,
                                               final String lastName,
                                               final LocalDate dateOfBirth) {
        Journey journey = passengerDao.getJourneyById(journeyId);
        Passenger passenger = passengerDao.getPassengerByInfo(firstName,
                lastName, dateOfBirth);
        return passengerDao.getTickets(journey, passenger).stream()
                .map(ticket -> mapper.convert(ticket))
                .collect(Collectors.toList());
    }

    @Override
    public String returnTicket(final int ticketId) {
        Ticket ticket = passengerDao.getTicketById(ticketId);
        if (ChronoUnit.MINUTES.between(LocalDateTime.now(clock),
                ticket.getFrom().getDeparture()) >= MIN_MINUTES) {
            passengerDao.delete(ticket);
            return SUCCESS;
        } else {
            return NO_TIME;
        }
    }

    private Passenger getOrCreatePassenger(final String firstName,
                                           final String lastName,
                                           final LocalDate dateOfBirth) {
        Passenger passenger;
        try {
            passenger = passengerDao
                    .getPassengerByInfo(firstName, lastName, dateOfBirth);
        } catch (NoResultException e) {
            passenger = new Passenger();
            passenger.setFirstName(firstName);
            passenger.setLastName(lastName);
            passenger.setDateOfBirth(dateOfBirth);
            passengerDao.add(passenger);
        }
        return passenger;
    }

    private double calcDistanceBetweenPoints(final int x1, final int y1,
                                             final int x2, final int y2) {
        double xDiff = Math.abs(x1 - x2);
        double yDiff = Math.abs(y1 - y2);
        return Math.ceil(Math.hypot(xDiff, yDiff));
    }

}
