package com.khye.repository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import com.khye.DTO.Bot;
import com.khye.DTO.RedditPost;
import com.khye.config.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedditPostAndBotRepository extends Repository {
    private static Logger log = LoggerFactory.getLogger(RedditPostAndBotRepository.class);
    RedditPostRepository redditPostRepository;
    BotRepository botRepository;

    public RedditPostAndBotRepository(Configuration config) {
        super("reddit_posts_and_bots", config);
        redditPostRepository = new RedditPostRepository(config);
        botRepository = new BotRepository(config);
    }

    public Optional<RedditPost> findOneByBotID(UUID uuid) {
        try {
            connect = DriverManager.getConnection(jdbcProps.getUrl(), jdbcProps.getUser(), jdbcProps.getPassword());
            statement = connect.createStatement();
            resultSet = statement.executeQuery(String.format("SELECT * FROM %s WHERE bot_id=\"%s\"", tableName,uuid.toString()));
            if (resultSet.next()) {
                // get reddit post
                String redditPostId = resultSet.getString("reddit_post_id");
                return redditPostRepository.findOneByID(redditPostId);
            }
        } catch (SQLException e) {
            log.error("Problem with finding reddit post by bot UUID: {}, error: {}", uuid.toString(), e);
        } finally {
            close();
        }
        return Optional.empty();
    }

    public Optional<Bot> findOneByRedditPostID(String id) {
        try {
            connect = DriverManager.getConnection(jdbcProps.getUrl(), jdbcProps.getUser(), jdbcProps.getPassword());
            statement = connect.createStatement();
            resultSet = statement.executeQuery(String.format("SELECT * FROM %s WHERE reddit_post_id=\"%s\"", tableName, id));
            if (resultSet.next()) {
                // get bot
                UUID botId = UUID.fromString(resultSet.getString("bot_id"));
                return botRepository.findOneByID(botId);
            }
        } catch (SQLException e) {
            log.error("Problem with finding reddit post by id: {}, error: {}", id, e);
        } finally {
            close();
        }
        return Optional.empty();
    }

    public void save(RedditPost post, Bot bot) {
        try {
            connect = DriverManager.getConnection(jdbcProps.getUrl(), jdbcProps.getUser(), jdbcProps.getPassword());
            statement = connect.createStatement();
            statement.executeUpdate(String.format("INSERT INTO `%s` (`bot_id`, `reddit_post_id`) VALUES ('%s', '%s')", tableName, bot.getUuid(), post.getId()));
        } catch (SQLException e) {
            log.error("Problem with saving new reddit post and bot relationship. Error: {}", e);
        } finally {
            close();
        }
    }

    public void delete(RedditPost post) {
        try {
            connect = DriverManager.getConnection(jdbcProps.getUrl(), jdbcProps.getUser(), jdbcProps.getPassword());
            statement = connect.createStatement();
            statement.executeUpdate(String.format("DELETE FROM `%s` WHERE `%s`.`reddit_post_id` = '%s'", tableName, tableName, post.getId()));
        } catch (SQLException e) {
            log.error("Problem with deleting relationship, error: {}", e);
        } finally {
            close();
        }
    }

    public void delete(Bot bot) {
        try {
            connect = DriverManager.getConnection(jdbcProps.getUrl(), jdbcProps.getUser(), jdbcProps.getPassword());
            statement = connect.createStatement();
            statement.executeUpdate(String.format("DELETE FROM `%s` WHERE `%s`.`bot_id` = '%s'", tableName, tableName, bot.getUuid()));
        } catch (SQLException e) {
            log.error("Problem with deleting relationship, error: {}", e);
        } finally {
            close();
        }
    }

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
            log.error("Error closing: {}", e);
        }
    }

}