package it.uninsubria.centrivaccinali.database;

import it.uninsubria.centrivaccinali.enumerator.Qualificatore;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Indirizzo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *  //TODO controllo sicurezza delle query
 */
public class Database {
    private final String utente = "123abc";
    private final String password = "123abc";
    private static Connection conn;
    private static PreparedStatement pstmt;
    private static Statement stmt;

    /**
     *
     */
    public Database() {}

    /**
     *
     * @param utente
     * @param password
     * @return
     */
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
        int result = -1;
        UUID uuid = null;

        // Controllo che ci sia gia' l'indirizzo
        try {
            pstmt = conn.prepareStatement("SELECT id_indirizzo " +
                                              "FROM public.\"IndirizzoCV\" " +
                                              "WHERE (qualificatore = ? AND " +
                                              "nome = ? AND " +
                                              "civico = ? AND " +
                                              "comune = ? AND " +
                                              "provincia = ? AND " +
                                              "cap = ?)");
            pstmt.setString(1, cv.getIndirizzo().getQualificatore().toString());
            pstmt.setString(2, cv.getIndirizzo().getNome());
            pstmt.setString(3, cv.getIndirizzo().getCivico());
            pstmt.setString(4, cv.getIndirizzo().getComune());
            pstmt.setString(5, cv.getIndirizzo().getProvincia());
            pstmt.setInt(6, cv.getIndirizzo().getCap());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                uuid = rs.getObject("id_indirizzo", java.util.UUID.class);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 2;
        }

        // Inserisco il nuovo centro solo se non presente
        if(uuid == null) {
            // Inserisco il nuovo indirizzo
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
                return 3;
            }

            // Recupero l'id univoco dell'indirizzo appena registrato
            if (result == 1) {
                try {
                    pstmt = conn.prepareStatement("SELECT id_indirizzo " +
                            "FROM public.\"IndirizzoCV\" " +
                            "WHERE (qualificatore = ? AND " +
                            "nome = ? AND " +
                            "civico = ? AND " +
                            "comune = ? AND " +
                            "provincia = ? AND " +
                            "cap = ?)");
                    pstmt.setString(1, cv.getIndirizzo().getQualificatore().toString());
                    pstmt.setString(2, cv.getIndirizzo().getNome());
                    pstmt.setString(3, cv.getIndirizzo().getCivico());
                    pstmt.setString(4, cv.getIndirizzo().getComune());
                    pstmt.setString(5, cv.getIndirizzo().getProvincia());
                    pstmt.setInt(6, cv.getIndirizzo().getCap());
                    ResultSet rs = pstmt.executeQuery();
                    if(rs.next()) {
                        uuid = rs.getObject("id_indirizzo", java.util.UUID.class);
                    } else {
                        return 4;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return 5;
                }
            }
        }

        // Scrivo il centro vaccinale
        if(uuid != null) {
            result = -1;
            try {
                pstmt = conn.prepareStatement("INSERT INTO public.\"CentriVaccinali\" (nome, indirizzo, tipologia) "
                                                + "VALUES (?, ?, ?)");
                pstmt.setString(1, cv.getNome());
                pstmt.setObject(2, uuid);
                pstmt.setString(3, cv.getTipologia().toString());
                result = pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return 6;
            }
        }

        // Creo la nuova tabella
        try {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tabelle_cv.Vaccinati_" + cv.getNome().replaceAll(" ", "_") + " ( " +
                    "nomeCentro character varying(50) NOT NULL, " +
                    "nome character varying(50) NOT NULL, " +
                    "cognome character varying(50) NOT NULL, " +
                    "codiceFiscale character varying(16) NOT NULL, " +
                    "dataSomministrazione date NOT NULL, " +
                    "vaccino character varying(16) NOT NULL, " +
                    "idVaccinazione bigint NOT NULL, " +
                    "PRIMARY KEY (codiceFiscale)" +
                    "UNIQUE(idVaccinazione))");
        } catch (SQLException e) {
            e.printStackTrace();
            return 7;
        }
        return result;
    }

    public List<CentroVaccinale> getCentriVaccinali(String x){
        List<CentroVaccinale> arrayNomeCentri = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM public.\"InfoCV\" WHERE nome_centro like ?");
            pstmt.setString(1, x + "%");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                arrayNomeCentri.add(
                        new CentroVaccinale(rs.getString("nome_centro"),
                                new Indirizzo(Qualificatore.valueOf(rs.getString("qualificatore")), rs.getString("nome"),
                                        rs.getString("civico"), rs.getString("comune"), rs.getString("provincia"),
                                        rs.getInt("cap")), TipologiaCentro.valueOf(rs.getString("tipologia"))));
            }
            return arrayNomeCentri;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int registraCittadino(Cittadino c) {
        try {
            pstmt = conn.prepareStatement("INSERT INTO public.\"Cittadini_Registrati\" (nome, cognome, codice_fiscale, email, userid, password, id_vaccino) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, c.getNome());
            pstmt.setString(2, c.getCognome());
            pstmt.setString(3, c.getCodicefiscale());
            pstmt.setString(4, c.getEmail());
            pstmt.setString(5, c.getUserid());
            pstmt.setString(6, c.getPassword());
            pstmt.setObject(7, c.getId_vaccino());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
