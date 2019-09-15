package ru.tsystems.sbb.model.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Route;
import ru.tsystems.sbb.model.entities.Station;

import java.util.List;

@Component
public class RouteDaoImpl implements RouteDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Line> allLines() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Line", Line.class)
                .getResultList();
    }

    @Override
    public List<Station> allLineStations(final Line line) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select s "
                + "from Station s join s.lines ls "
                + "where ls.line = :line "
                + "order by ls.order asc", Station.class)
                .setParameter("line", line)
                .getResultList();
    }

    @Override
    public Line getLineById(final int lineId) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Line.class, lineId);
    }

    @Override
    public List<Route> allLineRoutes(final Line line) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Route r "
                + "where r.line = :line", Route.class)
                .setParameter("line", line)
                .getResultList();
    }

    @Override
    public Route getRouteById(final int routeId) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Route.class, routeId);
    }

    @Override
    public List<Station> getAllStations() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Station s order by s.name",
                Station.class).getResultList();
    }

    @Override
    public List<LineStation> getLineStations(final Line line, final int from) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from LineStation ls "
                + "where ls.line = :line "
                + "and ls.order >= :order", LineStation.class)
                .setParameter("line", line)
                .setParameter("order", from)
                .getResultList();
    }

    @Override
    public int getStationOrder(Line line, Station station) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select ls.order from LineStation ls "
                + "where ls.line = :line and ls.station = :station", Integer.class)
                .setParameter("line", line)
                .setParameter("station", station).getSingleResult();
    }

    @Override
    public List<Station> getStations(int first, int last, Line line) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select s from Station s join s.lines ls "
                + "where ls.line = :line and ls.order >= :first "
                + "and ls.order <= :last order by ls.order", Station.class)
                .setParameter("line", line)
                .setParameter("first", first)
                .setParameter("last", last).getResultList();
    }
}
