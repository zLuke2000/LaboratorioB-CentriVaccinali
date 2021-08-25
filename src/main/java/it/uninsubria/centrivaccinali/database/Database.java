package it.uninsubria.centrivaccinali.database;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private final String utente = "123abc";
    private final String password = "123abc";

    public Database() {}

    public Boolean connect(String utente, String password) {
        if(this.utente.equals(utente) && this.password.equals(password)) {
            try {
                Connection connection = DBHelper.getConnection();
                System.out.println("Connessione stabilita: " + connection);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Autenticazione fallita");
        }
        return false;
    }
}
