package it.uninsubria.laboratoriob.server;

import it.uninsubria.laboratoriob.database.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class ServerCV {

    public static void main(String[] args) throws SQLException {
        System.out.println("Inserire le credenziali di accesso");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        String utente = null;
        System.out.print("utente: ");
        while(true) {
            try {
                utente = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (utente != null && utente.length() == 0) {
                System.err.print("non puo' essere vuoto");
            } else {
                break;
            }
            System.out.print("utente: ");
        }

        String password = null;
        System.out.print("password: ");
        while(true) {
            try {
                password = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (password != null && password.length() == 0) {
                System.err.println("non puo' essere vuoto");
            } else {
                break;
            }
            System.out.print("password: ");
        }
        new Database(utente, password);
    }
}
