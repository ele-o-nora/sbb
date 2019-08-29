package ru.tsystems.board.controllers;

import lombok.Getter;
import lombok.Setter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tsystems.board.services.ScheduleService;
import ru.tsystems.dto.ScheduledStopDto;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
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

import java.util.List;

@Named
@ApplicationScoped
@Startup
@Getter
@Setter
public class ScheduleBean {

    @Inject
    private ScheduleService scheduleService;

    private static final String DEFAULT_STATION = "King's Landing";
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ScheduleBean.class);

    private List<String> stations;
    private List<ScheduledStopDto> currentSchedule;
    private String currentStation;

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    @Inject
    @Push(channel = "scheduleNews")
    private PushContext pushContext;

    /**
     * Initializes locally maintained list of station names and current
     *  station's schedule. Initializes message listener.
     */
    @PostConstruct
    public void init() {
        currentStation = DEFAULT_STATION;
        stations = scheduleService.stationsList();
        currentSchedule = scheduleService.stationSchedule(currentStation);

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
            LOGGER.error("Failed establish topic subscription", e);
        }
    }

    /**
     * Calls to ScheduleService in order to bring station and schedule maps to
     *  current state. Updates locally maintained list of station names and
     *  current station's schedule. Informs frontend about update.
     */
    public void updateSchedule() {
        LOGGER.info("Updating schedule");
        scheduleService.updateMaps();
        stations = scheduleService.stationsList();
        currentSchedule = scheduleService.stationSchedule(currentStation);
        pushContext.send("update");
    }

    /**
     * Calls to ScheduleService in order to update list of ScheduledStopDto
     *  so that it would properly reflect the current station choice.
     */
    public void changeStation() {
        currentSchedule = scheduleService.stationSchedule(currentStation);
    }

}
