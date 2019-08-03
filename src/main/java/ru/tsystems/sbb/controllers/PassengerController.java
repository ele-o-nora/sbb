package ru.tsystems.sbb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PassengerController {

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
        return null;
    }
}
