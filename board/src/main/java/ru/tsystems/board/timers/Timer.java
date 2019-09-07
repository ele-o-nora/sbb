package ru.tsystems.board.timers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tsystems.board.controllers.ScheduleBean;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.enterprise.context.ContextNotActiveException;
import javax.inject.Inject;

@Stateless
public class Timer {

    @Inject
    private ScheduleBean scheduleBean;

    private static final Logger LOGGER = LoggerFactory.getLogger(Timer.class);

    /**
     * Calls to ScheduleBean in order to update the schedule maps every hour
     *  at 00 minutes 00 seconds.
     */
    @Schedule(hour = "*", minute="30")
    public void updateSchedule() {
        try {
            scheduleBean.updateSchedule();
        } catch (ContextNotActiveException e) {
            LOGGER.error("No active boards detected", e);
        }
    }
}
