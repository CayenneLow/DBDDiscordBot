package com.khye;

import java.util.ArrayList;
import java.util.List;

import com.khye.config.Configuration;
import com.khye.config.RedditProps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class RedditIngestor {
    private Logger log = LoggerFactory.getLogger(RedditIngestor.class);
    private String baseEndPoint;
    private Configuration config;
    private RedditProps redditProps;

    public RedditIngestor(Configuration config) {
        this.config = config;
        this.redditProps = config.getReddit();
        this.baseEndPoint = config.getReddit().getOauthBase();
    }

    public List<JSONObject> getHot(String source, String after, String before, Integer count, Integer limit,
            Integer numRequested) {
        HttpResponse<String> response = Unirest.get(baseEndPoint + source + "hot.json")
                .header("Authorization", redditProps.getAuthHeader()).queryString("limit", limit.toString())
                .queryString("count", count.toString()).asString();
        JSONObject responseJson = new JSONObject(response.getBody());
        if (response.getStatus() == 401 || response.getStatus() == 403) {
            log.info("Access token invalid, refreshing");
            refreshAccessToken();
            return getHot(source, after, before, count, limit, numRequested);
        } else if (!response.isSuccess()) {
            log.error("Something went wrong with Reddit API call, status: {}", response.getStatus());
            return null;
        } else {
            List<JSONObject> entries = new ArrayList<>();
            JSONArray children = responseJson.getJSONObject("data").getJSONArray("children");
            while (entries.size() < numRequested) {
                int rand = (int) Math.floor(Math.random() * (children.length()));
                JSONObject jsonData = children.getJSONObject(rand).getJSONObject("data");
                entries.add(jsonData);
            }
            return entries;
        }
    }

    public void refreshAccessToken() {
        HttpResponse<String> response = Unirest.post(redditProps.getRefreshTokenUrl())
                .field("grant_type", "refresh_token").field("refresh_token", redditProps.getRefreshToken()).asString();
        if (response.isSuccess()) {
            JSONObject obj = new JSONObject(response.getBody());
            log.debug("New Access Token: {}", obj.getString("access_token"));
            config.refreshAccessToken(obj.getString("access_token"));
        } else {
            log.error("Something went wrong with refreshing token");
        }
    }

    public void hidePost(String postName) {
        HttpResponse<String> response = Unirest.post(baseEndPoint + "api/hide")
                .header("Authorization", redditProps.getAuthHeader()).queryString("id", postName).asString();
        log.debug("Hide post status: {}", response.getStatus());
    }
}