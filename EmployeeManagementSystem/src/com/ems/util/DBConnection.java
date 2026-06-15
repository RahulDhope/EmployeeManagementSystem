package com.ems.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to manage Oracle JDBC connection.
 * Uses singleton pattern — only one Connection object is created.
 *
 * SETUP:
 *  1. Download ojdbc11.jar from Oracle website
 *  2. In IntelliJ: File > Project Structure > Modules > Dependencies > + > JARs
 *     Add the ojdbc11.jar file
 *  3. Change USERNAME and PASSWORD to your Oracle credentials
 */
public class DBConnection {

    // ── Connection details ────────────────────────────────────────────────────
    private static final String URL      = "jdbc:oracle:thin:@localhost:1521:xe";
    // If using Oracle 21c+, try:  "jdbc:oracle:thin:@localhost:1521/XEPDB1"
    private static final String USERNAME = "system";   // change to your username
    private static final String PASSWORD = "oracle";   // change to your password

    private static Connection connection = null;

    // Private constructor — prevents instantiation
    private DBConnection() {}

    /**
     * Returns a singleton database connection.
     * Creates a new one only if none exists or it is closed.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load Oracle JDBC driver
                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("[DB] Connection established successfully.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Oracle JDBC Driver not found. " +
                        "Add ojdbc11.jar to your project dependencies.\n" + e.getMessage());
            }
        }
        return connection;
    }

    /**
     * Closes the database connection safely.
     * Call this when the application exits.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("[DB] Connection closed.");
            } catch (SQLException e) {
                System.err.println("[DB] Error closing connection: " + e.getMessage());
            }
        }
    }
}
