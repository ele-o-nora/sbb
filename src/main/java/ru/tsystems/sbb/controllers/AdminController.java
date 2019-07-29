package ru.tsystems.sbb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.sbb.services.view.AdminViewService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminViewService viewService;

    @RequestMapping(value = {"", "/"})
    public ModelAndView adminPanel() {
        return new ModelAndView("admin", viewService.getLinesList());
    }

    @RequestMapping("/addStation/{id}")
    public ModelAndView addStation(@PathVariable final int id) {
        return new ModelAndView("addStation",
                viewService.getCurrentLineStations(id));
    }

    @PostMapping("/addNewStation")
    public ModelAndView addStation(@RequestParam(value = "name")
                                               final String newStation,
                                   @RequestParam(value = "line")
                                               final int lineId,
                                   @RequestParam(value = "zone")
                                               final int zone,
                                   @RequestParam(value = "order")
                                               final int order) {
        return new ModelAndView("addStation",
                viewService.addNewStation(newStation, lineId, zone, order));
    }
}
