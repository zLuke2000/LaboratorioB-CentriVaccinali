//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che fornisce la funzionalit&amp;amp;agrave di connessione e la disconessione al database.
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele 742495
 * @author Pintonello Christian 741112
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

    /** Riferiemento alla connection a database. */
    private static Connection connection = null;

    /** Costruttore primario della classe. */
    public DBHelper() {}


    /**
     * Metodo che instaura la connesione a database.
     * @param host indirizzo del database
     * @param user nome del profilo del database
     * @param password password del profilo del database
     * @return istanza della classe Connection.
     */
    public static Connection getConnection(String host, String user, String password) {
        if(connection == null) {
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                System.err.println("[DBHelper] credenziali database errate o database non corretto");
            }
        }
        return connection;
    }
}