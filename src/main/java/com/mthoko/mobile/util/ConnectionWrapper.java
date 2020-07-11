package com.mthoko.mobile.util;

import java.sql.Connection;

public class ConnectionWrapper {

    private Connection connection;

    public ConnectionWrapper(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
