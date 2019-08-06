package ru.tsystems.sbb.services.data;

import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.TicketDto;
import ru.tsystems.sbb.model.dto.TrainDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface AdminDataService {
    void addNewStation(String stationName, int lineId, int order,
                       int distBefore, int distAfter);
    void addNewTrainModel(String model, int seats, int speed);
    void addNewRoute(String routeNumber, int lineId, String[] stations,
                     int[] waitTimes);
    void modifyRoute(int routeId, String[] stations,
                     int[] waitTimes);
    List<TrainDto> getAllTrainModels();
    void scheduleJourneys(int routeId, LocalTime departure,
                          LocalDate dayFrom, LocalDate dayUntil,
                          int trainId, boolean outbound);
    void updateTariff(float price);
    float currentTariff();
    List<JourneyDto> getJourneys(LocalDateTime start, int page);
    List<TicketDto> getTickets(int journeyId, int page);
    int maxPages(LocalDateTime start);
}
