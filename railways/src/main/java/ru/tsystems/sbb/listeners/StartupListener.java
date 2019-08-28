package ru.tsystems.sbb.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        jmsTemplate.send(session -> session.createTextMessage("Up&running"));
    }
}
