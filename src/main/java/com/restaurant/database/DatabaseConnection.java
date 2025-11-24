package com.restaurant.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DatabaseConnection {
    // Singleton: Only one connection instance
    private static Connection connection = null;
    
    // SQLite database URL
    // Format: jdbc:sqlite:path/to/database.db
    private static final String DEFAULT_DB_URL = "jdbc:sqlite:database/restaurant.db";
    private static String dbUrl = DEFAULT_DB_URL;
    
    // Private constructor - prevents instantiation
    private DatabaseConnection() {
        // Singleton pattern: Can't create instances
    }
    
    
    // Get the database connection (Singleton pattern)
    // Creates connection if it doesn't exist
    public static Connection getConnection() {
        try {
            // Check if connection exists and is valid
            if (connection == null || connection.isClosed()) {
                // Create new connection
                connection = DriverManager.getConnection(dbUrl);
                System.out.println("✅ Connected to database: " + dbUrl);
                
                // Initialize database (create tables)
                initializeDatabase();
            }
        } catch (SQLException e) {
            System.err.println("❌ Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    
  
    // Initialize database by executing schema.sql
    // Creates all tables if they don't exist
     
    private static void initializeDatabase() {
        try {
            // Read schema.sql from resources
            InputStream inputStream = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("database/schema.sql");
            
            if (inputStream == null) {
                System.err.println("❌ schema.sql not found in resources!");
                return;
            }
            
            // Read the SQL file
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
            );
            
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }
            reader.close();
            
            // Split by semicolon to get individual statements
            String[] statements = sql.toString().split(";");
            
            // Execute each CREATE TABLE statement
            Statement stmt = connection.createStatement();
            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty()) {
                    stmt.execute(statement);
                }
            }
            stmt.close();
            
            System.out.println("✅ Database tables initialized successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    // Close the database connection
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                System.out.println("✅ Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error closing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Test the database connection
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    // Override DB URL (useful for tests)
    public static void setDatabaseUrl(String url) {
        dbUrl = (url == null || url.isBlank()) ? DEFAULT_DB_URL : url;
        closeConnection();
    }
}
