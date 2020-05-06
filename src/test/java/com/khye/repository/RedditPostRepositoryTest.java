package com.khye.repository;

import static org.junit.Assert.assertEquals;

import com.khye.DTO.RedditPost;
import com.khye.config.Configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RedditPostRepositoryTest {

    RedditPostRepository repo;
    RedditPost dummyPost;

    @Before
    public void setup() {
        repo = new RedditPostRepository(Configuration.loadTest());
        dummyPost = constructDummyPost();

    }

    @Test
    public void testInsert() {
        repo.save(dummyPost);
        RedditPost result = repo.findOneByID(dummyPost.getId()).get();
        assertEquals(dummyPost, result);
    }

    @After
    public void cleanUp() {
        repo.delete(dummyPost);
    }

    private RedditPost constructDummyPost() {
        RedditPost bot = new RedditPost("t3_test", "/r/test");
        return bot;
    }

}