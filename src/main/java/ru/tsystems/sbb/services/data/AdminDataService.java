package ru.tsystems.sbb.services.data;

public interface AdminDataService {
    void addNewStation(String stationName, int lineId, int zone, int order);
    void addNewTrainModel(String model, int seats);
    void addNewRoute(String routeNumber, int lineId, String[] stations,
                     int[] timesEnRoute, int[] waitTimes);
    void modifyRoute(int routeId, String[] stations,
                     int[] timesEnRoute, int[] waitTimes);
}
