package ru.tsystems.sbb.controllers.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.sbb.model.dto.SignUpDto;
import ru.tsystems.sbb.services.view.ScheduleViewService;

@Controller
@SessionAttributes({"ticketOrder", "transferTickets"})
public class MainController {

    @Autowired
    private ScheduleViewService viewService;

    @GetMapping("/")
    public ModelAndView homePage(SessionStatus status) {
        status.setComplete();
        return new ModelAndView("index", viewService.getStationsList());
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login", "signUpDto", new SignUpDto());
    }

    @GetMapping("/accessDenied")
    public ModelAndView accessDenied() {
        return new ModelAndView("accessDenied");
    }

    @GetMapping("/map")
    public ModelAndView map() {
        return new ModelAndView("map", viewService.prepareRailwayMap());
    }
}
