package ru.tsystems.sbb.services.view;

import java.util.Map;

public interface AdminViewService {
    Map<String, Object> prepAdminPanel();
    Map<String, Object> getCurrentLineStations(int lineId);
    Map<String, Object> addNewStation(String stationName, int lineId,
                                      int order, int distBefore,
                                      int distAfter);
    void addNewTrainModel(String model, int seats, int speed);
    Map<String, Object> modifyRouteStations(int lineId, int routeId);
    Map<String, Object> newRouteStopPattern(String routeNumber, int routeId,
                                            int lineId, String[] stations);
    void addNewRoute(String routeNumber, int lineId, String[] stations,
                     int[] waitTimes);
    void modifyRoute(int routeId, String[] stations, int[] waitTimes);
    void scheduleRoute(int routeId, String departureTime,
                       String dateFrom, String dateUntil,
                       int trainId, String direction);
}
