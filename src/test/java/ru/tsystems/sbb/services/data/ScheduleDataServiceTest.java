package ru.tsystems.sbb.services.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tsystems.sbb.model.dao.ScheduleDao;
import ru.tsystems.sbb.model.dto.JourneyDto;
import ru.tsystems.sbb.model.dto.ScheduledStopDto;
import ru.tsystems.sbb.model.dto.TransferTrainsDto;
import ru.tsystems.sbb.model.entities.Journey;
import ru.tsystems.sbb.model.entities.Line;
import ru.tsystems.sbb.model.entities.LineStation;
import ru.tsystems.sbb.model.entities.ScheduledStop;
import ru.tsystems.sbb.model.entities.Station;
import ru.tsystems.sbb.model.mappers.EntityToDtoMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ScheduleDataServiceTest {

    @InjectMocks
    private ScheduleDataServiceImpl scheduleDataService;

    @Mock
    private EntityToDtoMapper mockMapper;

    @Mock
    private ScheduleDao mockScheduleDao;

    private static final int TRANSFER_TIME = 15;

    @BeforeEach
    void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void directTrainsFromToDifferentLinesTest() {
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        LocalDateTime dateTime = LocalDateTime.of(2020, 2, 2, 20, 2);
        String searchType = "departure";
        Station firstStation = new Station();
        LineStation first = new LineStation();
        first.setLine(new Line());
        firstStation.setLines(Collections.singletonList(first));
        Station secondStation = new Station();
        LineStation second = new LineStation();
        second.setLine(new Line());
        secondStation.setLines(Collections.singletonList(second));
        when(mockScheduleDao.getStationByName(eq(stationFrom)))
                .thenReturn(firstStation);
        when(mockScheduleDao.getStationByName(eq(stationTo)))
                .thenReturn(secondStation);

        List<JourneyDto> result = scheduleDataService
                .directTrainsFromTo(stationFrom, stationTo,
                        dateTime, searchType);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationFrom));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationTo));
        verifyNoMoreInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertTrue(result.isEmpty());
    }

    @Test
    void directTrainsFromToByDepartureTest() {
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        LocalDateTime dateTime = LocalDateTime.of(2020, 2, 2, 20, 2);
        String searchType = "departure";
        Station first = new Station();
        Station second = new Station();
        LineStation lineStation = new LineStation();
        lineStation.setLine(new Line());
        first.setLines(Collections.singletonList(lineStation));
        second.setLines(Collections.singletonList(lineStation));
        List<Journey> journeys = new ArrayList<>();
        Journey journey = new Journey();
        journeys.add(journey);
        JourneyDto journeyDto = new JourneyDto();
        when(mockScheduleDao.getStationByName(eq(stationFrom)))
                .thenReturn(first);
        when(mockScheduleDao.getStationByName(eq(stationTo)))
                .thenReturn(second);
        when(mockScheduleDao.trainsFromToByDeparture(any(Station.class),
                any(Station.class), any(LocalDateTime.class)))
                .thenReturn(journeys);
        when(mockMapper.convert(any(Journey.class)))
                .thenReturn(journeyDto);

        List<JourneyDto> result = scheduleDataService
                .directTrainsFromTo(stationFrom, stationTo,
                        dateTime, searchType);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationFrom));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationTo));
        verify(mockScheduleDao, times(1))
                .trainsFromToByDeparture(same(first), same(second),
                        same(dateTime));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper).convert(same(journey));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(1, result.size());
        assertEquals(journeyDto, result.get(0));
    }

    @Test
    void directTrainsFromToByArrivalTest() {
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        LocalDateTime dateTime = LocalDateTime.of(2020, 2, 2, 20, 2);
        String searchType = "arrival";
        Station first = new Station();
        Station second = new Station();
        LineStation lineStation = new LineStation();
        lineStation.setLine(new Line());
        first.setLines(Collections.singletonList(lineStation));
        second.setLines(Collections.singletonList(lineStation));
        List<Journey> journeys = new ArrayList<>();
        Journey journey = new Journey();
        journeys.add(journey);
        JourneyDto journeyDto = new JourneyDto();
        when(mockScheduleDao.getStationByName(eq(stationFrom)))
                .thenReturn(first);
        when(mockScheduleDao.getStationByName(eq(stationTo)))
                .thenReturn(second);
        when(mockScheduleDao.trainsFromToByArrival(any(Station.class),
                any(Station.class), any(LocalDateTime.class)))
                .thenReturn(journeys);
        when(mockMapper.convert(any(Journey.class)))
                .thenReturn(journeyDto);

        List<JourneyDto> result = scheduleDataService
                .directTrainsFromTo(stationFrom, stationTo,
                        dateTime, searchType);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationFrom));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationTo));
        verify(mockScheduleDao, times(1))
                .trainsFromToByArrival(same(first), same(second),
                        same(dateTime));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper).convert(same(journey));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(1, result.size());
        assertEquals(journeyDto, result.get(0));
    }

    @Test
    void transferTrainsByDepartureEmptyFirstTrainTest() {
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        LocalDateTime dateTime = LocalDateTime.of(2020, 2, 2, 20, 2);
        String searchType = "departure";
        Station first = new Station();
        Station second = new Station();
        Station transfer = new Station();
        List<Station> transferStations = Collections
                .singletonList(transfer);
        when(mockScheduleDao.getStationByName(eq(stationFrom)))
                .thenReturn(first);
        when(mockScheduleDao.getStationByName(eq(stationTo)))
                .thenReturn(second);
        when(mockScheduleDao.getTransferStations(any(Station.class),
                any(Station.class))).thenReturn(transferStations);
        when(mockScheduleDao.trainsFromToByDeparture(same(first),
                any(Station.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        List<TransferTrainsDto> result = scheduleDataService
                .trainsWithTransfer(stationFrom, stationTo,
                        dateTime, searchType);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationFrom));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationTo));
        verify(mockScheduleDao, times(1))
                .getTransferStations(same(first), same(second));
        verify(mockScheduleDao, times(1))
                .trainsFromToByDeparture(same(first), same(transfer),
                        same(dateTime));
        verifyNoMoreInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertTrue(result.isEmpty());
    }

    @Test
    void transferTrainsByDepartureEmptySecondTrainTest() {
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        LocalDateTime dateTime = LocalDateTime.of(2020, 2, 2, 20, 2);
        String searchType = "departure";
        Station first = new Station();
        Station second = new Station();
        Station transfer = new Station();
        List<Station> transferStations = Collections
                .singletonList(transfer);
        ScheduledStop transferStop = new ScheduledStop();
        transferStop.setStation(transfer);
        transferStop.setArrival(dateTime.plusHours(2));
        Journey firstJourney = new Journey();
        firstJourney.setStops(Collections.singletonList(transferStop));
        List<Journey> trainsToTransfer = Collections
                .singletonList(firstJourney);
        when(mockScheduleDao.getStationByName(eq(stationFrom)))
                .thenReturn(first);
        when(mockScheduleDao.getStationByName(eq(stationTo)))
                .thenReturn(second);
        when(mockScheduleDao.getTransferStations(any(Station.class),
                any(Station.class))).thenReturn(transferStations);
        when(mockScheduleDao.trainsFromToByDeparture(same(first),
                any(Station.class), any(LocalDateTime.class)))
                .thenReturn(trainsToTransfer);

        List<TransferTrainsDto> result = scheduleDataService
                .trainsWithTransfer(stationFrom, stationTo,
                        dateTime, searchType);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationFrom));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationTo));
        verify(mockScheduleDao, times(1))
                .getTransferStations(same(first), same(second));
        verify(mockScheduleDao, times(1))
                .trainsFromToByDeparture(same(first), same(transfer),
                        same(dateTime));
        verify(mockScheduleDao, times(1))
                .firstTrainAfter(same(transfer), same(second),
                        eq(transferStop.getArrival()
                                .plusMinutes(TRANSFER_TIME)));
        verifyNoMoreInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertTrue(result.isEmpty());
    }

    @Test
    void transferTrainsByDepartureSuccessTest() {
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        LocalDateTime dateTime = LocalDateTime.of(2020, 2, 2, 20, 2);
        String searchType = "departure";
        Station first = new Station();
        Station second = new Station();
        Station transfer = new Station();
        List<Station> transferStations = Collections
                .singletonList(transfer);
        ScheduledStop transferStop = new ScheduledStop();
        transferStop.setStation(transfer);
        transferStop.setArrival(dateTime.plusHours(2));
        Journey firstJourney = new Journey();
        firstJourney.setStops(Collections.singletonList(transferStop));
        List<Journey> trainsToTransfer = Collections
                .singletonList(firstJourney);
        Journey secondJourney = new Journey();
        JourneyDto firstJourneyDto = new JourneyDto();
        JourneyDto secondJourneyDto = new JourneyDto();
        when(mockScheduleDao.getStationByName(eq(stationFrom)))
                .thenReturn(first);
        when(mockScheduleDao.getStationByName(eq(stationTo)))
                .thenReturn(second);
        when(mockScheduleDao.getTransferStations(any(Station.class),
                any(Station.class))).thenReturn(transferStations);
        when(mockScheduleDao.trainsFromToByDeparture(same(first),
                any(Station.class), any(LocalDateTime.class)))
                .thenReturn(trainsToTransfer);
        when(mockScheduleDao.firstTrainAfter(any(Station.class),
                any(Station.class), any(LocalDateTime.class)))
                .thenReturn(secondJourney);
        when(mockMapper.convert(same(firstJourney)))
                .thenReturn(firstJourneyDto);
        when(mockMapper.convert(same(secondJourney)))
                .thenReturn(secondJourneyDto);

        List<TransferTrainsDto> result = scheduleDataService
                .trainsWithTransfer(stationFrom, stationTo,
                        dateTime, searchType);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationFrom));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationTo));
        verify(mockScheduleDao, times(1))
                .getTransferStations(same(first), same(second));
        verify(mockScheduleDao, times(1))
                .trainsFromToByDeparture(same(first), same(transfer),
                        same(dateTime));
        verify(mockScheduleDao, times(1))
                .firstTrainAfter(same(transfer), same(second),
                        eq(transferStop.getArrival()
                                .plusMinutes(TRANSFER_TIME)));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(firstJourney));
        verify(mockMapper, times(1)).convert(same(secondJourney));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(1, result.size());
        assertSame(firstJourneyDto, result.get(0).getFirstTrain());
        assertSame(secondJourneyDto, result.get(0).getSecondTrain());
    }

    @Test
    void transferTrainsByArrivalEmptySecondTrainTest() {
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        LocalDateTime dateTime = LocalDateTime.of(2020, 2, 2, 20, 2);
        String searchType = "arrival";
        Station first = new Station();
        Station second = new Station();
        Station transfer = new Station();
        List<Station> transferStations = Collections
                .singletonList(transfer);
        when(mockScheduleDao.getStationByName(eq(stationFrom)))
                .thenReturn(first);
        when(mockScheduleDao.getStationByName(eq(stationTo)))
                .thenReturn(second);
        when(mockScheduleDao.getTransferStations(any(Station.class),
                any(Station.class))).thenReturn(transferStations);
        when(mockScheduleDao.trainsFromToByArrival(any(Station.class),
                same(second), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        List<TransferTrainsDto> result = scheduleDataService
                .trainsWithTransfer(stationFrom, stationTo,
                        dateTime, searchType);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationFrom));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationTo));
        verify(mockScheduleDao, times(1))
                .getTransferStations(same(first), same(second));
        verify(mockScheduleDao, times(1))
                .trainsFromToByArrival(same(transfer), same(second),
                        same(dateTime));
        verifyNoMoreInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertTrue(result.isEmpty());
    }

    @Test
    void transferTrainsByArrivalEmptyFirstTrainTest() {
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        LocalDateTime dateTime = LocalDateTime.of(2020, 2, 2, 20, 2);
        String searchType = "arrival";
        Station first = new Station();
        Station second = new Station();
        Station transfer = new Station();
        List<Station> transferStations = Collections
                .singletonList(transfer);
        ScheduledStop transferStop = new ScheduledStop();
        transferStop.setStation(transfer);
        transferStop.setDeparture(dateTime.plusHours(2));
        Journey secondJourney = new Journey();
        secondJourney.setStops(Collections.singletonList(transferStop));
        List<Journey> trainsFromTransfer = Collections
                .singletonList(secondJourney);
        when(mockScheduleDao.getStationByName(eq(stationFrom)))
                .thenReturn(first);
        when(mockScheduleDao.getStationByName(eq(stationTo)))
                .thenReturn(second);
        when(mockScheduleDao.getTransferStations(any(Station.class),
                any(Station.class))).thenReturn(transferStations);
        when(mockScheduleDao.trainsFromToByArrival(any(Station.class),
                same(second), any(LocalDateTime.class)))
                .thenReturn(trainsFromTransfer);

        List<TransferTrainsDto> result = scheduleDataService
                .trainsWithTransfer(stationFrom, stationTo,
                        dateTime, searchType);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationFrom));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationTo));
        verify(mockScheduleDao, times(1))
                .getTransferStations(same(first), same(second));
        verify(mockScheduleDao, times(1))
                .trainsFromToByArrival(same(transfer), same(second),
                        same(dateTime));
        verify(mockScheduleDao, times(1)).lastTrainBefore(same(first),
                same(transfer), eq(transferStop.getDeparture()
                        .minusMinutes(TRANSFER_TIME)));
        verifyNoMoreInteractions(mockScheduleDao);
        verifyZeroInteractions(mockMapper);

        assertTrue(result.isEmpty());

    }

    @Test
    void transferTrainsByArrivalSuccessTest() {
        String stationFrom = "stationFrom";
        String stationTo = "stationTo";
        LocalDateTime dateTime = LocalDateTime.of(2020, 2, 2, 20, 2);
        String searchType = "arrival";
        Station first = new Station();
        Station second = new Station();
        Station transfer = new Station();
        List<Station> transferStations = Collections
                .singletonList(transfer);
        ScheduledStop transferStop = new ScheduledStop();
        transferStop.setStation(transfer);
        transferStop.setDeparture(dateTime.plusHours(2));
        Journey secondJourney = new Journey();
        secondJourney.setStops(Collections.singletonList(transferStop));
        List<Journey> trainsFromTransfer = Collections
                .singletonList(secondJourney);
        Journey firstJourney = new Journey();
        JourneyDto firstJourneyDto = new JourneyDto();
        JourneyDto secondJourneyDto = new JourneyDto();
        when(mockScheduleDao.getStationByName(eq(stationFrom)))
                .thenReturn(first);
        when(mockScheduleDao.getStationByName(eq(stationTo)))
                .thenReturn(second);
        when(mockScheduleDao.getTransferStations(any(Station.class),
                any(Station.class))).thenReturn(transferStations);
        when(mockScheduleDao.trainsFromToByArrival(any(Station.class),
                same(second), any(LocalDateTime.class)))
                .thenReturn(trainsFromTransfer);
        when(mockScheduleDao.lastTrainBefore(any(Station.class),
                any(Station.class), any(LocalDateTime.class)))
                .thenReturn(firstJourney);
        when(mockMapper.convert(same(firstJourney)))
                .thenReturn(firstJourneyDto);
        when(mockMapper.convert(same(secondJourney)))
                .thenReturn(secondJourneyDto);

        List<TransferTrainsDto> result = scheduleDataService
                .trainsWithTransfer(stationFrom, stationTo,
                        dateTime, searchType);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationFrom));
        verify(mockScheduleDao, times(1)).getStationByName(same(stationTo));
        verify(mockScheduleDao, times(1))
                .getTransferStations(same(first), same(second));
        verify(mockScheduleDao, times(1))
                .trainsFromToByArrival(same(transfer), same(second),
                        same(dateTime));
        verify(mockScheduleDao, times(1)).lastTrainBefore(same(first),
                same(transfer), eq(transferStop.getDeparture()
                        .minusMinutes(TRANSFER_TIME)));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(firstJourney));
        verify(mockMapper, times(1)).convert(same(secondJourney));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(1, result.size());
        assertSame(firstJourneyDto, result.get(0).getFirstTrain());
        assertSame(secondJourneyDto, result.get(0).getSecondTrain());
    }

    @Test
    void stationScheduleComparatorSameArrivalSameDepartureTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        first.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 20));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        second.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 20));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(firstStop, result.get(0));
        assertSame(secondStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorSameArrivalOnlyFirstDepartureTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        first.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 20));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(firstStop, result.get(0));
        assertSame(secondStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorSameArrivalOnlySecondDepartureTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        second.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 20));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(firstStop, result.get(0));
        assertSame(secondStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorSameArrivalFirstEarlierDepartureTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        first.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 10));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        second.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 20));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(firstStop, result.get(0));
        assertSame(secondStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorSameArrivalSecondEarlierDepartureTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        first.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 20));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        second.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 10));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(secondStop, result.get(0));
        assertSame(firstStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorFirstEarlierArrivalTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setArrival(LocalDateTime.of(2020, 2, 2, 20, 20));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(firstStop, result.get(0));
        assertSame(secondStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorSecondEarlierArrivalTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setArrival(LocalDateTime.of(2020, 2, 2, 20, 20));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(secondStop, result.get(0));
        assertSame(firstStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorFirstArrivalSameAsSecondDepartureTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 2));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(firstStop, result.get(0));
        assertSame(secondStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorFirstArrivalBeforeSecondDepartureTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 20));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(firstStop, result.get(0));
        assertSame(secondStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorFirstArrivalAfterSecondDepartureTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setArrival(LocalDateTime.of(2020, 2, 2, 20, 20));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 2));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(secondStop, result.get(0));
        assertSame(firstStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorFirstDepartureSameAsSecondArrivalTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 2));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(firstStop, result.get(0));
        assertSame(secondStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorFirstDepartureBeforeSecondArrivalTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 2));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setArrival(LocalDateTime.of(2020, 2, 2, 20, 20));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(firstStop, result.get(0));
        assertSame(secondStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorFirstDepartureAfterSecondArrivalTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 20));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setArrival(LocalDateTime.of(2020, 2, 2, 20, 2));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(secondStop, result.get(0));
        assertSame(firstStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorSameDeparturesTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 2));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 2));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(firstStop, result.get(0));
        assertSame(secondStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorFirstEarlierDepartureTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 2));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 20));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(firstStop, result.get(0));
        assertSame(secondStop, result.get(1));
    }

    @Test
    void stationScheduleComparatorSecondEarlierDepartureTest() {
        Journey journey = new Journey();
        ScheduledStop first = new ScheduledStop();
        first.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 20));
        first.setJourney(journey);
        ScheduledStop second = new ScheduledStop();
        second.setDeparture(LocalDateTime.of(2020, 2, 2, 20, 2));
        second.setJourney(journey);
        Station station = new Station();
        String stationName = "stationName";
        LocalDateTime from = LocalDateTime.of(2020, 2, 2, 20, 0);
        ScheduledStopDto firstStop = new ScheduledStopDto();
        ScheduledStopDto secondStop = new ScheduledStopDto();
        when(mockScheduleDao.getStationByName(anyString())).thenReturn(station);
        when(mockScheduleDao.stationSchedule(any(Station.class),
                any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(first, second));
        when(mockMapper.convert(same(first))).thenReturn(firstStop);
        when(mockMapper.convert(same(second))).thenReturn(secondStop);

        List<ScheduledStopDto> result = scheduleDataService
                .stationSchedule(stationName, from);

        verify(mockScheduleDao, times(1)).getStationByName(same(stationName));
        verify(mockScheduleDao, times(1))
                .stationSchedule(same(station), same(from));
        verifyNoMoreInteractions(mockScheduleDao);
        verify(mockMapper, times(1)).convert(same(first));
        verify(mockMapper, times(1)).convert(same(second));
        verifyNoMoreInteractions(mockMapper);

        assertEquals(2, result.size());
        assertSame(secondStop, result.get(0));
        assertSame(firstStop, result.get(1));
    }

}
