package ru.tsystems.sbb.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tsystems.sbb.controllers.view.AdminController;
import ru.tsystems.sbb.services.view.AdminViewService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminViewService mockAdminViewService;

    private static final String ROUTE_NUMBER = "routeNumber";

    @BeforeEach
    void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void scheduleRouteNullStationsTest() {
        adminController.scheduleRoute(ROUTE_NUMBER, 1, 1, null);

        verify(mockAdminViewService, times(1))
                .modifyRouteStations(eq(1), eq(1));
        verifyNoMoreInteractions(mockAdminViewService);
    }

    @Test
    void scheduleRouteEmptyStationsTest() {
        adminController.scheduleRoute(ROUTE_NUMBER, 1, 1, new String[]{});

        verify(mockAdminViewService, times(1))
                .modifyRouteStations(eq(1), eq(1));
        verifyNoMoreInteractions(mockAdminViewService);
    }

    @Test
    void scheduleRouteOneStationTest() {
        adminController.scheduleRoute(ROUTE_NUMBER, 1, 1, new String[]{"one"});

        verify(mockAdminViewService, times(1))
                .modifyRouteStations(eq(1), eq(1));
        verifyNoMoreInteractions(mockAdminViewService);
    }

    @Test
    void scheduleRouteSuccessTest() {
        String[] stations = new String[]{"one", "two", "three"};
        adminController.scheduleRoute(ROUTE_NUMBER, 1, 1, stations);

        verify(mockAdminViewService, times(1))
                .newRouteStopPattern(same(ROUTE_NUMBER), eq(1), eq(1),
                        same(stations));
        verifyNoMoreInteractions(mockAdminViewService);
    }
}
