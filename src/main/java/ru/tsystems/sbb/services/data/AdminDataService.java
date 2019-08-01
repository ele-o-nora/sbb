package ru.tsystems.sbb.services.data;

import ru.tsystems.sbb.model.entities.Train;

import java.util.List;

public interface AdminDataService {
    void addNewStation(String stationName, int lineId, int zone, int order,
                       int distBefore, int distAfter);
    void addNewTrainModel(String model, int seats, int speed);
    void addNewRoute(String routeNumber, int lineId, String[] stations,
                     int[] waitTimes);
    void modifyRoute(int routeId, String[] stations,
                     int[] waitTimes);
    List<Train> getAllTrainModels();
}
