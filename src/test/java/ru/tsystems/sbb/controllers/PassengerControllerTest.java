package ru.tsystems.sbb.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.times;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.sbb.model.dto.PassengerDetailsDto;
import ru.tsystems.sbb.model.dto.PasswordDto;
import ru.tsystems.sbb.model.dto.SignUpDto;
import ru.tsystems.sbb.services.view.PassengerViewService;

@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {

    @InjectMocks
    private PassengerController controller;

    @Mock
    private PassengerViewService mockViewService;

    @BeforeEach
    void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void signUpErrorTest(@Mock BindingResult bindingResult) {
        when(bindingResult.hasErrors()).thenReturn(true);
        SignUpDto signUpDto = new SignUpDto(new PassengerDetailsDto(),
                new PasswordDto());

        ModelAndView mav = controller.signUp(signUpDto, bindingResult);

        verify(mockViewService, times(1)).failedSignUp();
        verifyNoMoreInteractions(mockViewService);

        assertEquals("index", mav.getViewName());
    }

    @Test
    void signUpSuccessTest(@Mock BindingResult bindingResult) {
        SignUpDto signUpDto = new SignUpDto(new PassengerDetailsDto(),
                new PasswordDto());

        ModelAndView mav = controller.signUp(signUpDto, bindingResult);

        verify(mockViewService, times(1))
                .register(signUpDto.getPassengerDetails().getFirstName(),
                        signUpDto.getPassengerDetails().getLastName(),
                        signUpDto.getPassengerDetails().getDateOfBirth(),
                        signUpDto.getEmail(),
                        signUpDto.getPassword().getPassword());
        verifyNoMoreInteractions(mockViewService);

        assertEquals("index", mav.getViewName());
    }

}
