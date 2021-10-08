package it.uninsubria.centrivaccinali.database;

import it.uninsubria.centrivaccinali.client.ClientCVInterface;
import it.uninsubria.centrivaccinali.enumerator.Qualificatore;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Indirizzo;
import it.uninsubria.centrivaccinali.models.Vaccinato;

import java.rmi.RemoteException;
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
     *  2 Indirizzo non trovato
     */
    public static final int ATTESA = -1;
    public static final int OK = 0;
    public static final int EXCEPTION = 1;
    public static final int INDIRIZZO_NON_TROVATO = 2;
    public static final int TABELLA_NON_CREATA = 3;

    private final String utente = "123abc";
    private final String password = "123abc";
    private static Connection conn;
    private static PreparedStatement pstmt;
    private static Statement stmt;

    public Database() {}

    /**
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

    /**
     * FUNZIONANTE
     * @param cv
     * @return
     */
    public int registraCentroVaccinale(CentroVaccinale cv) {
        int result = ATTESA;
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
            return EXCEPTION;
        }

        // Inserisco il nuovo centro solo se non presente
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
                result = pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return EXCEPTION;
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
                        return INDIRIZZO_NON_TROVATO;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return EXCEPTION;
                }
            }
        }

        // Scrivo il centro vaccinale
        if(uuid != null) {
            result = ATTESA;
            try {
                pstmt = conn.prepareStatement("INSERT INTO public.\"CentriVaccinali\" (nome, indirizzo, tipologia) "
                                                + "VALUES (?, ?, ?)");
                pstmt.setString(1, cv.getNome());
                pstmt.setObject(2, uuid);
                pstmt.setString(3, cv.getTipologia().toString());
                result = pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return EXCEPTION;
            }
        }

        // Creo la nuova tabella
        if(result == 1) {
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
                return OK;
            } catch (SQLException e) {
                e.printStackTrace();
                return EXCEPTION;
            }
        } else {
            return TABELLA_NON_CREATA;
        }
    }

    public int loginUtente(ClientCVInterface client, String username, String password) {
        try {
            pstmt = conn.prepareStatement("SELECT *" +
                                              "FROM public.\"Cittadini_Registrati\"" +
                                              "WHERE userid = ? AND password = ?");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                //TODO recupera info cittadino e notifica client
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
                client.notifyLogin(true, c);
            } else {
                System.err.println("[Database] login fallito");
                client.notifyLogin(false, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                client.notifyLogin(false, null);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        } catch (RemoteException e) {
            System.err.println("[Database] errore di comunicazione con il client");
            e.printStackTrace();
        }
        return -1;
    }

    public int registraCittadino(Cittadino c) {
        //TODO controllo su nome e cognome???
        try {
            System.out.println("[Database] controllo che id vaccinazione sia corretto");
            pstmt=conn.prepareStatement("SELECT *" +
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
                return OK;
            } else {
                System.err.println("[Database] id vaccinazione o codice fiscale non valido");
                //TODO mostra errore lato client
                return EXCEPTION;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return EXCEPTION;
        }
    }

    public int registraVaccinato(Vaccinato nuovoVaccinato) {
        int risultatoQuery=-1;
        try {
            pstmt = conn.prepareStatement("INSERT INTO tabelle_cv.\"vaccinati_" + nuovoVaccinato.getNomeCentro().replaceAll(" ", "_") + "\" VALUES (?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, nuovoVaccinato.getNomeCentro());
            pstmt.setString(2, nuovoVaccinato.getNome());
            pstmt.setString(3, nuovoVaccinato.getCognome());
            pstmt.setString(4, nuovoVaccinato.getCodiceFiscale());
            pstmt.setDate(5, nuovoVaccinato.getDataSomministrazione());
            pstmt.setString(6, String.valueOf(nuovoVaccinato.getVaccinoSomministrato()));
            pstmt.setLong(7, nuovoVaccinato.getIdVaccino());
            risultatoQuery=pstmt.executeUpdate();
            System.out.println("[Database] registrato nuovo vaccinato");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                pstmt=conn.prepareStatement("INSERT INTO tabelle_cv.\"vaccinati\" VALUES (?, ?, ?)");
                pstmt.setLong(1, nuovoVaccinato.getIdVaccino());
                pstmt.setString(2, nuovoVaccinato.getCodiceFiscale());
                pstmt.setString(3, nuovoVaccinato.getNomeCentro());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("[Db thread] registrato vaccinato");
        }).start();
        //return -1;
        return risultatoQuery;
        //TODO sistemare i punti di return
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
