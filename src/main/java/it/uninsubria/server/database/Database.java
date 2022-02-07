//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.server.database;

import it.uninsubria.centrivaccinali.enumerator.Qualificatore;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.enumerator.Vaccino;
import it.uninsubria.centrivaccinali.models.*;

import java.util.*;
import java.util.regex.Pattern;
import java.sql.*;

/**
 * Classe che permette di eseguire le query su database.
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele 742495
 * @author Pintonello Christian 741112
 */
public class Database {

    /**
     * Riferimento alla connessione tra il programma java e il database.
     * @see Connection
     */
    private static Connection conn;


    /**
     * Variabile contenente uno statement sql preparato prima di eseguire la query.
     * @see PreparedStatement
     */
    private static PreparedStatement pstmt;


    /**
     * Variabile contenete il risultato dopo una query a database.
     * @see ResultSet
     */
    private ResultSet rs;


    /**
     * Rappresenta l'operazione da eseguire.
     * @see Result
     */
    private Result risultato;


    /**
     * Costruttore vuoto per <code>Database</code>.
     */
    public Database() { }


    /**
     * Permette di ottenere la connessione al database.
     * @return un booleano che rappresenta lo stato della connessione.
     * @param host indirizzo del database
     * @param user nome del profilo del database
     * @param password password del profilo del database
     * @see DBHelper
     */
    public boolean connettiDB(String host, String user, String password) {
        conn = DBHelper.getConnection(host, user, password);
        System.out.println("Connessione stabilita: " + conn);
        return conn != null;
    }


    /**
     * Effettua l'inserimento di un centro vaccinale su database seguendo determinati passaggi:<br>
     *  - Controlla che l'indirizzo del centro sia gi&amp;agrave nella tabella dedicata<br>
     *  - Registra l'indirizzo se esito negativo dell'azione precedente<br>
     *  - Registra il centro vaccinale passato come paramento<br>
     *  - Crea la tabella dedicata nel database (se inesistente)
     * @param cv centro vaccinale da registrare.
     * @return un oggetto di tipo <code>Result</code> contenete l'esito dell'operazione.
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

            }
        }

        // Creo la nuova tabella
        try {
            Statement stmt = conn.createStatement();
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


    /**
     * Metodo per effettuare la login del cittadino nell'applicazione.
     * @param username nome utente inserita dal cittadino.
     * @param password password inserita dal cittadino.
     * @return un oggetto di tipo <code>Result</code> contenete l'esito dell'operazione.
     */
    public Result loginUtente(String username, String password) {
        risultato = new Result(false, Result.Operation.LOGIN_CITTADINO);
        //Verifico corrispondenza userid e password
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
                        rs.getLong("id_vaccinazione")
                );
                risultato.setResult(true);
                risultato.setCittadino(c);

