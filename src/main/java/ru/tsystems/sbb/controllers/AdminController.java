package ru.tsystems.sbb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.sbb.services.view.AdminViewService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminViewService viewService;

    @RequestMapping("")
    public String adminPanel() {
        return "admin";
    }

    @RequestMapping("/addStation")
    public ModelAndView chooseLineForNewStation() {
        return new ModelAndView("addStation", viewService.getLinesList());
    }

    @RequestMapping("/addStation/{id}")
    public ModelAndView addStation(@PathVariable int id) {
        return new ModelAndView("addStation",
                viewService.getCurrentLineStations(id));
    }
}
