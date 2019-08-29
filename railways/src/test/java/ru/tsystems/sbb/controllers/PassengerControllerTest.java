package ru.tsystems.sbb.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.times;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.sbb.controllers.view.PassengerController;
import ru.tsystems.sbb.model.dto.BuyerDetailsDto;
import ru.tsystems.sbb.model.dto.ChangeNameDto;
import ru.tsystems.sbb.model.dto.PassengerDetailsDto;
import ru.tsystems.sbb.model.dto.PasswordDto;
import ru.tsystems.sbb.model.dto.SignUpDto;
import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;
import ru.tsystems.sbb.services.view.PassengerViewService;

@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {

    @InjectMocks
    private PassengerController controller;

    @Mock
    private PassengerViewService mockViewService;

    private static final String EDIT_INFO = "editInfo";
    private static final String BUY_TICKETS = "buyTickets";

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
                .register(same(signUpDto.getPassengerDetails().getFirstName()),
                        same(signUpDto.getPassengerDetails().getLastName()),
                        same(signUpDto.getPassengerDetails().getDateOfBirth()),
                        same(signUpDto.getEmail()),
                        same(signUpDto.getPassword().getPassword()));
        verifyNoMoreInteractions(mockViewService);

        assertEquals("index", mav.getViewName());
    }

    @Test
    void changePasswordErrorTest(@Mock BindingResult bindingResult) {
        when(bindingResult.hasErrors()).thenReturn(true);
        PasswordDto passwordDto = new PasswordDto();

        ModelAndView mav = controller
                .changePassword(passwordDto, bindingResult);

        verifyZeroInteractions(mockViewService);

        assertEquals(EDIT_INFO, mav.getViewName());
        assertTrue(mav.getModel().containsKey("changeNameDto"));
        assertEquals(new ChangeNameDto(), mav.getModel().get("changeNameDto"));
    }

    @Test
    void changePasswordSuccessTest(@Mock BindingResult bindingResult) {
        PasswordDto passwordDto = new PasswordDto();

        ModelAndView mav = controller
                .changePassword(passwordDto, bindingResult);

        verify(mockViewService, times(1))
                .changePassword(same(passwordDto.getPassword()));
        verifyNoMoreInteractions(mockViewService);

        assertEquals(EDIT_INFO, mav.getViewName());
    }

    @Test
    void changeNameErrorTest(@Mock BindingResult bindingResult) {
        when(bindingResult.hasErrors()).thenReturn(true);
        ChangeNameDto changeNameDto = new ChangeNameDto();

        ModelAndView mav = controller.changeName(changeNameDto, bindingResult);

        verifyZeroInteractions(mockViewService);

        assertEquals(EDIT_INFO, mav.getViewName());
        assertTrue(mav.getModel().containsKey("passwordDto"));
        assertEquals(new PasswordDto(), mav.getModel().get("passwordDto"));
    }

    @Test
    void changeNameSuccessTest(@Mock BindingResult bindingResult) {
        ChangeNameDto changeNameDto = new ChangeNameDto();

        ModelAndView mav = controller.changeName(changeNameDto, bindingResult);

        verify(mockViewService, times(1))
                .changeName(same(changeNameDto.getFirstName()),
                        same(changeNameDto.getLastName()));
        verifyNoMoreInteractions(mockViewService);

        assertEquals(EDIT_INFO, mav.getViewName());
    }

    @Test
    void finalizeTicketSaleErrorTest(@Mock BindingResult bindingResult,
                                     @Mock SessionStatus status) {
        when(bindingResult.hasErrors()).thenReturn(true);
        TicketOrderDto order = new TicketOrderDto();
        BuyerDetailsDto buyerDetails = new BuyerDetailsDto();

        ModelAndView mav = controller.finalizeTicketSale(order, buyerDetails,
                bindingResult, status);

        verify(mockViewService, times(1)).prepBuyerInfo();
        verifyNoMoreInteractions(mockViewService);

        assertEquals(BUY_TICKETS, mav.getViewName());
    }

    @Test
    void finalizeTicketSaleSuccessTest(@Mock BindingResult bindingResult,
                                       @Mock SessionStatus status) {
        TicketOrderDto order = new TicketOrderDto();
        BuyerDetailsDto buyerDetails = new BuyerDetailsDto();
        buyerDetails.setPassenger(new PassengerDetailsDto());

        ModelAndView mav = controller.finalizeTicketSale(order, buyerDetails,
                bindingResult, status);

        verify(status, times(1)).setComplete();
        verifyNoMoreInteractions(status);

        verify(mockViewService, times(1)).finalizeTicketSale(same(order),
                same(buyerDetails.getPassenger().getFirstName()),
                same(buyerDetails.getPassenger().getLastName()),
                same(buyerDetails.getPassenger().getDateOfBirth()));
        verifyNoMoreInteractions(mockViewService);

        assertEquals(BUY_TICKETS, mav.getViewName());
    }

    @Test
    void finalizeTicketsSaleErrorTest(@Mock BindingResult bindingResult,
                                      @Mock SessionStatus status) {
        when(bindingResult.hasErrors()).thenReturn(true);
        TransferTicketOrderDto order = new TransferTicketOrderDto();
        BuyerDetailsDto buyerDetails = new BuyerDetailsDto();

        ModelAndView mav = controller.finalizeTicketsSale(order, buyerDetails,
                bindingResult, status);

        verify(mockViewService, times(1)).prepBuyerInfo();
        verifyNoMoreInteractions(mockViewService);

        assertEquals(BUY_TICKETS, mav.getViewName());

    }

    @Test
    void finalizeTicketsSaleSuccessTest(@Mock BindingResult bindingResult,
                                        @Mock SessionStatus status) {
        TransferTicketOrderDto order = new TransferTicketOrderDto();
        BuyerDetailsDto buyerDetails = new BuyerDetailsDto();
        buyerDetails.setPassenger(new PassengerDetailsDto());

        ModelAndView mav = controller.finalizeTicketsSale(order, buyerDetails,
                bindingResult, status);

        verify(status, times(1)).setComplete();
        verifyNoMoreInteractions(status);

        verify(mockViewService, times(1)).finalizeTicketsSale(same(order),
                same(buyerDetails.getPassenger().getFirstName()),
                same(buyerDetails.getPassenger().getLastName()),
                same(buyerDetails.getPassenger().getDateOfBirth()));
        verifyNoMoreInteractions(mockViewService);

        assertEquals(BUY_TICKETS, mav.getViewName());
    }

}
