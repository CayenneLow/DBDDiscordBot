package com.khye.repository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import com.khye.DTO.RedditPost;
import com.khye.config.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedditPostRepository extends Repository {
    private static Logger log = LoggerFactory.getLogger(RedditPostRepository.class);

    public RedditPostRepository(Configuration config) {
        super("reddit_posts", config);
    }

    public Optional<RedditPost> findOneByID(String id) {
        try {
            connect = DriverManager.getConnection(jdbcProps.getUrl(), jdbcProps.getUser(), jdbcProps.getPassword());
            statement = connect.createStatement();
            resultSet = statement.executeQuery(String.format("SELECT * FROM %s WHERE id=\"%s\"", tableName,id));
            if (resultSet.next()) {
                return Optional.of(new RedditPost(resultSet.getString("id"), resultSet.getString("permalink")));
            }
        } catch (SQLException e) {
            log.error("Problem with finding reddit post by id: {}, error: {}", id, e);
        } finally {
            close();
        }
        return Optional.empty();
    }

    public void save(RedditPost post) {
        try {
            connect = DriverManager.getConnection(jdbcProps.getUrl(), jdbcProps.getUser(), jdbcProps.getPassword());
            statement = connect.createStatement();
            statement.executeUpdate(String.format("INSERT INTO `%s` (`id`, `permalink`) VALUES ('%s', '%s')", tableName, post.getId(), post.getPermalink()));
        } catch (SQLException e) {
            log.error("Problem with saving new reddit post: {}, error: {}", post, e);
        } finally {
            close();
        }
    }

    public void delete(RedditPost post) {
        try {
            connect = DriverManager.getConnection(jdbcProps.getUrl(), jdbcProps.getUser(), jdbcProps.getPassword());
            statement = connect.createStatement();
            statement.executeUpdate(String.format("DELETE FROM `%s` WHERE `%s`.`id` = '%s'", tableName, tableName, post.getId()));
        } catch (SQLException e) {
            log.error("Problem with deleting Reddit Post: {}, error: {}", post, e);
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