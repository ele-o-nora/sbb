package ru.tsystems.sbb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.tsystems.sbb.services.data.AdminDataService;
import ru.tsystems.sbb.services.data.PassengerDataService;
import ru.tsystems.sbb.services.data.RouteDataService;
import ru.tsystems.sbb.services.data.ScheduleDataService;
import ru.tsystems.sbb.services.view.AdminViewService;
import ru.tsystems.sbb.services.view.AdminViewServiceImpl;
import ru.tsystems.sbb.services.view.PassengerViewService;
import ru.tsystems.sbb.services.view.PassengerViewServiceImpl;
import ru.tsystems.sbb.services.view.ScheduleViewService;
import ru.tsystems.sbb.services.view.ScheduleViewServiceImpl;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan(basePackages = "ru.tsystems.sbb.services.view")
public class ViewServiceTestConfig {

    private final static LocalDate LOCAL_DATE = LocalDate.of(2020, 2, 2);

    @Bean
    public AdminViewService adminViewService() {
        return new AdminViewServiceImpl();
    }

    @Bean
    public PassengerViewService passengerViewService() {
        return new PassengerViewServiceImpl();
    }

    @Bean
    public ScheduleViewService scheduleViewService() {
        return new ScheduleViewServiceImpl();
    }

    @Bean
    public AdminDataService adminDataService() {
        return mock(AdminDataService.class);
    }

    @Bean
    public PassengerDataService passengerDataService() {
        return mock(PassengerDataService.class);
    }

    @Bean
    public RouteDataService routeDataService() {
        return mock(RouteDataService.class);
    }

    @Bean
    public ScheduleDataService scheduleDataService() {
        return mock(ScheduleDataService.class);
    }

    @Bean
    public Clock clock() {
        return Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId
                .systemDefault()).toInstant(), ZoneId.systemDefault());
    }

}
