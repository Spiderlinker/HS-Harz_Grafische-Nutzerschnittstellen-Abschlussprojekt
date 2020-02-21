package de.hsharz.qwixx.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static final String DATABASE = System.getProperty("user.home") + "\\.qwixx\\ScoreDatabase";
	private static final String DATABASE_URL = "jdbc:hsqldb:file:/" + DATABASE;
	private static final String USER = "SA";
	private static final String PASSWORD = "";
	private static final String DATABASE_DRIVER = "org.hsqldb.jdbc.JDBCDriver";

	// Pool mit Datenbankverbindungen
	private static Connection connection;

	private DatabaseConnection() {
		// Single instance
	}

	/**
	 * Liefert eine neue Connection zu der Datenbank.
	 * 
	 * @return eine neue Connection zu der Datenbank
	 * @throws SQLException Falls
	 */
	public static Connection getConnection() throws SQLException {
		if (isConnectionClosed()) {
			establishConnection();
		}
		return connection;
	}

	/**
	 * 
	 */
	private static void establishConnection() {
		try {
			Class.forName(DATABASE_DRIVER);
			connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gibt an, ob die Verbindung zur Datenbank getrennt wurde bzw. nicht aufgebaut
	 * ist
	 * 
	 * @return true, falls keine Verbindung besteht; andernfalls false
	 * @throws SQLException Fehler beim Prüfen, ob Datenbank geschlossen
	 */
	public static boolean isConnectionClosed() throws SQLException {
		return connection == null || connection.isClosed();
	}

	/**
	 * Schließt alle Connections (den ConnectionPool) zu der Datenbank
	 * 
	 * @throws SQLException Fehler beim Schließen der Connections
	 */
	public static void closeConnection() throws SQLException {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

}
