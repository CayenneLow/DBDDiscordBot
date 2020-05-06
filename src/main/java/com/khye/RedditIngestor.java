package com.khye;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.khye.DTO.Bot;
import com.khye.DTO.RedditPost;
import com.khye.config.Configuration;
import com.khye.config.RedditProps;
import com.khye.service.RedditPostAndBotService;

import org.apache.commons.lang3.tuple.Pair;
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

    private RedditPostAndBotService redditPostAndBotService;

    public RedditIngestor(Configuration config) {
        this.config = config;
        this.redditProps = config.getReddit();
        this.baseEndPoint = config.getReddit().getOauthBase();
        redditPostAndBotService = new RedditPostAndBotService(config);
    }

    public List<JSONObject> getContent(Bot bot, String source, String after, String before, Integer count, Integer limit,
            Integer numRequested) {
        HttpResponse<String> response = Unirest.get(baseEndPoint + source)
                .header("Authorization", redditProps.getAuthHeader()).queryString("limit", limit.toString())
                .queryString("count", count.toString()).asString();
        JSONObject responseJson = new JSONObject(response.getBody());
        if (response.getStatus() == 401 || response.getStatus() == 403) {
            log.info("Access token invalid, refreshing");
            refreshAccessToken();
            return getContent(bot, source, after, before, count, limit, numRequested);
        } else if (!response.isSuccess()) {
            log.error("Something went wrong with Reddit API call, status: {}", response.getStatus());
            return null;
        } else {
            List<JSONObject> entries = new ArrayList<>();
            JSONArray children = responseJson.getJSONObject("data").getJSONArray("children");
            while (entries.size() < numRequested) {
                int rand = (new Random()).nextInt(children.length());
                JSONObject jsonData = children.getJSONObject(rand).getJSONObject("data");

                // check if we've seen this post before
                Optional<Pair<Bot, RedditPost>> queryRelationship = redditPostAndBotService.findByRelationship(bot.getUuid(), jsonData.getString("name"));
                if (queryRelationship.isPresent()) continue;

                if (jsonData.getBoolean("is_self") || jsonData.getBoolean("is_video")) {
                    log.debug("Got self/v.redd.it post");
                    continue;
                } else {
                    entries.add(jsonData);
                    // add to db
                    RedditPost post = new RedditPost(jsonData.getString("name"), jsonData.getString("permalink"));
                    redditPostAndBotService.saveRelationship(post, bot);
                }
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
}