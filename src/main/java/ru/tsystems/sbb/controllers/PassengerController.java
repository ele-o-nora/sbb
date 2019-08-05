package ru.tsystems.sbb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.sbb.model.dto.TicketOrderDto;
import ru.tsystems.sbb.model.dto.TransferTicketOrderDto;
import ru.tsystems.sbb.services.view.PassengerViewService;

@Controller
public class PassengerController {

    @Autowired
    private PassengerViewService viewService;

    private static final String BUY_TICKETS = "buyTickets";

    @PostMapping("/register")
    public ModelAndView signUp(@RequestParam(name = "firstName")
                                    final String firstName,
                               @RequestParam(name = "lastName")
                                    final String lastName,
                               @RequestParam(name = "dateOfBirth")
                                    final String dateOfBirth,
                               @RequestParam(name = "email")
                                    final String email,
                               @RequestParam(name = "password")
                                    final String password) {
        return new ModelAndView("index", viewService
                .register(firstName, lastName, dateOfBirth, email, password));
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
        return new ModelAndView("index", viewService
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
        return new ModelAndView("index", viewService
                .finalizeTicketsSale(order, firstName, lastName, dateOfBirth));
    }
}
