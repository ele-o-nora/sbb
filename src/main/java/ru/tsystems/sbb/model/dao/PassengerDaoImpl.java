package ru.tsystems.sbb.model.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Passenger;
import ru.tsystems.sbb.model.entities.Role;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Ticket;
import ru.tsystems.sbb.model.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PassengerDaoImpl implements PassengerDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User getUserByEmail(final String email) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from User u where u.email = :email",
                User.class).setParameter("email", email).getSingleResult();
    }

    @Override
    public void add(final User user) {
        sessionFactory.getCurrentSession().persist(user);
    }

    @Override
    public void add(final Passenger passenger) {
        sessionFactory.getCurrentSession().persist(passenger);
    }

    @Override
    public Role getRoleByName(final String name) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Role r where r.name = :name",
                Role.class).setParameter("name", name).getSingleResult();
    }

    @Override
    public void add(final Ticket ticket) {
        sessionFactory.getCurrentSession().persist(ticket);
    }

    @Override
    public float getCurrentTariff() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select t.pricePerTenLeagues "
                        + "from Tariff t order by t.momentFrom desc",
                Float.class).setMaxResults(1).getSingleResult();
    }

    @Override
    public Journey getJourneyById(final int id) {
        return sessionFactory.getCurrentSession().get(Journey.class, id);
    }

    @Override
    public int currentTickets(final Journey journey, final ScheduledStop from,
                              final ScheduledStop to) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select count(distinct t) from Ticket t "
                + "join t.from st1 join t.to st2 "
                + "where t.journey = :journey "
                + "and st1.departure < :arrival "
                + "and st2.arrival > :departure", Long.class)
                .setParameter("journey", journey)
                .setParameter("departure", from.getDeparture())
                .setParameter("arrival", to.getArrival())
                .uniqueResult().intValue();
    }

    @Override
    public ScheduledStop getStopById(final int id) {
        return sessionFactory.getCurrentSession().get(ScheduledStop.class, id);
    }

    @Override
    public Passenger getPassengerByInfo(final String firstName,
                                        final String lastName,
                                        final LocalDate dateOfBirth) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select p from Passenger p "
                + "where p.firstName = :fName and p.lastName = :lName "
                + "and p.dateOfBirth = :dob", Passenger.class)
                .setParameter("fName", firstName)
                .setParameter("lName", lastName)
                .setParameter("dob", dateOfBirth)
                .getSingleResult();
    }

    @Override
    public Passenger getUserPassenger(final User user) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Passenger p "
                + "where p.user = :user", Passenger.class)
                .setParameter("user", user).getSingleResult();
    }

    @Override
    public List<Ticket> getTickets(final Journey journey,
                                   final Passenger passenger) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Ticket t where t.journey = :journey "
                + "and t.passenger = :passenger", Ticket.class)
                .setParameter("journey", journey)
                .setParameter("passenger", passenger).getResultList();
    }

    @Override
    public List<Journey> getJourneys(final LocalDateTime start,
                                     final int page,
                                     final int searchStep) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select j from Journey j "
                + "join j.stops st where st.arrival is null "
                + "and st.departure >= :start "
                + "and st.departure < :end "
                + "order by st.departure asc", Journey.class)
                .setParameter("start", start)
                .setParameter("end", start.plusDays(1))
                .setFirstResult(searchStep * (page - 1))
                .setMaxResults(searchStep).getResultList();
    }

    @Override
    public List<Ticket> getTickets(final Journey journey, final int page,
                                   final int searchStep) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select t from Ticket t "
                + "join t.from st1 join t.to st2 "
                + "where t.journey = :journey "
                + "order by st1.departure asc, st2.arrival asc", Ticket.class)
                .setParameter("journey", journey)
                .setFirstResult(searchStep * (page - 1))
                .setMaxResults(searchStep).getResultList();
    }

    @Override
    public int journeysCount(final LocalDateTime start) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select count(j) from Journey j "
                + "join j.stops st where st.arrival is null "
                + "and st.departure >= :start "
                + "and st.departure < :end", Long.class)
                .setParameter("start", start)
                .setParameter("end", start.plusDays(1))
                .getSingleResult().intValue();
    }

    @Override
    public void update(final Passenger passenger) {
        sessionFactory.getCurrentSession().update(passenger);
    }

    @Override
    public void update(final User user) {
        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    public List<Ticket> getPassengerTickets(final Passenger passenger,
                                            final int page,
                                            final int searchStep) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select t from Ticket t "
                + "join t.from st "
                + "where t.passenger = :passenger "
                + "order by st.departure desc", Ticket.class)
                .setParameter("passenger", passenger)
                .setFirstResult(searchStep * (page - 1))
                .setMaxResults(searchStep).getResultList();
    }

    @Override
    public int ticketsCount(final Passenger passenger) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select count(t) from Ticket t "
                + "where t.passenger = :passenger", Long.class)
                .setParameter("passenger", passenger)
                .getSingleResult().intValue();
    }

    @Override
    public Ticket getTicketById(final int id) {
        return sessionFactory.getCurrentSession().get(Ticket.class, id);
    }

    @Override
    public void delete(final Ticket ticket) {
        sessionFactory.getCurrentSession().delete(ticket);
    }
}
