package com.mthoko.learners.common.repo;

import com.mthoko.learners.exception.ApplicationException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public abstract class BaseResourceRemote {

    public static final String APPLICATION_PROPERTIES = "application.properties";

    public Connection getConnection() {
        try {
            String prefix = "spring";
            String timeZonConfig = getAppProperty("datasource.servertimezone");
            String host = getAppProperty(prefix + ".datasource.host");
            String username = getAppProperty(prefix + ".datasource.username");
            String pwd = getAppProperty(prefix + ".datasource.password");
            String db = getAppProperty(prefix + ".datasource.database");
            String url = String.format("jdbc:mysql://%s/%s?serverTimezone=%s&characterEncoding=latin1", host, db,
                    timeZonConfig);
            Class.forName(getAppProperty("datasource.driver"));
            return DriverManager.getConnection(url, username, pwd);
        } catch (ClassNotFoundException | SQLException ex) {
            throw new ApplicationException(
                    "An error occurred while processing your request" + ex.getMessage() + ", please try again later",
                    ex);
        }
    }

    public void execSQL(String sql) {
        try {
            getConnection().createStatement().execute(sql);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    public void execSQL(List<String> sql) {
        try {
            Statement statement = getConnection().createStatement();
            for (String string : sql) {
                statement.execute(string);
            }
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    public Integer retrieveIntFromQuery(String columnName, String sql) throws SQLException {
        ResultSet countCursor = executeQuery(sql);
        Integer result = null;
        if (countCursor.next()) {
            result = countCursor.getInt(columnName);
        }
        return result;
    }

    private ResultSet executeQuery(String sql) {
        try {
            return getConnection().createStatement().executeQuery(sql);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    public String getAppProperty(String propertyName) {
        InputStream resource = getClass().getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES);
        Properties properties = new Properties();
        try {
            properties.load(resource);
            return properties.getProperty(propertyName);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }
}
