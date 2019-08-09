package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.model.dao.AdminDao;
import ru.tsystems.sbb.model.dao.PassengerDao;
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
    private AdminDao adminDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityToDtoMapper mapper;

    private static final String NO_TIME = "Not enough time before departure.";
    private static final String NO_SEATS = "No available seats.";
    private static final String SAME_PASSENGER = "You already have a ticket "
            + "for this journey.";
    private static final String SUCCESS = "success";
    private static final int SEARCH_RESULTS_STEP = 5;
    private static final int BASE_TARIFF_DISTANCE = 30;
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
        int fromOrder = adminDao.getStationOrder(line, from);
        int toOrder = adminDao.getStationOrder(line, to);
        int distance;
        if (fromOrder > toOrder) {
            distance = adminDao.inboundDistance(from, to, line);
        } else {
            distance = adminDao.outboundDistance(from, to, line);
        }
        float tariff = passengerDao.getCurrentTariff();
        int tenLeaguesSections = distance / BASE_TARIFF_DISTANCE + 1;
        return tenLeaguesSections * tariff;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String buyTicket(final TicketOrderDto ticketOrder,
                          final String firstName, final String lastName,
                          final LocalDate dateOfBirth) {
        Journey journey = passengerDao.getJourneyById(ticketOrder
                .getJourney().getId());
        ScheduledStop from = passengerDao.getStopById(ticketOrder
                .getOrigin().getId());
        ScheduledStop to = passengerDao.getStopById(ticketOrder
                .getDestination().getId());
        float price = getTicketPrice(journey.getId(),
                from.getStation().getName(), to.getStation().getName());
        Passenger passenger = getOrCreatePassenger(firstName, lastName,
                dateOfBirth);
        if (ChronoUnit.MINUTES.between(LocalDateTime.now(),
                from.getDeparture()) < MIN_MINUTES) {
            return NO_TIME;
        } else if (!passengerDao.getTickets(journey, passenger).isEmpty()) {
            return SAME_PASSENGER;
        } else if (passengerDao.currentTickets(journey, from, to)
                < journey.getTrainType().getSeats()) {
            Ticket ticket = Ticket.builder().from(from).to(to).journey(journey)
                    .price(price).passenger(passenger).build();
            passengerDao.add(ticket);
            return SUCCESS;
        } else {
            return NO_SEATS;
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
        ticketOrder.setFormattedPrice(mapper.formatPrice(price));
        ScheduledStop stopFrom = journey.getStops().stream()
                .filter(scheduledStop -> scheduledStop.getStation()
                        .getName().equals(stationFrom)).findFirst()
                .orElse(new ScheduledStop());
        if (ChronoUnit.MINUTES.between(LocalDateTime.now(),
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String buyTickets(final TransferTicketOrderDto tickets,
                              final String firstName,
                              final String lastName,
                              final LocalDate dateOfBirth) {
        Journey firstJourney = passengerDao.getJourneyById(tickets
                .getFirstTrain().getJourney().getId());
        Journey secondJourney = passengerDao.getJourneyById(tickets
                .getSecondTrain().getJourney().getId());
        ScheduledStop firstFrom = passengerDao.getStopById(tickets
                .getFirstTrain().getOrigin().getId());
        ScheduledStop firstTo = passengerDao.getStopById(tickets
                .getFirstTrain().getDestination().getId());
        ScheduledStop secondFrom = passengerDao.getStopById(tickets
                .getSecondTrain().getOrigin().getId());
        ScheduledStop secondTo = passengerDao.getStopById(tickets
                .getSecondTrain().getDestination().getId());
        float firstPrice = getTicketPrice(firstJourney.getId(),
                firstFrom.getStation().getName(),
                firstTo.getStation().getName());
        float secondPrice = getTicketPrice(secondJourney.getId(),
                secondFrom.getStation().getName(),
                secondTo.getStation().getName());
        Passenger passenger = getOrCreatePassenger(firstName, lastName,
                dateOfBirth);
        if (ChronoUnit.MINUTES.between(LocalDateTime.now(),
                firstFrom.getDeparture()) < MIN_MINUTES) {
            return NO_TIME;
        } else if (!passengerDao.getTickets(firstJourney, passenger).isEmpty()
        || !passengerDao.getTickets(secondJourney, passenger).isEmpty()) {
            return SAME_PASSENGER;
        } else if (passengerDao.currentTickets(firstJourney, firstFrom,
                firstTo) < firstJourney.getTrainType().getSeats()
                && passengerDao.currentTickets(secondJourney, secondFrom,
                secondTo) < secondJourney.getTrainType().getSeats()) {
            Ticket firstTicket = Ticket.builder().from(firstFrom).to(firstTo)
                    .journey(firstJourney).price(firstPrice)
                    .passenger(passenger).build();
            Ticket secondTicket = Ticket.builder().from(secondFrom).to(secondTo)
                    .journey(secondJourney).price(secondPrice)
                    .passenger(passenger).build();
            passengerDao.add(firstTicket);
            passengerDao.add(secondTicket);
            return SUCCESS;
        }
        return NO_SEATS;
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
    public String returnTicket(final int ticketId) {
        Ticket ticket = passengerDao.getTicketById(ticketId);
        if (ChronoUnit.MINUTES.between(LocalDateTime.now(),
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

}
