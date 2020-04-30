package com.khye;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
    private static Logger log = LoggerFactory.getLogger(Configuration.class);
    private int defaultNMemes;
    private Map<String, String> discord;
    private Map<String, String> reddit;
    private Map<String, String> app;

    public void refreshAccessToken(String newAccessToken) {
        // refreshing in memory
        reddit.put("authHeader", "bearer " + newAccessToken);
        // refreshing on disk
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
        try {
            mapper.writeValue(new File(
                    Configuration.class.getClass().getClassLoader().getResource("application-props.yml").getFile()),
                    this);
        } catch (IOException e) {
            log.error("Error refreshing access token on disk");
        }
    }

    public int getDefaultNMemes() {
        return this.defaultNMemes;
    }

    public void setDefaultNMemes(int defaultNMemes) {
        this.defaultNMemes = defaultNMemes;
    }

    public Map<String, String> getDiscord() {
        return this.discord;
    }

    public void setDiscord(Map<String, String> discord) {
        this.discord = discord;
    }

    public Map<String, String> getReddit() {
        return this.reddit;
    }

    public void setReddit(Map<String, String> reddit) {
        this.reddit = reddit;
    }

    public Map<String, String> getApp() {
        return this.app;
    }

    public void setApp(Map<String, String> app) {
        this.app = app;
    }

}