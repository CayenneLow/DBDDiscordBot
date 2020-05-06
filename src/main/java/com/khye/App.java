package com.khye;

import com.khye.config.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    @SuppressWarnings("unused")
    private Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        JDAConnector jdaConnector = new JDAConnector(Configuration.load());
        jdaConnector.connect();
    }
}
