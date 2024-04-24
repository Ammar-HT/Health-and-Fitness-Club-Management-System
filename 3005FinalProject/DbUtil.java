package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *  this is a static class to provide a way to connect to the db in multiple functions without re-supplying
 *  the parameters such as url, user, password
 */
public class DbUtil {
    private static final String url = "jdbc:postgresql://localhost:5432/a3q1"; //need to update to current database
    private static final String user = "postgres"; // replace with your own username
    static final String password = "umarjan"; // replace with your own password

    /**
     * This method is used to establish a connection to the PostgreSQL database.
     * It uses the DriverManager's getConnection method to establish a connection using the database URL, username, and password.
     * and it returns a Connection object which is used by the calling funciton to execute SQL queries.
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
