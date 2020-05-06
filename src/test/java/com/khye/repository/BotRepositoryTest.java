package com.khye.repository;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import com.khye.DTO.Bot;
import com.khye.config.Configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BotRepositoryTest {

    BotRepository repo;
    Bot dummyBot;

    @Before
    public void setup() {
        repo = new BotRepository(Configuration.loadTest());
        dummyBot = constructDummyBot();
    }

    @Test
    public void testInsert() {
        repo.save(dummyBot);
        Bot result = repo.findOneByID(dummyBot.getUuid()).get();
        assertEquals(dummyBot, result);
    }

    @After
    public void cleanUp() {
        repo.delete(dummyBot);
    }

    private Bot constructDummyBot() {
        Bot bot = new Bot(UUID.nameUUIDFromBytes("test".getBytes()), System.currentTimeMillis());
        return bot;
    }

}