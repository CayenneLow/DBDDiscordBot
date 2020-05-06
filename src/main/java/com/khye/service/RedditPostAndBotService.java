package com.khye.service;

import java.util.Optional;
import java.util.UUID;

import com.khye.DTO.Bot;
import com.khye.DTO.RedditPost;
import com.khye.config.Configuration;
import com.khye.repository.BotRepository;
import com.khye.repository.RedditPostAndBotRepository;
import com.khye.repository.RedditPostRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedditPostAndBotService {

    private Logger log = LoggerFactory.getLogger(RedditPostAndBotService.class);

    RedditPostAndBotRepository redditPostAndBotRepository;

    RedditPostRepository redditPostRepo;

    BotRepository botRepo;

    public RedditPostAndBotService(Configuration config) {
        redditPostAndBotRepository = new RedditPostAndBotRepository(config);
        redditPostRepo = new RedditPostRepository(config);
        botRepo = new BotRepository(config);
    }

    public void saveRelationship(RedditPost post, Bot bot) {
        log.info("Saving new Relationship: {} + {}", post, bot);
        redditPostRepo.save(post);
        redditPostAndBotRepository.save(post, bot);
    }

    public void saveBot(Bot bot) {
        log.info("Saving new Bot: {}", bot);
        botRepo.save(bot);
    }

    public void saveRedditPost(RedditPost post) {
        log.info("Saving new Post: {}", post);
        redditPostRepo.save(post);
    }

    public Optional<Bot> findByBotId(UUID uuid) {
        return botRepo.findOneByID(uuid);
    }

    public Optional<RedditPost> findByRedditPostId(String id) {
        return redditPostRepo.findOneByID(id);
    }

    public Optional<RedditPost> findByRelationship(UUID uuid) {
        return redditPostAndBotRepository.findOneByBotID(uuid);
    }

    public Optional<Bot> findByRelationship(String id) {
        return redditPostAndBotRepository.findOneByRedditPostID(id);
    }
}