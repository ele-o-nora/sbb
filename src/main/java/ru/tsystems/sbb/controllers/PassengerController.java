package ru.tsystems.sbb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.sbb.model.dto.SignUpDto;
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
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setFirstName(firstName);
        signUpDto.setLastName(lastName);
        signUpDto.setDateOfBirth(dateOfBirth);
        signUpDto.setEmail(email);
        signUpDto.setPassword(password);
        return new ModelAndView("index", viewService.register(signUpDto));
    }
}
