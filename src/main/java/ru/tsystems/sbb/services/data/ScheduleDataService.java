package ru.tsystems.sbb.services.data;

import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.ScheduledStopDto;
import ru.tsystems.sbb.model.dto.TransferTrainsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleDataService {
    List<ScheduledStopDto> stationSchedule(String stationName,
                                           LocalDateTime from);
    List<JourneyDto> directTrainsFromTo(String stationFrom, String stationTo,
                                  LocalDateTime from, String searchType);
    List<TransferTrainsDto> trainsFromToWithTransfer(String stationFrom,
                                                     String stationTo,
                                                     LocalDateTime from,
                                                     String searchType);
}
