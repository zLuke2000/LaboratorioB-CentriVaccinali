package it.uninsubria.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che fornisce la funzionalit&amp;amp;agrave di connessione e la disconessione al database...
 */
public class DBHelper {

    /**
     * path-protocol standard postgresql.
     */
    private final static String protocol = "jdbc:postgresql://";


    /**
     * host del database.
     */
    private final static String host = "localhost/";


    /**
     * nome della resource di database.
     */
    private final static String resource = "laboratorioB";


    /**
     * url generato dalla concatenazione del protcol, host e nome del database.
     */
    private final static String url = protocol + host + resource;


    /**
     * username per gli admin del progetto su database.
     */
    private final static String username = "admin_laboratorioB";


    /**
     * Password per poter accedere al progetto su database.
     */
    private final static String password = "&UsCk*s$#wUOkG4r";


    /**
     * Riferiemento alla connection a database.
     */
    private static Connection connection = null;


    /**
     * Costruttore primario della classe.
     */
    public DBHelper() {}


    /**
     * Metodo che instaura la connesione a database.
     * @return istanza della classe Connection.
     */
    public static Connection getConnection() {
        if(connection == null) {
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                System.err.println("[DBHelper] credenziali database errate o database non corretto");
            }
        }
        return connection;
    }


    /**
     * Metodo per eseguire la chiusura della connessione a database.
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