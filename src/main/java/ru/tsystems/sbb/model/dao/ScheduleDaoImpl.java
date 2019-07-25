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

    @Override
    public List<ScheduledStop> stationSchedule(Station station,
                                               LocalDateTime from) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select distinct s from ScheduledStop s " +
                "where s.station = :station " +
                "and ((s.arrival >= :from and s.arrival < :to) " +
                "or (s.departure >= :from and s.departure < :to)) " +
                "order by s.arrival, s.departure asc", ScheduledStop.class)
                .setParameter("station", station)
                .setParameter("from", from)
                .setParameter("to", from.plusHours(6))
                .getResultList();
    }

    @Override
    public List<Journey> trainsFromTo(Station origin, Station destination,
                                      LocalDateTime from) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select j from Journey j " +
                "join j.stops st1, j.stops st2 " +
                "where st1.station = :origin and st2.station = :dest " +
                "and st1.departure >= :from and st1.departure < :to " +
                "order by st1.departure asc", Journey.class)
                .setParameter("origin", origin)
                .setParameter("dest", destination)
                .setParameter("from", from)
                .setParameter("to", from.plusHours(6)).getResultList();
    }

    @Override
    public Station getStationByName(String stationName) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Station s " +
                "where s.name = :name", Station.class)
                .setParameter("name", stationName).getSingleResult();
    }
}
