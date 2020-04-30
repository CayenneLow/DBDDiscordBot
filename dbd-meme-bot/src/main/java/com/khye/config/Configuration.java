package com.khye.config;

import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class Configuration {
    private static Logger log = LoggerFactory.getLogger(Configuration.class);
    private DiscordProps discord;
    private RedditProps reddit;
    private AppProps app;

    public Configuration() {
    }

    public Configuration(DiscordProps discord, RedditProps reddit, AppProps app) {
        this.discord = discord;
        this.reddit = reddit;
        this.app = app;
    }

    public void refreshAccessToken(String newAccessToken) {
        // refreshing in memory
        reddit.setAuthHeader("bearer " + newAccessToken);
        // refreshing on disk
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(this, writer);
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
}