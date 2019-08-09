package ru.tsystems.sbb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.sbb.model.dto.ChangeNameDto;
import ru.tsystems.sbb.model.dto.PasswordDto;
import ru.tsystems.sbb.model.dto.SignUpDto;
import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;
import ru.tsystems.sbb.services.view.PassengerViewService;

import javax.validation.Valid;

@Controller
public class PassengerController {

    @Autowired
    private PassengerViewService viewService;

    private static final String BUY_TICKETS = "buyTickets";

    @PostMapping("/register")
    public ModelAndView signUp(@ModelAttribute("signUpDto") @Valid
                               final SignUpDto signUpDto,
                               final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("index", viewService.failedSignUp());
        }
        return new ModelAndView("index", viewService
                .register(signUpDto.getFirstName(), signUpDto.getLastName(),
                        signUpDto.getDateOfBirth(), signUpDto.getEmail(),
                        signUpDto.getPassword().getPassword()));
    }

    @PostMapping("/buyTickets")
    public ModelAndView prepBuyTickets(@RequestParam(value = "firstJourneyId")
                                       final int firstJourneyId,
                                       @RequestParam(value = "secondJourneyId")
                                       final int secondJourneyId,
                                       @RequestParam(value = "from")
                                       final String from,
                                       @RequestParam(value = "to")
                                       final String to,
                                       @RequestParam(value = "transfer")
                                       final String transfer) {
        return new ModelAndView(BUY_TICKETS, viewService
                .prepTicketsSale(firstJourneyId, secondJourneyId,
                        from, to, transfer));
    }

    @PostMapping("/buyTicket")
    public ModelAndView prepBuyTicket(@RequestParam(value = "journeyId")
                                          final int journeyId,
                                      @RequestParam(value = "from")
                                          final String from,
                                      @RequestParam(value = "to")
                                          final String to) {
        return new ModelAndView(BUY_TICKETS, viewService
                .prepTicketSale(journeyId, from, to));
    }

    @PostMapping("/finalizeTicketSale")
    public ModelAndView finalizeTicketSale(@RequestParam(value = "firstName")
                                               final String firstName,
                                           @RequestParam(value = "lastName")
                                                final String lastName,
                                           @RequestParam(value = "dateOfBirth")
                                                final String dateOfBirth,
                                           @ModelAttribute(name = "ticketOrder")
                                           final TicketOrderDto order) {
        return new ModelAndView(BUY_TICKETS, viewService
                .finalizeTicketSale(order, firstName, lastName, dateOfBirth));
    }

    @PostMapping("/finalizeTicketsSale")
    public ModelAndView finalizeTicketsSale(@RequestParam(value = "firstName")
                                                final String firstName,
                                            @RequestParam(value = "lastName")
                                                final String lastName,
                                            @RequestParam(value = "dateOfBirth")
                                                final String dateOfBirth,
                                            @ModelAttribute("transferTickets")
                                       final TransferTicketOrderDto order) {
        return new ModelAndView(BUY_TICKETS, viewService
                .finalizeTicketsSale(order, firstName, lastName, dateOfBirth));
    }

    @GetMapping("/editInfo")
    public ModelAndView editUserInfo() {
        return new ModelAndView("editInfo", viewService.editUserInfo());
    }

    @PostMapping("/changeName")
    public ModelAndView changeName(@ModelAttribute("changeNameDto") @Valid
                                   final ChangeNameDto changeNameDto,
                                   final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("editInfo",
                    "passwordDto", new PasswordDto());
        }
        return new ModelAndView("editInfo",
                viewService.changeName(changeNameDto.getFirstName(),
                        changeNameDto.getLastName()));
    }

    @PostMapping("/changePassword")
    public ModelAndView changePassword(@ModelAttribute("passwordDto") @Valid
                                       final PasswordDto passwordDto,
                                       final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("editInfo",
                    "changeNameDto", new ChangeNameDto());
        }
        return new ModelAndView("editInfo",
                viewService.changePassword(passwordDto.getPassword()));
    }

    @GetMapping("/myTickets")
    public ModelAndView myTickets(@RequestParam(defaultValue = "1")
                                  final int page) {
        return new ModelAndView("tickets", viewService.getUserTickets(page));
    }

    @PostMapping("/returnTicket")
    public ModelAndView returnTicket(@RequestParam(value = "ticketId")
                                     final int ticketId) {
        return new ModelAndView("tickets", viewService.returnTicket(ticketId));
    }
}
