package ru.tsystems.sbb.services.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.tsystems.sbb.model.dao.PassengerDao;
import ru.tsystems.sbb.model.entities.Role;
import ru.tsystems.sbb.model.entities.User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RailwaysUserDetailsServiceTest {

    @InjectMocks
    private RailwaysUserDetailsService railwaysUserDetailsService;

    @Mock
    private PassengerDao mockPassengerDao;

    @BeforeEach
    void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void loadUserByUsernameNullTest() {
        String email = "email";
        assertThrows(UsernameNotFoundException.class,
                () -> railwaysUserDetailsService.loadUserByUsername(email));
    }

    @Test
    void loadUserByUsernameSuccessTest() {
        String email = "email";
        User user = new User();
        Role role = new Role();
        role.setName("USER");
        user.setPassword("password");
        user.setRoles(Collections.singleton(role));
        when(mockPassengerDao.getUserByEmail(anyString())).thenReturn(user);

        UserDetails userDetails = railwaysUserDetailsService
                .loadUserByUsername(email);

        assertEquals(email, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
