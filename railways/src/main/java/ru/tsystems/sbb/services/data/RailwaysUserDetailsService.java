package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.model.dao.PassengerDao;
import ru.tsystems.sbb.model.entities.Role;
import ru.tsystems.sbb.model.entities.User;

import java.util.stream.Collectors;

@Service
@Transactional
public class RailwaysUserDetailsService implements UserDetailsService {

    @Autowired
    private PassengerDao passengerDao;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user =  passengerDao.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        UserBuilder builder = org.springframework.security.core
                .userdetails.User.withUsername(email);
        builder.password(user.getPassword());
        String[] roles = new String[]{};
        builder.roles(user.getRoles().stream()
                .map(Role::getName).collect(Collectors.toList())
                .toArray(roles));
        return builder.build();
    }

}
