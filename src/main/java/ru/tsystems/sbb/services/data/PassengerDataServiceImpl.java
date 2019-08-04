package ru.tsystems.sbb.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.sbb.model.dao.PassengerDao;
import ru.tsystems.sbb.model.entities.Passenger;
import ru.tsystems.sbb.model.entities.Role;
import ru.tsystems.sbb.model.entities.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class PassengerDataServiceImpl implements PassengerDataService {

    @Autowired
    private PassengerDao passengerDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(final String firstName, final String lastName,
                         final LocalDate dateOfBirth, final String email,
                         final String password) {
        Passenger passenger = new Passenger();
        passenger.setFirstName(firstName);
        passenger.setLastName(lastName);
        passenger.setDateOfBirth(dateOfBirth);
        passengerDao.add(passenger);
        User user = new User();
        user.setEmail(email);
        user.setPassenger(passenger);
        user.setPassword(passwordEncoder.encode(password));
        Set<Role> roles = new HashSet<>();
        Role userRole = passengerDao.getRoleByName("USER");
        roles.add(userRole);
        user.setRoles(roles);
        passengerDao.add(user);
    }

}
