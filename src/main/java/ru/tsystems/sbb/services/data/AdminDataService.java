package ru.tsystems.sbb.services.data;

public interface AdminDataService {
    void addNewStation(String stationName, int lineId, int zone, int order);
}
