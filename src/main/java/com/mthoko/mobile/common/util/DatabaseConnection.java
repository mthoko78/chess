package com.mthoko.mobile.common.util;

import java.sql.*;

import static com.mthoko.mobile.common.util.MyConstants.print;

/**
 *
 * @author Mthoko
 */

public class DatabaseConnection {
	public Connection conn;
	public Statement statement;
	public ResultSet resultSet;
	public String dbName = "academic";

	public DatabaseConnection(String dbName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root", "");
			statement = conn.createStatement();
		} catch (SQLException | ClassNotFoundException exc) {
			print("Error: " + exc.toString());
		}
	}

	public DatabaseConnection() {
		this("mthoko");
	}

	public static Connection getConnection(String dbName) {
		return new DatabaseConnection(dbName).getConn();
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public void close() {
		try {
			this.conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
