package it.uninsubria.centrivaccinali.database;

import it.uninsubria.centrivaccinali.models.CentroVaccinale;

import java.sql.*;
import java.util.UUID;

public class Database {
    private final String utente = "123abc";
    private final String password = "123abc";
    private static Connection conn;
    private static PreparedStatement pstmt;
    private static Statement stmt;

    public Database() {}

    public Boolean connect(String utente, String password) {
        if(this.utente.equals(utente) && this.password.equals(password)) {
            try {
                conn = DBHelper.getConnection();
                System.out.println("Connessione stabilita: " + conn);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Autenticazione fallita");
        }
        return false;
    }

    public int registraCentroVaccinale(CentroVaccinale cv) {
        // Scrivo l'indirizzo
        int result = -1;
        UUID uuid = null;

        try {
            pstmt = conn.prepareStatement("INSERT INTO public.\"IndirizzoCV\" (qualificatore, nome, civico, comune, provincia, cap) "
                                            + "VALUES (?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, cv.getIndirizzo().getQualificatore().toString());
            pstmt.setString(2, cv.getIndirizzo().getNome());
            pstmt.setString(3, cv.getIndirizzo().getCivico());
            pstmt.setString(4, cv.getIndirizzo().getComune());
            pstmt.setString(5, cv.getIndirizzo().getProvincia());
            pstmt.setInt(6, cv.getIndirizzo().getCap());
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 2;
        }

        // Recupero l'id univoco dell'indirizzo appena registrato
        if(result == 1) {
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id_indirizzo " +
                                                     "FROM public.\"IndirizzoCV\" " +
                                                     "WHERE qualificatore = '" + cv.getIndirizzo().getQualificatore().toString() + "'");
                rs.next();
                uuid = rs.getObject("id_indirizzo", java.util.UUID.class);
            } catch (SQLException e) {
                e.printStackTrace();
                return 3;
            }
        }

        // Scrivo il centro vaccinale
        if(uuid != null) {
            result = -1;
            try {
                pstmt = conn.prepareStatement("INSERT INTO public.\"CentriVaccinali\" (nome, indirizzo, tipologia) "
                                                + "VALUES (?, ?, ?)");
                pstmt.setString(1, cv.getIndirizzo().getQualificatore().toString());
                pstmt.setObject(2, uuid);
                pstmt.setString(3, cv.getIndirizzo().getCivico());
                result = pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return 4;
            }
        }

        // TODO Creare la tabella per il centro vaccinale corrente
        return result;
    }
}
