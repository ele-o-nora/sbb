package ru.tsystems.sbb.model.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tsystems.sbb.model.entities.Passenger;
import ru.tsystems.sbb.model.entities.Role;
import ru.tsystems.sbb.model.entities.User;

@Component
public class PassengerDaoImpl implements PassengerDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User getUserByEmail(String email) {
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
    public Role getRoleByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Role r where r.name = :name",
                Role.class).setParameter("name", name).getSingleResult();
    }
}
