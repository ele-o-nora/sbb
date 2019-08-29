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

    /**
     * Executes request to get current array of stations.
     * @return StationDto[] that was returned as REST response
     */
    public StationDto[] getStationList() {
        Client client = ClientBuilder.newClient();
        Response response = client.target(TARGET).path("/stations")
                .request().get();
        return response.readEntity(StationDto[].class);
    }

    /**
     * Executes request to get schedule for specific station.
     * @param stationId id of the station to be used in REST request
     * @return List of ScheduledStopDto from array that was returned as
     *  REST response
     */
    public List<ScheduledStopDto> getStationSchedule(final int stationId) {
        Client client = ClientBuilder.newClient();
        Response response = client.target(TARGET).path("/schedule/" + stationId)
                .request().get();
        return Arrays.asList(response.readEntity(ScheduledStopDto[].class));
    }
}
