package ru.tsystems.sbb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.sbb.services.view.ScheduleViewService;

@Controller
public class MainController {

    @Autowired
    private ScheduleViewService viewService;

    @GetMapping("/")
    public ModelAndView homePage() {
        return new ModelAndView("index", viewService.getStationsList());
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
