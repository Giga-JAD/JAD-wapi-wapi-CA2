package com.Giga_JAD.Wapi_Wapi.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

//reference: https://www.linkedin.com/learning/java-ee-concurrency-and-multithrea

public class DBConnection {
	private static final Dotenv dotenv = Dotenv.load();

	public static Connection getConnection() {

		String dbUrl = dotenv.get("DB_URL");
        String dbUser = dotenv.get("DB_USER");
        String dbPassword = dotenv.get("DB_PASSWORD");
        String dbClass = dotenv.get("DB_CLASS");

		Connection connection = null;
		try {
			Class.forName(dbClass);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
}