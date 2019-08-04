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

@Component
public class PassengerDaoImpl implements PassengerDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User getUserByEmail(final String email) {
        Session session = sessionFactory.openSession();
        User user = session.createQuery("from User u where u.email = :email",
                User.class).setParameter("email", email).getSingleResult();
        session.close();
        return user;
    }

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().persist(user);
    }

    @Override
    public void add(Passenger passenger) {
        sessionFactory.getCurrentSession().persist(passenger);
    }

    @Override
    public Role getRoleByName(final String name) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Role r where r.name = :name",
                Role.class).setParameter("name", name).getSingleResult();
    }

    @Override
    public void add(Ticket ticket) {
        sessionFactory.getCurrentSession().persist(ticket);
    }

    @Override
    public float getCurrentTariff() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select t.pricePerTenLeagues "
                        + "from Tariff t order by t.dateFrom desc",
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
    public Passenger getUserPassenger(User user) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Passenger p "
                + "where p.user = :user", Passenger.class)
                .setParameter("user", user).getSingleResult();
    }

    @Override
    public Ticket getTicket(Journey journey, Passenger passenger) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Ticket t where t.journey = :journey "
                + "and t.passenger = :passenger", Ticket.class)
                .setParameter("journey", journey)
                .setParameter("passenger", passenger).getSingleResult();
    }
}
