package ru.tsystems.sbb.model.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.Station;

@Component
public class AdminDaoImpl implements AdminDao {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public void update(final LineStation lineStation) {
        sessionFactory.getCurrentSession().update(lineStation);
    }

    @Override
    public void add(final Station station) {
        sessionFactory.getCurrentSession().save(station);
    }

    @Override
    public void add(final LineStation lineStation) {
        sessionFactory.getCurrentSession().persist(lineStation);
    }
}
