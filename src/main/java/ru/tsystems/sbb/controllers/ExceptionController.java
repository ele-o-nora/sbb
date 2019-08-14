package ru.tsystems.sbb.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.tsystems.sbb.model.dto.PassengerDetailsDto;
import ru.tsystems.sbb.model.dto.PasswordDto;
import ru.tsystems.sbb.model.dto.SignUpDto;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ExceptionController.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNotFound(HttpServletRequest req, Exception ex) {
        LOGGER.error("Request {} raised {}", req.getRequestURI(), ex);
        return new ModelAndView("notFound", "signUpDto",
                new SignUpDto(new PassengerDetailsDto(), new PasswordDto()));
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
        LOGGER.error("Request {} raised {}", req.getRequestURI(), ex);
        return new ModelAndView("error", "signUpDto",
                new SignUpDto(new PassengerDetailsDto(), new PasswordDto()));
    }

}
