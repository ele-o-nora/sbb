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
import ru.tsystems.sbb.model.entities.Train;

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
    public void cleanRouteStopPattern(Route route) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("delete from RouteStation rs "
                + "where rs.route = :route").executeUpdate();
    }
}
