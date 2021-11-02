package it.uninsubria.centrivaccinali.database;

import it.uninsubria.centrivaccinali.enumerator.Qualificatore;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.*;

import java.nio.channels.ScatteringByteChannel;
import java.util.regex.Pattern;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *  TODO controllo sicurezza delle query
 */
public class Database {

    private final String utente = "123abc";
    private final String password = "123abc";
    private static Connection conn;
    private static PreparedStatement pstmt;
    private static Statement stmt;
    private ResultSet rs;
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
     * extendedResult ritorna 1 se l'userID esiste - 0 se l'userID non esiste
     */
    public Result registraCentroVaccinale(CentroVaccinale cv) {
        UUID uuid = null;
        risultato = new Result(false, Result.Operation.REGISTRAZIONE_CENTRO);

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
            rs = pstmt.executeQuery();
            if(rs.next()) {
                uuid = rs.getObject("id_indirizzo", java.util.UUID.class);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                rs = pstmt.executeQuery();
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
                // Nome centro gia' inserito
                System.err.println(e.getMessage());
                if(((e.getMessage().split(Pattern.quote(")")))[0].split(Pattern.quote("("))[1]).equals("nome")) {
                    risultato.setExtendedResult(Result.Error.NOME_IN_USO);
                }
                return risultato;

                /*
                 * Dettaglio: Key (nome)=(CENTRO VACCINAZIONI VARESE SCHIRANNA) already exists. // PRIMO SPLIT
                 *
                 * ["Dettaglio: Key (nome", "=(CENTRO VACCINAZIONI VARESE SCHIRANNA)", " already exists."] // Prendo l'indice 0
                 *
                 * Dettaglio: Key (nome // Secondo SPLIT
                 *
                 * ["Dettaglio: Key (", "nome"] // Prendo l'indice 1
                 */
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
                    "id_vaccinazione numeric(16) NOT NULL PRIMARY KEY);");
            risultato.setResult(true);
            return risultato;
        } catch (SQLException e) {
            e.printStackTrace();
            return risultato;
        }
    }

    public Result loginUtente(String username, String password) {
        risultato = new Result(false, Result.Operation.LOGIN_CITTADINO);
        try {
            pstmt = conn.prepareStatement("SELECT *" +
                                              "FROM public.\"Cittadini_Registrati\"" +
                                              "WHERE userid = ? AND password = ?");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
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
                risultato.setResult(true);
                risultato.setCittadino(c);
            } else {
                pstmt = conn.prepareStatement("SELECT COUNT(*)" +
                                                 "FROM public.\"Cittadini_Registrati\"" +
                                                 "WHERE userid = ?");
                pstmt.setString(1, username);
                rs = pstmt.executeQuery();
                rs.next();
                if(rs.getInt(1) == 0) {
                    risultato.setExtendedResult(Result.Error.USERNAME_NON_TROVATO);
                } else {
                    risultato.setExtendedResult(Result.Error.PASSWORD_ERRATA);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultato;
    }

    public Result registraCittadino(Cittadino c) {
        Result risultato = new Result(false, Result.Operation.REGISTRAZIONE_CITTADINO);
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                                        "FROM tabelle_cv.\"vaccinati\" " +
                                        "WHERE id_vaccinazione = ? AND codice_fiscale = ?");
            pstmt.setLong(1, c.getId_vaccino());
            pstmt.setString(2, c.getCodice_fiscale());
            rs = pstmt.executeQuery();
            //vaccinato e' presente nel db dei centri vaccinali
            //posso registrare il cittadino correttamente
            if (rs.next()) {
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
            } else {
                System.err.println("[Database] id vaccinazione o codice fiscale non valido");
                risultato.setExtendedResult(Result.Error.CF_ID_NON_VALIDI);
            }
            return risultato;
        } catch (SQLException e) {
            e.printStackTrace();
            String colonna = ((e.getMessage().split(Pattern.quote(")")))[0].split(Pattern.quote("("))[1]);
            try{
                switch (colonna) {
                    case "codice_fiscale":
                        risultato.setExtendedResult(Result.Error.CITTADINO_GIA_REGISTRATO);
                    case "email":
                        pstmt = conn.prepareStatement("SELECT COUNT(*) " +
                                "FROM public.\"Cittadini_Registrati\"" +
                                "WHERE email = ?");
                        pstmt.setString(1, c.getEmail());
                        rs = pstmt.executeQuery();
                        rs.next();
                        if(rs.getInt(1) == 1) {
                            risultato.setExtendedResult(Result.Error.EMAIL_GIA_IN_USO);
                        }
                    case "userid":
                        pstmt = conn.prepareStatement("SELECT COUNT(*) " +
                                "FROM public.\"Cittadini_Registrati\"" +
                                "WHERE userid = ?");
                        pstmt.setString(1, c.getUserid());
                        rs = pstmt.executeQuery();
                        rs.next();
                        if(rs.getInt(1) == 1) {
                            risultato.setExtendedResult(Result.Error.USERID_GIA_IN_USO);
                        }
                    case "id_vaccino":
                        pstmt = conn.prepareStatement("SELECT COUNT(*) " +
                                "FROM public.\"Cittadini_Registrati\"" +
                                "WHERE id_vaccino = ?");
                        pstmt.setLong(1, c.getId_vaccino());
                        rs = pstmt.executeQuery();
                        rs.next();
                        if(rs.getInt(1) == 1) {
                            risultato.setExtendedResult(Result.Error.CITTADINO_GIA_REGISTRATO);
                        }
                        break;
                }
            } catch(SQLException e1) {
                e1.printStackTrace();
            }
        }
        return risultato;
    }

    public Result registraVaccinato(Vaccinato nuovoVaccinato) {
        Result risultato = new Result(false, Result.Operation.REGISTRAZIONE_VACCINATO);
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
            String colonna = ((e.getMessage().split(Pattern.quote(")")))[0].split(Pattern.quote("("))[1]);
            if (colonna.equals("codice_fiscale")) {
                System.err.println("Codice fiscale gia' associato ad un vaccinato");
                risultato.setExtendedResult(Result.Error.CF_GIA_IN_USO);
            } else if(colonna.equals("id_vaccinazione")) {
                System.err.println("Id vaccinazione gia' associato ad un vaccinato");
                risultato.setExtendedResult(Result.Error.IDVAC_GIA_IN_USO);
            }
        }
        return risultato;
    }

    /**
     * FUNZIONANTE
     */
    public Result getComuni(String provincia){
        Result risultato = new Result(false, Result.Operation.RISULTATO_COMUNI);
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
        Result risultato = new Result(false, Result.Operation.RISULTATO_CENTRI);
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

    public Result ricercaCentroPerNome(String nomeCentro) {
        Result risultato = new Result(false, Result.Operation.RICERCA_CENTRO);
        List<CentroVaccinale> risultatoRicerca = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM public.\"InfoCV\" " +
                    "WHERE nome_centro LIKE ? " +
                    "ORDER BY nome_centro");
            pstmt.setString(1, "%" + nomeCentro + "%");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                risultatoRicerca.add(new CentroVaccinale(rs.getString("nome_centro"),
                        new Indirizzo(Qualificatore.valueOf(rs.getString("qualificatore")),
                                rs.getString("nome"),
                                rs.getString("civico"),
                                rs.getString("comune"),
                                rs.getString("provincia"),
                                rs.getInt("cap")),
                        TipologiaCentro.valueOf(rs.getString("tipologia"))));
            }
            risultato.setResult(true);
            risultato.setResultCentri(risultatoRicerca);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultato;
    }

    public Result ricercaCentroPerComuneTipologia(String comune, TipologiaCentro tipologia) {
        Result risultato = new Result(false, Result.Operation.RICERCA_CENTRO);
        List<CentroVaccinale> risultatoRicerca = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM public.\"InfoCV\" " +
                    "WHERE comune ILIKE ? AND " +
                    "tipologia = ?" +
                    "ORDER BY nome_centro");
            pstmt.setString(1, "%" + comune + "%");
            pstmt.setString(2, tipologia.toString());
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                risultatoRicerca.add(new CentroVaccinale(rs.getString("nome_centro"),
                        new Indirizzo(Qualificatore.valueOf(rs.getString("qualificatore")),
                                rs.getString("nome"),
                                rs.getString("civico"),
                                rs.getString("comune"),
                                rs.getString("provincia"),
                                rs.getInt("cap")),
                        TipologiaCentro.valueOf(rs.getString("tipologia"))));
            }
            risultato.setResult(true);
            risultato.setResultCentri(risultatoRicerca);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultato;
    }

    public Result registraEA(EventoAvverso ea, long idVac) {
        Result risultato = new Result(false, Result.Operation.REGISTRA_EVENTO_AVVERSO);
        try {
            pstmt = conn.prepareStatement("INSERT INTO public.\"EventiAvversi\" " +
                                              "VALUES (?, ?, ?, ?)");
            pstmt.setLong(1, idVac);
            pstmt.setString(2, ea.getEvento());
            pstmt.setInt(3, ea.getSeverita());
            pstmt.setString(4, ea.getNote());
            risultato.setResult(true);
        } catch (SQLException e) {
            risultato.setExtendedResult(Result.Error.EVENTO_GIA_INSERITO);
            e.printStackTrace();
        }
        return risultato;
    }
}
