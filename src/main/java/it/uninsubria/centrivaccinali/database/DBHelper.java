package it.uninsubria.centrivaccinali.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {

        private final static String protocol = "jdbc:postgresql://";
        private final static String host = "localhost/";
        private final static String resource = "laboratorioB";
        private final static String url = protocol + host + resource;

        private final static String username = "admin_laboratorioB";
        private final static String password = "&UsCk*s$#wUOkG4r";

        private static Connection connection = null;

        public DBHelper() {}

        public static Connection getConnection() throws SQLException {

            if(connection == null) {
                connection = DriverManager.getConnection(url, username, password);
            }

            return connection;
        }

        public static void closeConnection() throws SQLException {
            connection.close();
            connection = null;
        }
}