package ru.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connection {

    private Connection conn;

    public connection()
    {
        try {
            final String HOST = "jdbc:mysql://localhost:3306/AutoCompany?characterEncoding=utf8";
            final String USERNAME = "root";
            final String PASSWORD = "Root@1111";
            final Connection conn = DriverManager.getConnection(HOST, USERNAME, PASSWORD);
            this.conn = conn;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public Connection getConnection()
    {
        return this.conn;
    }

}
