package com.restaurant.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String DB_PORT = System.getenv().getOrDefault("DB_PORT", "5432");
    private static final String DB_NAME = System.getenv().getOrDefault("DB_NAME", "restaurant");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "postgres");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "restaurant123");

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = String.format("jdbc:postgresql://%s:%s/%s", DB_HOST, DB_PORT, DB_NAME);
            connection = DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
            System.out.println(" Conectado a base de datos: " + DB_NAME + " @ " + DB_HOST + ":" + DB_PORT);
        }
        return connection;
    }

    public static void testConnection() {
        try {
            getConnection();
        } catch (SQLException e) {
            System.err.println(" Error conectando a DB: " + e.getMessage());
        }
    }
}
