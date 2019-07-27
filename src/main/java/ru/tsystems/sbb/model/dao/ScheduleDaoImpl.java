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
    private static final int SEARCH_PERIOD = 12;

    @Override
    public List<ScheduledStop> stationSchedule(final Station station,
                                               final LocalDateTime from) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select distinct s from ScheduledStop s "
                + "where s.station = :station "
                + "and ((s.arrival >= :from and s.arrival < :to) "
                + "or (s.departure >= :from and s.departure < :to)) "
                + "order by s.arrival, s.departure asc", ScheduledStop.class)
                .setParameter("station", station)
                .setParameter("from", from)
                .setParameter("to", from.plusHours(SEARCH_PERIOD))
                .getResultList();
    }

    @Override
    public List<Journey> trainsFromToByDeparture(final Station origin,
                                      final Station destination,
                                      final LocalDateTime from) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select j from Journey j "
                + "join j.stops st1, j.stops st2 "
                + "where st1.station = :origin and st2.station = :dest "
                + "and st1.departure >= :from and st1.departure < :to "
                + "and st1.departure < st2.arrival "
                + "order by st1.departure asc", Journey.class)
                .setParameter("origin", origin)
                .setParameter("dest", destination)
                .setParameter("from", from)
                .setParameter("to", from.plusHours(SEARCH_PERIOD))
                .getResultList();
    }

    @Override
    public List<Journey> trainsFromToByArrival(Station origin, Station destination, LocalDateTime by) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select j from Journey j "
                + "join j.stops st1, j.stops st2 "
                + "where st1.station = :origin and st2.station = :dest "
                + "and st2.arrival <= :by and st2.arrival > :from "
                + "and st1.departure < st2.arrival "
                + "order by st1.departure", Journey.class)
                .setParameter("origin", origin)
                .setParameter("dest", destination)
                .setParameter("by", by)
                .setParameter("from", by.minusHours(SEARCH_PERIOD))
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
    public List<Station> getAllStations() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Station s order by s.name",
                Station.class).getResultList();
    }
}
