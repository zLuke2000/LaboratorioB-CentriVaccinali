package it.uninsubria.centrivaccinali.database;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private final String utente = "123abc";
    private final String password = "123abc";

    public Database(String utente, String password) throws SQLException {
        if(this.utente.equals(utente) && this.password.equals(password)) {
            Connection connection = DBHelper.getConnection();
            System.out.println("Connessione stabilita: " + connection);
        } else {
            System.err.println("Autenticazione fallita");
        }
    }
}
