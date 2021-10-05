package it.uninsubria.centrivaccinali.database;

import it.uninsubria.centrivaccinali.enumerator.Qualificatore;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Indirizzo;
import it.uninsubria.centrivaccinali.models.Vaccinato;

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
                pstmt = conn.prepareStatement("INSERT INTO public.\"IndirizzoCV\" (id_indirizzo, qualificatore, nome, civico, comune, provincia, cap) "
                                                + "VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)");
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
            // TODO da sistemare
            stmt=conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tabelle_cv.Vaccinati_" + cv.getNome().replaceAll(" ", "_") + " ( " +
                    "nome_centro varchar(50) NOT NULL, " +
                    "nome varchar(50) NOT NULL, " +
                    "cognome varchar(50) NOT NULL, " +
                    "codice_fiscale varchar(16) NOT NULL UNIQUE, " +
                    "data_somministrazione date NOT NULL, " +
                    "vaccino varchar(16) NOT NULL, " +
                    "id_vaccinazione bigint NOT NULL PRIMARY KEY);");
        } catch (SQLException e) {
            e.printStackTrace();
            return 7;
        }
        return result;
    }

    //TODO metodo login utente

    public int loginUtente(String username, String password) {
        try {
            pstmt=conn.prepareStatement("SELECT * FROM public.\"Cittadini_Registrati\" WHERE userid="+username+" AND password="+password);
            ResultSet rs=pstmt.executeQuery();
            if (rs.next()){
                //TODO recupera info cittadino
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
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
            pstmt.setLong(7, c.getId_vaccino());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int registraVaccinato(Vaccinato nuovoVaccinato) {
        System.out.println("registrazione in corso");
        try {
            String nomeCentro=nuovoVaccinato.getNomeCentro();
            pstmt = conn.prepareStatement("INSERT INTO tabelle_cv.\"vaccinati_" + nomeCentro + "\" (nome_centro, nome, cognome, codice_fiscale, data_somministrazione, vaccino, id_vaccinazione) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, nuovoVaccinato.getNomeCentro());
            pstmt.setString(2, nuovoVaccinato.getNome());
            pstmt.setString(3, nuovoVaccinato.getCognome());
            pstmt.setString(4, nuovoVaccinato.getCodiceFiscale());
            pstmt.setDate(5, nuovoVaccinato.getDataSomministrazione());
            pstmt.setString(6, String.valueOf(nuovoVaccinato.getVaccinoSomministrato()));
            pstmt.setLong(7, nuovoVaccinato.getIdVaccino());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("[Database] registrato nuovo vaccinato");
        return -1;
    }

    /**
     * FUNZIONANTE
     * @param provincia
     * @return
     */
    public List<String> getComuni(String provincia){
        List<String> arrayComuni = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("SELECT comune " +
                                              "FROM public.\"IndirizzoCV\" " +
                                              "WHERE provincia = ? " +
                                              "ORDER BY comune");
            pstmt.setString(1, provincia);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                arrayComuni.add(rs.getString("comune"));
            }
            return arrayComuni;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * FUNZIONANTE
     * @param comune
     * @return
     */
    public List<CentroVaccinale> getCentriVaccinali(String comune){
        List<CentroVaccinale> arrayNomeCentri = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM public.\"InfoCV\" " +
                    "WHERE comune = ? " +
                    "ORDER BY nome_centro");
            pstmt.setString(1, comune);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                arrayNomeCentri.add(new CentroVaccinale(rs.getString("nome_centro"),
                        new Indirizzo(Qualificatore.valueOf(rs.getString("qualificatore")),
                                rs.getString("nome"),
                                rs.getString("civico"),
                                rs.getString("comune"),
                                rs.getString("provincia"),
                                rs.getInt("cap")),
                        TipologiaCentro.valueOf(rs.getString("tipologia"))));
            }
            return arrayNomeCentri;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
