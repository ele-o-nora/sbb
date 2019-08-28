package ru.tsystems.board.controllers;

import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@Startup
public class Timer {

    @Inject
    private ScheduleBean scheduleBean;

    @Schedule(hour = "*")
    public void updateSchedule() {
        scheduleBean.updateSchedule();
    }
}
