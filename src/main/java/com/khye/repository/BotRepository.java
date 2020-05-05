package com.khye.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

import com.khye.DTO.Bot;
import com.khye.config.Configuration;
import com.khye.config.JDBCProps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotRepository {
    private static Logger log = LoggerFactory.getLogger(BotRepository.class);

    private JDBCProps jdbcProps;
    private Connection connect;
    private Statement statement;
    private ResultSet resultSet;

    public BotRepository(Configuration config) {
        this.jdbcProps = config.getJdbcProps();
    }

    public Optional<Bot> findOneByID(UUID uuid) {
        try {
            connect = DriverManager.getConnection(jdbcProps.getUrl(), jdbcProps.getUser(), jdbcProps.getPassword());
            statement = connect.createStatement();
            resultSet = statement.executeQuery(String.format("SELECT * FROM bots WHERE id=%s", uuid.toString()));
            if (resultSet.first()) {
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
            connect = DriverManager.getConnection(jdbcProps.getUrl(), jdbcProps.getUser(),
                    jdbcProps.getPassword());
            statement = connect.createStatement();
            resultSet = statement.executeQuery(String.format("INSERT INTO `bots` (`id`, `time_created`) VALUES ('%s', '%s')", bot.getUuid().toString(), bot.getTime_created()));
        } catch (SQLException e) {
            log.error("Problem with saving new bot: {}, error: {}", bot, e);
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