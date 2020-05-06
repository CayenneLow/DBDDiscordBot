package com.khye.service;

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

    public void save(RedditPost post, Bot bot) {
        botRepo.save(bot);
        redditPostRepo.save(post);
        redditPostAndBotRepository.save(post, bot);
    }
}