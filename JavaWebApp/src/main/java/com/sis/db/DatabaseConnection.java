package com.sis.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DRIVER =
            "com.mysql.cj.jdbc.Driver";

    private static final String URL =
            System.getenv("DB_URL");

    private static final String USER =
            System.getenv("DB_USER");

    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    public static Connection getConnection()
            throws SQLException {

        try {

            Class.forName(DRIVER);

        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}