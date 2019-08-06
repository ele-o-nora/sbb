package ru.tsystems.sbb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = {"", "/"})
    public ModelAndView adminPanel() {
        return new ModelAndView("admin", viewService.prepAdminPanel());
    }

    @GetMapping("/addStation/{id}")
    public ModelAndView addStation(@PathVariable final int id) {
        return new ModelAndView("addStation",
                viewService.getCurrentLineStations(id));
    }

    @PostMapping("/addNewStation")
    public ModelAndView addStation(@RequestParam(value = "name")
                                               final String newStation,
                                   @RequestParam(value = "line")
                                               final int lineId,
                                   @RequestParam(value = "order")
                                               final int order,
                                   @RequestParam(value = "distanceBefore",
                                   required = false, defaultValue = "0")
                                       final int before,
                                   @RequestParam(value = "distanceAfter",
                                   required = false, defaultValue = "0")
                                       final int after) {
        return new ModelAndView("addStation",
                viewService.addNewStation(newStation, lineId, order,
                        before, after));
    }

    @PostMapping("/addTrain")
    public ModelAndView addNewTrainModel(@RequestParam(value = "model")
                                             final String model,
                                         @RequestParam(value =  "seats")
                                             final int seats,
                                         @RequestParam(value = "speed")
                                            final int speed) {
        viewService.addNewTrainModel(model, seats, speed);
        return adminPanel();
    }

    @GetMapping("/addRoute/{lineId}")
    public ModelAndView addNewRoute(@PathVariable final int lineId) {
        return new ModelAndView("routeStations",
                viewService.getCurrentLineStations(lineId));
    }

    @GetMapping("/editRoute/{lineId}/{routeId}")
    public ModelAndView editRoute(@PathVariable final int routeId,
                                  @PathVariable final int lineId) {
        return new ModelAndView("routeStations",
                viewService.modifyRouteStations(lineId, routeId));
    }

    @PostMapping("/scheduleRoute")
    public ModelAndView scheduleRoute(@RequestParam(value = "routeNumber")
                                  final String routeNum,
                              @RequestParam(value = "routeId", required = false,
                              defaultValue = "0") final int routeId,
                              @RequestParam(value = "lineId") final int lineId,
                              @RequestParam(value = "stations")
                                  final String[] stations) {
        return new ModelAndView("scheduleRoute",
                viewService.newRouteStopPattern(routeNum, routeId,
                        lineId, stations));
    }

    @PostMapping("/addNewRoute")
    public ModelAndView addNewRouteFinal(@RequestParam(value = "routeNumber")
                                             final String routeNum,
                                         @RequestParam(value = "lineId")
                                             final int lineId,
                                         @RequestParam(value = "stations")
                                             final String[] stations,
                                         @RequestParam(value = "waitTimes")
                                             final int[] waitTimes) {
        viewService.addNewRoute(routeNum, lineId, stations, waitTimes);
        return adminPanel();
    }

    @PostMapping("/modifyRoute")
    public ModelAndView modifyRouteFinal(@RequestParam(value = "routeId")
                                             final int routeId,
                                         @RequestParam(value = "stations")
                                             final String[] stations,
                                         @RequestParam(value = "waitTimes")
                                             final int[] waitTimes) {
        viewService.modifyRoute(routeId, stations, waitTimes);
        return adminPanel();
    }

    @PostMapping("/scheduleRouteJourneys")
    public ModelAndView scheduleRouteJourneys(@RequestParam(value = "routeId")
                                              final int routeId,
                                              @RequestParam(value = "dateFrom")
                                              final String dateFrom,
                                              @RequestParam(value = "dateUntil")
                                              final String dateUntil,
                                              @RequestParam(value = "departure")
                                              final String departureTime,
                                              @RequestParam(value = "trainId")
                                              final int trainId,
                                              @RequestParam(value = "direction")
                                              final String direction) {
        viewService.scheduleRoute(routeId, departureTime, dateFrom, dateUntil,
                trainId, direction);
        return adminPanel();
    }

    @PostMapping("/updateTariff")
    public ModelAndView updateTariff(@RequestParam(value = "price")
                                     final float price) {
        viewService.updateTariff(price);
        return adminPanel();
    }
}
