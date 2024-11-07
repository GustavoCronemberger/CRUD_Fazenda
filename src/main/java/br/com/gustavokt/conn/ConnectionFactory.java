package br.com.gustavokt.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() {
        String url = "jdbc:postgresql://localhost:5432/crud_fazenda";
        String username = "gustavokt";
        String password = "admin";
        try {
            return DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}