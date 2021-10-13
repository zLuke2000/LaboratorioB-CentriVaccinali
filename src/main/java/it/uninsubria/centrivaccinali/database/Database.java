package it.uninsubria.centrivaccinali.database;

import it.uninsubria.centrivaccinali.enumerator.Qualificatore;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *  TODO controllo sicurezza delle query
 */
public class Database {

    /**
     * LEGENDA
     * -1 ATTESA
     *  0 Programma OK
     *  1 Eccezione
     */
    public static final int ATTESA = -1;
    public static final int OK = 0;
    public static final int EXCEPTION = 1;

    private final String utente = "123abc";
    private final String password = "123abc";
    private static Connection conn;
    private static PreparedStatement pstmt;
    private static Statement stmt;
    private Result risultato;

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

    /**
     * FUNZIONANTE
     */
    public Result registraCentroVaccinale(CentroVaccinale cv) {
        int result = ATTESA;
        UUID uuid = null;
        risultato = new Result(false, Result.REGISTRAZIONE_CENTRO);

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
            //TODO settare eccezzione
            return risultato;
        }

        // Inserisco il nuovo indirizzo solo se non presente
        if(uuid == null) {
            // Inserisco il nuovo indirizzo
            try {
                pstmt = conn.prepareStatement("INSERT INTO public.\"IndirizzoCV\" VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)");
                pstmt.setString(1, cv.getIndirizzo().getQualificatore().toString());
                pstmt.setString(2, cv.getIndirizzo().getNome());
                pstmt.setString(3, cv.getIndirizzo().getCivico());
                pstmt.setString(4, cv.getIndirizzo().getComune());
                pstmt.setString(5, cv.getIndirizzo().getProvincia());
                pstmt.setInt(6, cv.getIndirizzo().getCap());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                //TODO mostrare al client il tipo di eccezione
                return risultato;
            }

            // Recupero l'id univoco dell'indirizzo appena registrato
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
                rs.next();
                uuid = rs.getObject("id_indirizzo", java.util.UUID.class);
            } catch (SQLException e) {
                e.printStackTrace();
                return risultato;
            }
        }

        // Scrivo il centro vaccinale
        if(uuid != null) {
            try {
                pstmt = conn.prepareStatement("INSERT INTO public.\"CentriVaccinali\" (nome, indirizzo, tipologia) "
                                                + "VALUES (?, ?, ?)");
                pstmt.setString(1, cv.getNome());
                pstmt.setObject(2, uuid);
                pstmt.setString(3, cv.getTipologia().toString());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                // TODO gestire tabella inserita con lo stesso nome
                e.printStackTrace();
                return risultato;
            }
        }

        // Creo la nuova tabella
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tabelle_cv.Vaccinati_" + cv.getNome().replaceAll(" ", "_") + " ( " +
                    "nome_centro varchar(50) NOT NULL, " +
                    "nome varchar(50) NOT NULL, " +
                    "cognome varchar(50) NOT NULL, " +
                    "codice_fiscale varchar(16) NOT NULL UNIQUE, " +
                    "data_somministrazione date NOT NULL, " +
                    "vaccino varchar(16) NOT NULL, " +
                    "id_vaccinazione bigint NOT NULL PRIMARY KEY);");
            risultato.setResult(true);
            return risultato;
        } catch (SQLException e) {
            e.printStackTrace();
            return risultato;
        }
    }

    public Result loginUtente(String username, String password) {
        risultato = new Result(false, Result.LOGIN_UTENTE);
        try {
            pstmt = conn.prepareStatement("SELECT *" +
                                              "FROM public.\"Cittadini_Registrati\"" +
                                              "WHERE userid = ? AND password = ?");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                Cittadino c = new Cittadino(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("codice_fiscale"),
                        rs.getString("email"),
                        rs.getString("userid"),
                        rs.getString("password"),
                        rs.getLong("id_vaccino")
                );
                System.out.println("[Database] login effettuato: " + rs.getString("userid"));
                risultato.setResult(true);
                risultato.setCittadino(c);
            } else {
                System.err.println("[Database] login fallito");
                //TODO mostrare errori
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultato;
    }

    public Result registraCittadino(Cittadino c) {
        Result risultato = new Result(false, Result.REGISTRAZIONE_CITTADINO);
        try {
            pstmt = conn.prepareStatement("SELECT *" +
                                        "FROM tabelle_cv.\"vaccinati\"" +
                                        "WHERE id_vaccinazione = ? AND codice_fiscale = ?");
            pstmt.setLong(1, c.getId_vaccino());
            pstmt.setString(2, c.getCodice_fiscale());
            ResultSet rs = pstmt.executeQuery();
            //id vaccinazione e' presente nel db dei centri vaccinali
            //posso registrare il cittadino correttamente
            if (rs.next()){
                pstmt = conn.prepareStatement("INSERT INTO public.\"Cittadini_Registrati\" VALUES (?, ?, ?, ?, ?, ?, ?)");
                pstmt.setString(1, c.getNome());
                pstmt.setString(2, c.getCognome());
                pstmt.setString(3, c.getCodice_fiscale());
                pstmt.setString(4, c.getEmail());
                pstmt.setString(5, c.getUserid());
                pstmt.setString(6, c.getPassword());
                pstmt.setLong(7, c.getId_vaccino());
                pstmt.executeUpdate();
                System.out.println("[Database] registrato nuovo cittadino");
                risultato.setResult(true);
                risultato.setCittadino(c);
                return risultato;
            } else {
                System.err.println("[Database] id vaccinazione o codice fiscale non valido");
                //TODO mostra errore lato client
                //return
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO separare gli errori (CF, Email, userID, IDvac) gia' in uso
        }
        return risultato;
    }

    public Result registraVaccinato(Vaccinato nuovoVaccinato) {
        Result risultato = new Result(false, Result.REGISTRAZIONE_VACCINATO);
        try {
            pstmt = conn.prepareStatement("INSERT INTO tabelle_cv.\"vaccinati_" + nuovoVaccinato.getNomeCentro().replaceAll(" ", "_") + "\" VALUES (?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, nuovoVaccinato.getNomeCentro());
            pstmt.setString(2, nuovoVaccinato.getNome());
            pstmt.setString(3, nuovoVaccinato.getCognome());
            pstmt.setString(4, nuovoVaccinato.getCodiceFiscale());
            pstmt.setDate(5, nuovoVaccinato.getDataSomministrazione());
            pstmt.setString(6, String.valueOf(nuovoVaccinato.getVaccinoSomministrato()));
            pstmt.setLong(7, nuovoVaccinato.getIdVaccino());
            pstmt.executeUpdate();
            //inserisci nuovo vaccinato nella tabella associativa
            pstmt=conn.prepareStatement("INSERT INTO tabelle_cv.\"vaccinati\" VALUES (?, ?, ?)");
            pstmt.setLong(1, nuovoVaccinato.getIdVaccino());
            pstmt.setString(2, nuovoVaccinato.getCodiceFiscale());
            pstmt.setString(3, nuovoVaccinato.getNomeCentro());
            pstmt.executeUpdate();
            System.out.println("[Database] registrato nuovo vaccinato");
            risultato.setResult(true);
            return risultato;
        } catch (SQLException e) {
            e.printStackTrace();
            //ritorna errore
        }
        return risultato;
    }

    /**
     * FUNZIONANTE
     */
    public Result getComuni(String provincia){
        Result risultato = new Result(false, Result.RISULTATO_COMUNI);
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
            risultato.setResult(true);
            risultato.setResultComuni(arrayComuni);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultato;
    }

    /**
     * FUNZIONANTE
     */
    public Result getCentriVaccinali(String comune){
        Result risultato = new Result(false, Result.RISULTATO_CENTRI);
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
            risultato.setResult(true);
            risultato.setResultCentri(arrayNomeCentri);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultato;
    }
}
