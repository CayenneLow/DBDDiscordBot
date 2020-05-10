package com.khye;

import com.khye.config.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    @SuppressWarnings("unused")
    private Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        JDAConnector jdaConnector;
        if (args.length > 0 && args[0].equals("prod")){
            jdaConnector = new JDAConnector(Configuration.load(true));
        } else {
            jdaConnector = new JDAConnector(Configuration.load(false));
        }
        jdaConnector.connect();
    }
}
