package com.khye.config;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class Configuration {
    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(Configuration.class);
    private DiscordProps discord;
    private RedditProps reddit;
    private JDBCProps jdbc;

    private static Configuration instance = null;

    private AppProps app;

    public Configuration() {
    }

    public Configuration(DiscordProps discord, RedditProps reddit, AppProps app, JDBCProps jdbc) {
        this.discord = discord;
        this.reddit = reddit;
        this.app = app;
        this.jdbc = jdbc;
    }

    public static Configuration load(boolean isProd) {
        if (instance == null) {
            Yaml yaml = new Yaml(new Constructor(Configuration.class));
            InputStream file;
            if (isProd) {
                file = Configuration.class.getClassLoader().getResourceAsStream("application-props-prod.yml");
            } else {
                file = Configuration.class.getClassLoader().getResourceAsStream("application-props-local.yml");
            }
            instance = yaml.load(file);
        } 
        return instance;
    }
    public static Configuration loadTest() {
        if (instance == null) {
            Yaml yaml = new Yaml(new Constructor(Configuration.class));
            InputStream file = Configuration.class.getClassLoader().getResourceAsStream("application-props.yml");   // TODO: Change to test props
            instance = yaml.load(file);
        } 
        return instance;
    }

    public void refreshAccessToken(String newAccessToken) {
        // refreshing in memory
        reddit.setAuthHeader("bearer " + newAccessToken);
    }

    public DiscordProps getDiscord() {
        return this.discord;
    }

    public void setDiscord(DiscordProps discord) {
        this.discord = discord;
    }

    public RedditProps getReddit() {
        return this.reddit;
    }

    public void setReddit(RedditProps reddit) {
        this.reddit = reddit;
    }

    public AppProps getApp() {
        return this.app;
    }

    public void setApp(AppProps app) {
        this.app = app;
    }

    public JDBCProps getJdbc() {
        return this.jdbc;
    }

    public void setJdbc(JDBCProps jdbc) {
        this.jdbc = jdbc;
    }

}