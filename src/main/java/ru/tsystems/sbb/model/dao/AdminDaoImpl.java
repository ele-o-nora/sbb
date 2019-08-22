package ru.tsystems.sbb.model.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.RouteStation;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.entities.StationsDistance;
import ru.tsystems.sbb.model.entities.Tariff;
import ru.tsystems.sbb.model.entities.Train;

import java.util.List;

@Component
public class AdminDaoImpl implements AdminDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void update(final LineStation lineStation) {
        sessionFactory.getCurrentSession().update(lineStation);
    }

    @Override
    public void add(Train train) {
        sessionFactory.getCurrentSession().persist(train);
    }

    @Override
    public void add(Route route) {
        sessionFactory.getCurrentSession().persist(route);
    }

    @Override
    public void add(RouteStation routeStation) {
        sessionFactory.getCurrentSession().persist(routeStation);
    }

    @Override
    public void add(Journey journey) {
        sessionFactory.getCurrentSession().persist(journey);
    }

    @Override
    public void add(ScheduledStop scheduledStop) {
        sessionFactory.getCurrentSession().persist(scheduledStop);
    }

    @Override
    public void add(final Station station) {
        sessionFactory.getCurrentSession().persist(station);
    }

    @Override
    public void add(final LineStation lineStation) {
        sessionFactory.getCurrentSession().persist(lineStation);
    }

    @Override
    public void add(Tariff tariff) {
        sessionFactory.getCurrentSession().persist(tariff);
    }

    @Override
    public void add(final StationsDistance stationsDistance) {
        sessionFactory.getCurrentSession().persist(stationsDistance);
    }

    @Override
    public void cleanRouteStopPattern(final Route route) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("delete from RouteStation rs "
                + "where rs.route = :route")
                .setParameter("route", route).executeUpdate();
    }

    @Override
    public List<Train> getAllTrainModels() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Train t order by t.model", Train.class)
                .getResultList();
    }

    @Override
    public void deleteDistance(Station s1, Station s2) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("delete from StationsDistance sd "
                + "where (sd.firstStation = :s1 and sd.secondStation = :s2) "
                + "or (sd.firstStation = :s2 and sd.secondStation = :s1)")
                .setParameter("s1", s1)
                .setParameter("s2", s2)
                .executeUpdate();
    }

    @Override
    public Train getTrainById(int trainId) {
        return sessionFactory.getCurrentSession().get(Train.class, trainId);
    }

    @Override
    public void update(Journey journey) {
        sessionFactory.getCurrentSession().update(journey);
    }
}
