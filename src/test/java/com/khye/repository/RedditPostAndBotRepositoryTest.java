package com.khye.repository;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import com.khye.DTO.Bot;
import com.khye.DTO.RedditPost;
import com.khye.config.Configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RedditPostAndBotRepositoryTest {

    RedditPostAndBotRepository redditPostAndBotRepository;

    RedditPostRepository redditPostRepo;
    RedditPost dummyPost;

    BotRepository botRepo;
    Bot dummyBot;

    @Before
    public void setup() {
        redditPostAndBotRepository = new RedditPostAndBotRepository(Configuration.loadTest());

        redditPostRepo = new RedditPostRepository(Configuration.loadTest());
        dummyPost = constructDummyPost();

        botRepo = new BotRepository(Configuration.loadTest());
        dummyBot = constructDummyBot();

        // insert entry into bot and reddit
        botRepo.save(dummyBot);
        redditPostRepo.save(dummyPost);
    }

    @Test
    public void testInsert() {
        redditPostAndBotRepository.save(dummyPost, dummyBot);
        // check bot
        Bot botResult = redditPostAndBotRepository.findOneByRedditPostID(dummyPost.getId()).get();
        Bot botExpected = botRepo.findOneByID(dummyBot.getUuid()).get();
        assertEquals(botExpected, botResult);
        // check reddit post
        RedditPost redditResult = redditPostAndBotRepository.findOneByBotID(dummyBot.getUuid()).get();
        RedditPost redditExpected = redditPostRepo.findOneByID(dummyPost.getId()).get();
        assertEquals(redditExpected, redditResult);
    }

    @After
    public void cleanUp() {
        redditPostAndBotRepository.delete(dummyBot);
        botRepo.delete(dummyBot);
        redditPostRepo.delete(dummyPost);
    }

    private RedditPost constructDummyPost() {
        RedditPost bot = new RedditPost("t3_test", "/r/test");
        return bot;
    }

    private Bot constructDummyBot() {
        Bot bot = new Bot(UUID.nameUUIDFromBytes("test".getBytes()), System.currentTimeMillis());
        return bot;
    }

}