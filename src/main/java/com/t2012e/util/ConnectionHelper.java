package com.t2012e.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/%s?useUnicode=true&characterEncoding=utf-8";
    private static final String DATABASE_NAME = "daily-news";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PWD = "";
    private static Connection cnn;

    public static Connection getConnection() throws SQLException {
        if (cnn == null || cnn.isClosed()){
            cnn = DriverManager.getConnection(
                    String.format(DATABASE_URL,DATABASE_NAME),
                    DATABASE_USER,
                    DATABASE_PWD);
        }

        return cnn;
    }
}
