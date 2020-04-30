package com.khye;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class JDAConnector {
    private static Logger log = LoggerFactory.getLogger(JDAConnector.class);
    private static Configuration config = Configuration.getInstance();
    public void connect() {
        try {
            JDA jda = JDABuilder.createDefault(config.getDiscord().get("botToken")).build();
            jda.addEventListener(new EventListener());
        } catch (LoginException e) {
            log.error(e.getMessage());
        }
    }
}
