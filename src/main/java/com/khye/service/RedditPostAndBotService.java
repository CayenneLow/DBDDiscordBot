package com.khye.service;

import java.util.Optional;
import java.util.UUID;

import com.khye.DTO.Bot;
import com.khye.DTO.RedditPost;
import com.khye.config.Configuration;
import com.khye.repository.BotRepository;
import com.khye.repository.RedditPostAndBotRepository;
import com.khye.repository.RedditPostRepository;

public class RedditPostAndBotService {

    RedditPostAndBotRepository redditPostAndBotRepository;

    RedditPostRepository redditPostRepo;

    BotRepository botRepo;

    public RedditPostAndBotService(Configuration config) {
        redditPostAndBotRepository = new RedditPostAndBotRepository(config);
        redditPostRepo = new RedditPostRepository(config);
        botRepo = new BotRepository(config);
    }

    public void saveRelationship(RedditPost post, Bot bot) {
        redditPostAndBotRepository.save(post, bot);
    }

    public void saveBot(Bot bot) {
        botRepo.save(bot);
    }

    public void saveRedditPost(RedditPost post) {
        redditPostRepo.save(post);
    }

    public Optional<Bot> findByBotId(UUID uuid) {
        return botRepo.findOneByID(uuid);
    }

    public Optional<RedditPost> findByRedditPostId(String id) {
        return redditPostRepo.findOneByID(id);
    }
}