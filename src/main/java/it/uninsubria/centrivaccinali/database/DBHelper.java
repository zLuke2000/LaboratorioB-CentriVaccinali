package it.uninsubria.centrivaccinali.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 */
public class DBHelper {

    /**
     *
     */
    private final static String protocol = "jdbc:postgresql://";

    /**
     *
     */
    private final static String host = "localhost/";

    /**
     *
     */
    private final static String resource = "laboratorioB";

    /**
     *
     */
    private final static String url = protocol + host + resource;

    /**
     *
     */
    private final static String username = "admin_laboratorioB";

    /**
     *
     */
    private final static String password = "&UsCk*s$#wUOkG4r";

    /**
     *
     */
    private static Connection connection = null;

    /**
     *
     */
    public DBHelper() {}

    /**
     *
     * @return
     */
    public static Connection getConnection() {
        if(connection == null) {
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * 
     */
    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection = null;
    }
}