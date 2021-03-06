package com.khye.repository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import com.khye.DTO.Bot;
import com.khye.config.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotRepository extends Repository {
    private static Logger log = LoggerFactory.getLogger(BotRepository.class);
    public BotRepository(Configuration config) {
        super("bots", config);
    }

    public Optional<Bot> findOneByID(UUID uuid) {
        try {
            connect = DriverManager.getConnection(jdbcProps.getUrl(), jdbcProps.getUser(), jdbcProps.getPassword());
            statement = connect.createStatement();
            resultSet = statement.executeQuery(String.format("SELECT * FROM %s WHERE id=\"%s\"", tableName,uuid.toString()));
            if (resultSet.next()) {
                return Optional
                        .of(new Bot(UUID.fromString(resultSet.getString("id")), resultSet.getLong("time_created")));
            }
        } catch (SQLException e) {
            log.error("Problem with finding bot by UUID: {}, error: {}", uuid.toString(), e);
        } finally {
            close();
        }
        return Optional.empty();
    }

    public void save(Bot bot) {
        try {
            connect = DriverManager.getConnection(jdbcProps.getUrl(), jdbcProps.getUser(), jdbcProps.getPassword());
            statement = connect.createStatement();
            statement.executeUpdate(String.format("INSERT INTO `%s` (`id`, `time_created`) VALUES ('%s', '%s')", tableName,bot.getUuid().toString(), bot.getTime_created()));
        } catch (SQLException e) {
            log.error("Problem with saving new bot: {}, error: {}", bot, e);
        } finally {
            close();
        }
    }

    public void delete(Bot bot) {
        try {
            connect = DriverManager.getConnection(jdbcProps.getUrl(), jdbcProps.getUser(), jdbcProps.getPassword());
            statement = connect.createStatement();
            statement.executeUpdate(String.format("DELETE FROM `%s` WHERE `%s`.`id` = '%s'", tableName, tableName, bot.getUuid().toString()));
        } catch (SQLException e) {
            log.error("Problem with deleting bot: {}, error: {}", bot, e);
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