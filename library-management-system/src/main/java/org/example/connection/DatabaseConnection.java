package org.example.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Static instance of the class for singleton pattern
    private static DatabaseConnection instance;
    private Connection connection;

    // volatile keyword ensures thread safety for double-checked locking
    private static volatile boolean closed = false;

    // Database credentials - replace with your own
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Deeptavo@2708";

    // Private constructor to prevent direct instantiation
    private DatabaseConnection() {
        try {
            // Load the MySQL JDBC driver
            // This step is important as it registers the driver with the DriverManager
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Public method to get the singleton instance
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Method to get the connection
    public Connection getConnection() throws SQLException {
        if (closed) {
            throw new SQLException("Database connection is closed");
        }
        // Check if connection is valid and reconnect if necessary
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        return connection;
    }

    // Method to close the connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                closed = true;
                instance = null;  // Reset the singleton instance
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to check if connection is closed
    public boolean isClosed() {
        return closed;
    }

}



