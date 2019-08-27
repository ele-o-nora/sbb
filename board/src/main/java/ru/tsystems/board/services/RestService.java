package ru.tsystems.board.services;

import ru.tsystems.dto.ScheduledStopDto;
import ru.tsystems.dto.StationDto;

import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Stateless
public class RestService {

    private static final String TARGET = "http://localhost:8180/railways/rest";

    public StationDto[] getStationList() {
        Client client = ClientBuilder.newClient();
        Response response = client.target(TARGET).path("/stations")
                .request().get();
        return response.readEntity(StationDto[].class);
    }

    public List<ScheduledStopDto> getStationSchedule(final int stationId) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(TARGET).path("/schedule/" + stationId)
                .request().get();
        return Arrays.asList(response.readEntity(ScheduledStopDto[].class));
    }
}
