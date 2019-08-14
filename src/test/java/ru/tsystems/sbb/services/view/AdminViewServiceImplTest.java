package ru.tsystems.sbb.services.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tsystems.sbb.model.dto.LineDto;
import ru.tsystems.sbb.model.dto.RouteDto;
import ru.tsystems.sbb.services.data.AdminDataService;
import ru.tsystems.sbb.services.data.RouteDataService;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
class AdminViewServiceImplTest {

    @InjectMocks
    private AdminViewServiceImpl adminViewService;

    @Mock
    private RouteDataService mockRouteDataService;

    @Mock
    private AdminDataService mockAdminDataService;

    @BeforeEach
    void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void updateTariffZeroTest() {
        float price = 0;

        adminViewService.updateTariff(price);

        verifyZeroInteractions(mockAdminDataService);
    }

    @Test
    void updateTariffSuccessTest() {
        float price = 1;

        adminViewService.updateTariff(price);

        verify(mockAdminDataService, times(1)).updateTariff(eq(price));
        verifyNoMoreInteractions(mockAdminDataService);
    }

    @Test
    void newRouteStopPatternZeroRouteIdTest() {
        String routeNumber = "routeNumber";
        int routeId = 0;
        int lineId = 0;
        String[] stations = new String[]{};
        LineDto line = new LineDto();

        when(mockRouteDataService.getLine(anyInt())).thenReturn(line);

        Map<String, Object> result = adminViewService
                .newRouteStopPattern(routeNumber, routeId, lineId, stations);

        verify(mockRouteDataService, times(1)).getLine(eq(lineId));
        verifyNoMoreInteractions(mockRouteDataService);

        assertTrue(result.containsKey("routeNumber"));
        assertEquals(routeNumber, result.get("routeNumber"));
        assertTrue(result.containsKey("line"));
        assertEquals(line, result.get("line"));
        assertTrue(result.containsKey("routeStations"));
        assertEquals(stations, result.get("routeStations"));
        assertFalse(result.containsKey("route"));
    }

    @Test
    void newRouteStopPatternNonZeroRouteIdTest() {
        String routeNumber = "routeNumber";
        int routeId = 1;
        int lineId = 0;
        String[] stations = new String[]{};
        LineDto line = new LineDto();
        RouteDto route = new RouteDto();

        when(mockRouteDataService.getLine(anyInt())).thenReturn(line);
        when(mockRouteDataService.getRoute(anyInt())).thenReturn(route);

        Map<String, Object> result = adminViewService
                .newRouteStopPattern(routeNumber, routeId, lineId, stations);

        verify(mockRouteDataService, times(1)).getLine(eq(lineId));
        verify(mockRouteDataService, times(1)).getRoute(eq(routeId));
        verifyNoMoreInteractions(mockRouteDataService);

        assertTrue(result.containsKey("routeNumber"));
        assertEquals(routeNumber, result.get("routeNumber"));
        assertTrue(result.containsKey("line"));
        assertEquals(line, result.get("line"));
        assertTrue(result.containsKey("routeStations"));
        assertEquals(stations, result.get("routeStations"));
        assertTrue(result.containsKey("route"));
        assertEquals(route, result.get("route"));
    }
}
