package ru.tsystems.sbb.model.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduleDaoImpl implements ScheduleDao {

    @Autowired
    private SessionFactory sessionFactory;
    private static final String ORIGIN = "origin";

    @Override
    public List<ScheduledStop> stationSchedule(final Station station,
                                               final LocalDateTime from,
                                               final int searchPeriod) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select distinct s from ScheduledStop s "
                + "where s.station = :station "
                + "and ((s.arrival >= :from and s.arrival < :to) "
                + "or (s.departure >= :from and s.departure < :to))",
                ScheduledStop.class)
                .setParameter("station", station)
                .setParameter("from", from)
                .setParameter("to", from.plusHours(searchPeriod))
                .getResultList();
    }

    @Override
    public List<Journey> trainsFromToByDeparture(final Station origin,
                                                 final Station destination,
                                                 final LocalDateTime from,
                                                 final int searchPeriod) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select j from Journey j "
                + "join j.stops st1 join j.stops st2 "
                + "where st1.station = :origin and st2.station = :dest"
                + " and st1.departure >= :from and st1.departure < :to "
                + "and st1.departure < st2.arrival "
                + "and j.cancelled = false "
                + "order by st1.departure asc", Journey.class)
                .setParameter(ORIGIN, origin)
                .setParameter("dest", destination)
                .setParameter("from", from)
                .setParameter("to", from.plusHours(searchPeriod))
                .getResultList();
    }

    @Override
    public List<Journey> trainsFromToByArrival(final Station origin,
                                               final Station destination,
                                               final LocalDateTime by,
                                               final int searchPeriod) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select j from Journey j "
                + "join j.stops st1 join j.stops st2 "
                + "where st1.station = :origin and st2.station = :dest"
                + " and st2.arrival <= :by and st2.arrival > :from "
                + "and st1.departure < st2.arrival"
                + " and j.cancelled = false"
                + " order by st1.departure asc", Journey.class)
                .setParameter(ORIGIN, origin)
                .setParameter("dest", destination)
                .setParameter("by", by)
                .setParameter("from", by.minusHours(searchPeriod))
                .getResultList();
    }

    @Override
    public Station getStationByName(final String stationName) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Station s "
                + "where s.name = :name", Station.class)
                .setParameter("name", stationName).getSingleResult();
    }

    @Override
    public List<Station> getTransferStations(final Station origin,
                                             final Station destination) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select distinct s from Station s "
                + "join s.routes rs1 join s.routes rs2 "
                + "join rs1.route r1 join rs2.route r2 "
                + "join r1.stations rs3 join r2.stations rs4 "
                + "where rs3.station = :origin "
                + "and rs4.station = :dest", Station.class)
                .setParameter(ORIGIN, origin)
                .setParameter("dest", destination).getResultList();
    }

    @Override
    public Journey lastTrainBefore(final Station origin,
                                   final Station destination,
                                   final LocalDateTime by) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select j from Journey j"
                + " join j.stops st1 join j.stops st2 "
                + "where st1.station = :origin and st2.station = :dest "
                + "and st2.arrival <= :by "
                + "and st1.departure < st2.arrival"
                + " and j.cancelled = false"
                + " order by st2.arrival desc", Journey.class)
                .setMaxResults(1)
                .setParameter(ORIGIN, origin)
                .setParameter("dest", destination)
                .setParameter("by", by)
                .uniqueResult();
    }

    @Override
    public Journey firstTrainAfter(final Station origin,
                                   final Station destination,
                                   final LocalDateTime from) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select j from Journey j"
                + " join j.stops st1 join j.stops st2 "
                + "where st1.station = :origin and st2.station = :dest "
                + "and st1.departure >= :from "
                + "and st1.departure < st2.arrival "
                + "and j.cancelled = false "
                + "order by st1.departure asc", Journey.class)
                .setMaxResults(1)
                .setParameter(ORIGIN, origin)
                .setParameter("dest", destination)
                .setParameter("from", from)
                .uniqueResult();
    }
}
