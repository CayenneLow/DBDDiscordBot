package com.khye.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
    @SuppressWarnings("unused")
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