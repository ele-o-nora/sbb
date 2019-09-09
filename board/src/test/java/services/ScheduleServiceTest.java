package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tsystems.board.services.RestService;
import ru.tsystems.board.services.ScheduleService;
import ru.tsystems.dto.ScheduledStopDto;
import ru.tsystems.dto.StationDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private RestService restServiceMock;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void initTest() {
        StationDto firstStation = new StationDto();
        firstStation.setName("firstStation");
        firstStation.setId(1);
        StationDto secondStation = new StationDto();
        secondStation.setName("secondStation");
        secondStation.setId(2);
        List<String> names = Arrays.asList("firstStation", "secondStation");
        StationDto[] stations = new StationDto[]{firstStation, secondStation};
        when(restServiceMock.getStationList()).thenReturn(stations);
        List<ScheduledStopDto> schedule = new ArrayList<>();
        when(restServiceMock.getStationSchedule(anyInt())).thenReturn(schedule);

        scheduleService.init();

        verify(restServiceMock, times(1)).getStationList();
        verify(restServiceMock, times(1)).getStationSchedule(eq(1));
        verify(restServiceMock, times(1)).getStationSchedule(eq(2));
        verifyNoMoreInteractions(restServiceMock);

        assertEquals(names, scheduleService.stationsList());
        assertSame(schedule, scheduleService.stationSchedule(1));
        assertSame(schedule, scheduleService.stationSchedule(2));
    }
}
