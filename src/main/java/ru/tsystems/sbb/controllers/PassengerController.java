package ru.tsystems.sbb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.sbb.services.view.PassengerViewService;

@Controller
public class PassengerController {

    @Autowired
    private PassengerViewService viewService;

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
}
