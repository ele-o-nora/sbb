package ru.tsystems.sbb.controllers.view;

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
                                   @RequestParam(value = "x")
                                       final int x,
                                   @RequestParam(value = "y")
                                       final int y) {
        return new ModelAndView("addStation",
                viewService.addNewStation(newStation, lineId, order, x, y));
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
                              @RequestParam(value = "stations",
                                      required = false)
                                  final String[] stations) {
        if (stations != null && stations.length > 1) {
            return new ModelAndView("scheduleRoute",
                    viewService.newRouteStopPattern(routeNum, routeId,
                            lineId, stations));
        } else {
            return new ModelAndView("routeStations",
                    viewService.modifyRouteStations(lineId, routeId));
        }
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

    @GetMapping("/journeys/{today}")
    public ModelAndView lookUpJourneys(@PathVariable final String today,
                                       @RequestParam(defaultValue = "1")
                                       final int page) {
        return new ModelAndView("journeys", viewService
                .lookUpJourneys(today, page));
    }

    @GetMapping("/journey/{id}/passengers")
    public ModelAndView listPassengers(@PathVariable final int id,
                                       @RequestParam(defaultValue = "1")
                                       final int page) {
        return new ModelAndView("passengers", viewService
                .listPassengers(id, page));
    }

    @GetMapping("/journey/{id}")
    public ModelAndView journeyInfo(@PathVariable final int id) {
        return new ModelAndView("journeyInfo", viewService.journeyInfo(id));
    }

    @PostMapping("/cancel")
    public String cancelJourney(@RequestParam final int journeyId,
                                @RequestParam final String date) {
        viewService.cancelJourney(journeyId);
        return "redirect:/admin/journeys/" + date;
    }

    @PostMapping("/delay")
    public String delayJourney(@RequestParam final int journeyId,
                               @RequestParam final int delay,
                               @RequestParam final String date) {
        viewService.delayJourney(journeyId, delay);
        return "redirect:/admin/journeys/" + date;
    }

    @PostMapping("/renameStation")
    public String renameStation(@RequestParam final int id,
                                @RequestParam final String name) {
        viewService.renameStation(id, name);
        return "redirect:/map";
    }
}
