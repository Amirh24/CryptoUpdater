package com.randomizer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQLDB {

	static final String driver = "com.mysql.jdbc.Driver";
	static final String url = "jdbc:mysql://localhost:3306/cryptodb";
	static final String username = "root";
	static final String password = "123456";

	public volatile static MySQLDB database = null;
	private static Connection connection;
	String tableName;

	private MySQLDB(String url, String username, String password) throws SQLException {
		connection = DriverManager.getConnection(url, username, password);

	}

	public static MySQLDB getConnection() throws Exception {

		if (database == null) {
			synchronized (MySQLDB.class) {
				if (database == null) {
					try {

						Class.forName(driver);

						database = new MySQLDB(url, username, password);

						System.out.println("Connected");

					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		}
		return database;
	}

	public void createTable(String tableName) throws Exception {

		this.tableName = tableName;
		try {
			PreparedStatement create = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName
					+ "(id INT NOT NULL AUTO_INCREMENT, name VARCHAR(255), symbol VARCHAR(255), price DOUBLE, marketcap DOUBLE, PRIMARY KEY(id))");
			create.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void post(String name, String symbolName, double price, double marketCap) throws Exception {
		PreparedStatement statment = null;
		try {
			 statment = connection
					.prepareStatement("INSERT into " + tableName + "(name, symbol, price, marketcap) VALUES ('" + name
							+ "', '" + symbolName + "', '" + price + "', '" + marketCap + "')");
			 statment.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			System.out.println("finished adding " + name + " to your subscriptions");
		}
	}

	public ArrayList<String> get() throws Exception {
		ArrayList<String> array = new ArrayList<String>();
		PreparedStatement statment = null;
		try {
			statment = connection.prepareStatement("SELECT symbol from " + tableName + " ORDER BY marketcap DESC");
			ResultSet result = statment.executeQuery();
			array = new ArrayList<String>();
			while (result.next()) {
				array.add(result.getString("symbol"));
			}
			result.close();
			System.out.println("Finished getting subscriptions");
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (statment != null)
					connection.close();
			} catch (SQLException se) {
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return array;
	}

	public void delete(String symbolName) throws Exception {
		try {
			PreparedStatement statment = connection
					.prepareStatement("DELETE FROM " + tableName + " WHERE symbol = '" + symbolName + "'");
			statment.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
