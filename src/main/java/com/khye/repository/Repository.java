package com.khye.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.khye.config.Configuration;
import com.khye.config.JDBCProps;

public abstract class Repository {
    protected JDBCProps jdbcProps;
    protected Connection connect;
    protected Statement statement;
    protected ResultSet resultSet;

    protected String tableName; 

    public Repository(String tableName, Configuration config) {
        this.tableName = tableName;
        jdbcProps = config.getJdbc();
    }
}