                // Ottengo nome_centro del cittadino
                pstmt = conn.prepareStatement("SELECT nome_centro " +
                                                  "FROM tabelle_cv.\"vaccinati\" " +
                                                  "WHERE id_vaccinazione = ? AND codice_fiscale = ?");
                pstmt.setLong(1, c.getId_vaccinazione());
                pstmt.setString(2, c.getCodice_fiscale());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    risultato.setCentroCittadino(rs.getString("nome_centro"));
                } else {
                    throw new SQLException();
                }
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


    /**
     * Effettua prima una query per verificare se l'utente è già registrato, in caso negativo viene effettuata la query di
     * inserimento del cittadino nel database. In caso in cui l'utente risulti essere già registrato verrà restituito
     * un messaggio di errore in base al campo/i già presenti.
     * @param c riferimento al cittadino da registrare
     * @return un oggetto di tipo <code>Result</code> contenete l'esito dell'operazione.
     */
    public Result registraCittadino(Cittadino c) {
        Result risultato = new Result(false, Result.Operation.REGISTRAZIONE_CITTADINO);
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                                        "FROM tabelle_cv.\"vaccinati\" " +
                                        "WHERE id_vaccinazione = ? AND codice_fiscale = ?");
            pstmt.setLong(1, c.getId_vaccinazione());
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
                pstmt.setLong(7, c.getId_vaccinazione());
                pstmt.executeUpdate();
                System.out.println("[Database] registrato nuovo cittadino");
                risultato.setResult(true);
                risultato.setCittadino(c);
            } else {
                System.err.println("[Database] id vaccinazione o codice fiscale non valido: " + c.getId_vaccinazione() + "  ->  " + c.getCodice_fiscale());
                risultato.setExtendedResult(Result.Error.CF_ID_NON_VALIDI);
            }
            return risultato;
        } catch (SQLException e) {
            String colonna = ((e.getMessage().split(Pattern.quote(")")))[0].split(Pattern.quote("("))[1]);
            try{
                switch (colonna) {
                    case "codice_fiscale":
                        risultato.setExtendedResult(Result.Error.CITTADINO_GIA_REGISTRATO);
                        System.err.println("CITTADINO GIA REGISTRATO");
                    case "email":
                        pstmt = conn.prepareStatement("SELECT COUNT(*) " +
                                "FROM public.\"Cittadini_Registrati\"" +
                                "WHERE email = ?");
                        pstmt.setString(1, c.getEmail());
                        rs = pstmt.executeQuery();
                        rs.next();
                        if(rs.getInt(1) == 1) {
                            risultato.setExtendedResult(Result.Error.EMAIL_GIA_IN_USO);
                            System.err.println("EMAIL GIA' REGISTRATA");
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
                            System.err.println("USERID GIA' REGISTRATO");
                        }
                    case "id_vaccinazione":
                        pstmt = conn.prepareStatement("SELECT COUNT(*) " +
                                "FROM public.\"Cittadini_Registrati\"" +
                                "WHERE id_vaccinazione = ?");
                        pstmt.setLong(1, c.getId_vaccinazione());
                        rs = pstmt.executeQuery();
                        rs.next();
                        if(rs.getInt(1) == 1) {
                            risultato.setExtendedResult(Result.Error.CITTADINO_GIA_REGISTRATO);
                            System.err.println("CITTADINO GIA' REGISTRATO");
                        }
                        break;
                }
            } catch(SQLException e1) {
                e1.printStackTrace();
            }
        }
        return risultato;
    }


    /**
     * Effettua la registrazione di un cittadino vaccinato tramite una query di insert sul database. In caso di errori
     * di inserimento per chiavi già presenti nel database, verrà lanciata un'eccezione.
     * @param nuovoVaccinato oggetto contenente tutti i dati del cittadino da registrare.
     * @return un oggetto di tipo <code>Result</code> contenete l'esito dell'operazione.
     */
    public Result registraVaccinato(Vaccinato nuovoVaccinato) {
        Result risultato = new Result(false, Result.Operation.REGISTRAZIONE_VACCINATO);
        try {
            pstmt = conn.prepareStatement("INSERT INTO tabelle_cv.\"vaccinati_" + nuovoVaccinato.getNomeCentro().toLowerCase(Locale.ROOT).replaceAll(" ", "_") + "\" VALUES (?, ?, ?, ?, ?, ?, ?)");
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
     * Effettua una query sul database per richiedere la lista dei comuni sulla base della provincia passata come parametro.
     * @param provincia provincia utilizzata ai fini della ricerca dei comuni.
     * @return un oggetto di tipo <code>Result</code> contenete l'esito dell'operazione.
     */
    public Result getComuni(String provincia){
        Result risultato = new Result(false, Result.Operation.RICERCA_COMUNI);
        List<Object> arrayComuni = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("SELECT DISTINCT comune " +
                                              "FROM public.\"IndirizzoCV\" " +
                                              "WHERE provincia = ? " +
                                              "ORDER BY comune");
            pstmt.setString(1, provincia);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                arrayComuni.add(rs.getString("comune"));
            }
            risultato.setResult(true);
            risultato.setList(arrayComuni);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultato;
    }


    /**
     * Effettua una query sul database per richiedere la lista dei centri vaccinali sulla base del comune passato come
     * parametro.
     * @param comune comune utilizzato ai fini della ricerca dei centri vaccinali.
     * @return un oggetto di tipo <code>Result</code> contenete l'esito dell'operazione.
     */
    public Result getCentriVaccinali(String comune){
        Result risultato = new Result(false, Result.Operation.RICERCA_CENTRI);
        List<Object> listaCentri = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM public.\"InfoCV\" " +
                    "WHERE comune = ? " +
                    "ORDER BY nome_centro");
            pstmt.setString(1, comune);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                listaCentri.add(new CentroVaccinale(rs.getString("nome_centro"),
                        new Indirizzo(Qualificatore.valueOf(rs.getString("qualificatore")),
                                rs.getString("nome"),
                                rs.getString("civico"),
                                rs.getString("comune"),
                                rs.getString("provincia"),
                                rs.getInt("cap")),
                        TipologiaCentro.getValue(rs.getString("tipologia"))));
            }
            risultato.setResult(true);
            risultato.setList(listaCentri);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultato;
    }


    /**
     * Effettua una query per ottenere la lista dei centri vaccinali che corrispondono interamente o in parte al nome del
     * centro passato come parametro.
     * @param nomeCentro utilizzato ai fini della ricerca dei centri vaccinali.
     * @return un oggetto di tipo <code>Result</code> contenete l'esito dell'operazione.
     */
    public Result ricercaCentroPerNome(String nomeCentro) {
        Result risultato = new Result(false, Result.Operation.RICERCA_CENTRO);
        List<Object> listaCentri = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM public.\"InfoCV\" " +
                    "WHERE nome_centro ILIKE ? " +
                    "ORDER BY nome_centro");
            pstmt.setString(1, "%" + nomeCentro + "%");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                listaCentri.add(new CentroVaccinale(rs.getString("nome_centro"),
                        new Indirizzo(Qualificatore.valueOf(rs.getString("qualificatore")),
                                rs.getString("nome"),
                                rs.getString("civico"),
                                rs.getString("comune"),
                                rs.getString("provincia"),
                                rs.getInt("cap")),
                        TipologiaCentro.getValue(rs.getString("tipologia"))));
            }
            risultato.setResult(true);
            risultato.setList(listaCentri);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultato;
    }


    /**
     * Effettua una query per ottenere la lista dei centri vaccinali sulla base del comune e della tipologia passati come
     * parametro.
     * @param comune utilizzato ai fini della ricerca dei centri vaccinali.
     * @param tipologia utilizzata ai fini della ricerca dei centri vaccinali.
     * @return un oggetto di tipo <code>Result</code> contenete l'esito dell'operazione.
     */
    public Result ricercaCentroPerComuneTipologia(String comune, TipologiaCentro tipologia) {
        Result risultato = new Result(false, Result.Operation.RICERCA_CENTRO);
        List<Object> listaCentri = new ArrayList<>();
        try {
            pstmt = conn.prepareStatement("SELECT * " +
                    "FROM public.\"InfoCV\" " +
                    "WHERE comune ILIKE ? AND " +
                    "tipologia = ?" +
                    "ORDER BY nome_centro");
            pstmt.setString(1, "%" + comune + "%");
            pstmt.setString(2, tipologia.toString());
            rs = pstmt.executeQuery();
            while(rs.next()) {
                listaCentri.add(new CentroVaccinale(rs.getString("nome_centro"),
                        new Indirizzo(Qualificatore.valueOf(rs.getString("qualificatore")),
                                rs.getString("nome"),
                                rs.getString("civico"),
                                rs.getString("comune"),
                                rs.getString("provincia"),
                                rs.getInt("cap")),
                        TipologiaCentro.getValue(rs.getString("tipologia"))));
            }
            risultato.setResult(true);
            risultato.setList(listaCentri);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultato;
    }


    /**
     * Effettua una query di inserimento sul database dell'evento evverso passato come parametro.
     * @param ea oggetto contenente tutti i dati relativi all'evento avverso.
     * @return un oggetto di tipo <code>Result</code> contenete l'esito dell'operazione.
     */
    public Result registraEA(EventoAvverso ea) {
        Result risultato = new Result(false, Result.Operation.REGISTRA_EVENTO_AVVERSO);
        try {
            pstmt = conn.prepareStatement("INSERT INTO public.\"EventiAvversi\" " +
                                              "VALUES (?, ?, ?, ?)");
            pstmt.setLong(1, ea.getIdVaccino());
            pstmt.setString(2, ea.getTipologiaEvento());
            pstmt.setInt(3, ea.getSeverita());
            pstmt.setString(4, ea.getNote());
            pstmt.executeUpdate();
            risultato.setResult(true);
        } catch (SQLException e) {
            risultato.setExtendedResult(Result.Error.EVENTO_GIA_INSERITO);
            e.printStackTrace();
        }
        return risultato;
    }


    /**
     * Effettua una query a database per ottenere una lista contenente la media di severit&amp;agrave per ogni tipo di evento
     * avverso, sulla base del centro vaccinale passato come parametro.
     * @param nomeCentro nome del centro da cui recuperare le segnalazioni
     * @return un oggetto di tipo <code>Result</code> contenete l'esito dell'operazione.
     */
    public Result leggiMediaEventiAvversi(String nomeCentro) {
        Result risultato = new Result(false, Result.Operation.LEGGI_EVENTI_AVVERSI);
        try {
            String centro = (nomeCentro.toLowerCase()).replaceAll("\\s", "_");
            List<String> eventi = new ArrayList<>();
            eventi.add("mal di testa");
            eventi.add("febbre");
            eventi.add("dolori muscolari e articolari");
            eventi.add("linfoadenopatia");
            eventi.add("tachicardia");
            eventi.add("crisi chipertensiva");

            pstmt = conn.prepareStatement("SELECT vaccino, evento, AVG(severita) " +
                                              "FROM public.\"EventiAvversi\" JOIN tabelle_cv.\"vaccinati_" + centro + "\" USING (id_vaccinazione) " +
                                              "WHERE evento IN (?, ?, ?, ?, ?, ?) " +
                                              "GROUP BY vaccino, evento " +
                                              "ORDER BY vaccino");
            for (int i = 0; i < eventi.size(); i++)
                pstmt.setString(i + 1, eventi.get(i));
            rs = pstmt.executeQuery();
            Map<String, Double> map = new HashMap<>();
            while(rs.next()) {
                map.put(rs.getString("vaccino") + "/" + rs.getString("evento"), rs.getDouble("avg"));
            }

            pstmt = conn.prepareStatement("SELECT vaccino, AVG(severita) " +
                    "FROM public.\"EventiAvversi\" JOIN tabelle_cv.\"vaccinati_" + centro + "\" USING (id_vaccinazione) " +
                    "WHERE evento NOT IN (?, ?, ?, ?, ?, ?) " +
                    "GROUP BY vaccino " +
                    "ORDER BY vaccino");
            for (int i = 0; i < eventi.size(); i++)
                pstmt.setString(i + 1, eventi.get(i));
            rs = pstmt.executeQuery();
            while(rs.next()) {
                map.put(rs.getString("vaccino") + "/altro", rs.getDouble("avg"));
            }
            risultato.setResult(true);
            risultato.setMap(map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultato;
    }


    /**
     * Effettua la lettura delle segnalazioni di eventi avversi di un determinato centro secondo due criteri passati
     * come parametri.
     * @param nomeCentro nome del centro da cui recuperare le segnalazioni
     * @param limit limite massimo di segnalazione acquisite dal database
     * @param offset offset di selezione delle tuple da database partendo dall'alto
     * @return un oggetto di tipo <code>Result</code> contenete l'esito dell'operazione.
     */
    public Result leggiSegnalazioni(String nomeCentro, int limit, int offset) {
        Result risultato = new Result(false, Result.Operation.LEGGI_EVENTI_AVVERSI);
        try {
            String centro = (nomeCentro.toLowerCase()).replaceAll("\\s", "_");

            pstmt = conn.prepareStatement("SELECT vaccino, evento, severita, note " +
                    "FROM public.\"EventiAvversi\" NATURAL JOIN tabelle_cv.\"vaccinati_" + centro + "\" " +
                    "limit ? " +
                    "offset ?");
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            rs = pstmt.executeQuery();
            List<Object> listEventi = new ArrayList<>();
            while(rs.next()) {
                String tipoEvento = rs.getString("evento");
                int severita = rs.getInt("severita");
                String note = rs.getString("note");
                Vaccino vaccino = Vaccino.getValue(rs.getString("vaccino"));
                EventoAvverso ea = new EventoAvverso(tipoEvento, severita, note, vaccino);
                listEventi.add(ea);
            }
            risultato.setResult(true);
            risultato.setList(listEventi);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultato;
    }


    /**
     * Effettua l'aggiornamento della password dell'account di un cittadino tramite una query di update sul database.
     * @param userid nome utente del cittadino
     * @param vecchiaPassword password vecchia
     * @param nuovaPassword password nuova
     * @return un oggetto di tipo <code>Result</code> contenete l'esito dell'operazione.
     */
    public Result aggiornaPSW(String userid, String vecchiaPassword, String nuovaPassword) {
        Result risultato = new Result(false, Result.Operation.AGGIORNA_PASSWORD_CITTADINO);
        try {
            pstmt = conn.prepareStatement("UPDATE public.\"Cittadini_Registrati\" " +
                                              "SET password = ? " +
                                              "WHERE userid = ? AND password = ?");
            pstmt.setString(1, nuovaPassword);
            pstmt.setString(2, userid);
            pstmt.setString(3, vecchiaPassword);
            if(pstmt.executeUpdate() == 1) {
                risultato.setResult(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultato;
    }
}
