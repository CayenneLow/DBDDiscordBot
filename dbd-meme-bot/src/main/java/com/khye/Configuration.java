package com.khye;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
    private static Logger log = LoggerFactory.getLogger(Configuration.class);
    private static Configuration singleton = null;
    private static int defaultNMemes;

    private static Map<String, String> discord;
    private static Map<String, String> reddit;
    private static Map<String, String> app;

    public static Configuration getInstance() {
        if (singleton == null) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.findAndRegisterModules();
            try {
                singleton = mapper.readValue(new File("src/main/resources/application-props.yaml"),
                        Configuration.class);
            } catch (IOException e) {
                log.error("Problem with reading properties");
                throw new RuntimeException();
            }
        }
        return singleton;
    }

    public static void refreshAccessToken(String string) {
        // refreshing in memory
        reddit.put("authHeader", "bearer " + string);
        // refresh disk
    }

    public int getDefaultNMemes() {
        return defaultNMemes;
    }

    public Map<String, String> getDiscord() {
        return discord;
    }

    public Map<String, String> getReddit() {
        return reddit;
    }

    public Map<String, String> getApp() {
        return app;
    }

}