package ru.tsystems.board.controllers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tsystems.board.services.ScheduleService;
import ru.tsystems.dto.ScheduledStopDto;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
@NoArgsConstructor
public class ScheduleBean implements Serializable {

    @Inject
    private ScheduleService scheduleService;

    private static final transient Logger LOGGER = LoggerFactory
            .getLogger(ScheduleBean.class);

    @Getter
    private List<String> stations;

    @Getter
    private List<ScheduledStopDto> currentSchedule;

    @Getter
    @Setter
    private String currentStation;
    private int currentStationId;

    private transient ConnectionFactory connectionFactory;
    private transient Connection connection;
    private transient Session session;
    private transient MessageConsumer consumer;

    @Inject
    @Push(channel = "scheduleNews")
    private PushContext pushContext;

    /**
     * Initializes locally maintained list of station names and current
     * station's schedule. Initializes message listener.
     */
    @PostConstruct
    public void init() {
        stations = scheduleService.stationsList();
        currentStation = scheduleService.getStationById(1);
        currentStationId = scheduleService.getStationId(currentStation);
        currentSchedule = scheduleService.stationSchedule(currentStationId);

        try {
            connectionFactory =
                    new ActiveMQConnectionFactory("tcp://localhost:61616");
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("railways");
            consumer = session.createConsumer(topic);
            MessageListener listener = message -> updateSchedule();
            consumer.setMessageListener(listener);
        } catch (JMSException e) {
            LOGGER.error("Failed to establish topic subscription", e);
        }
    }

    /**
     * Calls to ScheduleService in order to bring station and schedule maps to
     * current state. Updates locally maintained list of station names and
     * current station's schedule. Informs frontend about update.
     */
    public void updateSchedule() {
        LOGGER.info("Updating schedule");
        scheduleService.updateMaps();
        stations = scheduleService.stationsList();
        currentSchedule = scheduleService.stationSchedule(currentStationId);
        currentStation = scheduleService.getStationById(currentStationId);
        pushContext.send("update");
    }

    /**
     * Calls to ScheduleService in order to update list of ScheduledStopDto
     * so that it would properly reflect the current station choice.
     */
    public void changeStation() {
        currentStationId = scheduleService.getStationId(currentStation);
        currentSchedule = scheduleService.stationSchedule(currentStationId);
    }

}
