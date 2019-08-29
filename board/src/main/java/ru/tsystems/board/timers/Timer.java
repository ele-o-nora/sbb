package ru.tsystems.board.timers;

import ru.tsystems.board.controllers.ScheduleBean;

import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@Startup
public class Timer {

    @Inject
    private ScheduleBean scheduleBean;

    /**
     * Calls to ScheduleBean in order to update the schedule maps every hour
     *  at 00 minutes 00 seconds.
     */
    @Schedule(hour = "*")
    public void updateSchedule() {
        scheduleBean.updateSchedule();
    }
}
