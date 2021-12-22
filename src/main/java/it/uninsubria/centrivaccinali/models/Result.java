package it.uninsubria.centrivaccinali.models;

import java.io.Serializable;
import java.util.*;

/**
 * Rappresenta l'esito delle operazioni effettuate sul database
 * @author ...
 */
public class Result implements Serializable {


    /**
     * Varabile per identificare serial version RMI.
     */
    private static final long serialVersionUID = 1L;


    /**
     * Lista contenente tutte le informazioni riguardanti l'esito delle operazioni
     */
    private final List<Error> extendedResult = new ArrayList<>();


    /**
     * Lista contenente gli oggetti da inviare al client
     */
    private List<Object> list;


    /**
     * Mappa da inviare al client che conterr&amp;agrave gli eventi avversi e la loro severit&amp;agrave
     */
    private Map<String, Double> map;


    /**
     * Rappresenta il tipo di operazione da effettuare
     */
    private Operation opType;


    /**
     * Rappresenta l'esito dell'operazione
     */
    private boolean result;


    /**
     * Rappresenta il cittadino che effettuer&amp;agrave il login
     */
    private Cittadino cittadino;


    /**
     * Rappresenta il centro presso cui &amp;egrave stato vaccinato il cittadino loggato
     */
    private String centroCittadino;


    /**
     * Costruttore per la classe <code>Result</code>
     * @param result l'esito dell'operazione
     * @param opType il tipo dell'operazione da effuettuare
     */
    public Result(boolean result, Operation opType) {
        this.result = result;
        this.opType = opType;
    }


    /**
     * Ritorna l'esito dell'operazione
     * @return un booleano che rappresenta l'esito dell'operazione
     */
    public boolean getResult() {
        return result;
    }


    /**
     * Setta l'esito dell'operazione
     * @param result booleano che rappresenta l'esito dell'operazione
     */
    public void setResult(boolean result) {
        this.result = result;
    }


    /**
     * Ritorna la lista che contiene tutte le informazioni dell'operazione effettuata.
     * @return la lista con tutte le informazioni riguardanti l'esito della operazione appena effattuata.
     * @see Error
     */
    public List<Error> getExtendedResult() {
        return extendedResult;
    }


    /**
     * Setta nella lista una specifica informazione riguardante l'esito dell'operazione.
     * @param enumerator l'errore sollevato durante l'esecuzione dell'operazione.
     * @see Error
     */
    public void setExtendedResult(Error enumerator) {
        this.extendedResult.add(enumerator);
    }


    /**
     * Restiusce il tipo dell'operazione.
     * @return tipologia di operazione.
     * @see Operation
     */
    public Operation getOpType() {
        return opType;
    }


    /**
     * Imposta il tipo di operazione.
     * @param opType tipologia di operazione da settare.
     * @see Operation
     */
    @SuppressWarnings("unused")
    public void setOpType(Operation opType) {
        this.opType = opType;
    }


    /**
     * Ritorna il cittadino che ha effettuato il login.
     * @return cittadino loggato.
     * @see Cittadino
     */
    public Cittadino getCittadino() {
        return cittadino;
    }


    /**
     * Imposta il cittadino che ha eseguito il login.
     * @param c cittadino loggato da settare.
     * @see Cittadino
     */
    public void setCittadino(Cittadino c) {
        cittadino = c;
    }


    /**
     * Ritorna il nome del centro in cui &amp;egrave stato vaccinato il cittadino.
     * @return nome del centro vaccinale.
     */
    public String getCentroCittadino() {
        return centroCittadino;
    }


    /**
     * Imposta il nome del centro in cui &amp;egrave stato vaccinato il cittadino.
     * @param centroCittadino nome del centro da settare.
     */
    public void setCentroCittadino(String centroCittadino) {
        this.centroCittadino = centroCittadino;
    }


    /**
     *
     * @param customClass
     * @param <T>
     * @return
     */
    @SuppressWarnings({"unused", "unchecked"})
    public <T> List<T> getList(Class<T> customClass) {
        List<T> lista = new ArrayList<>();
        for(Object o: list) {
            lista.add(((T) o));
        }
        return lista;
    }

    /**
     * 
     * @return
     */
    public Map<String, Double> getMap() {
        return map;
    }

    /**
     * 
     * @param map
     */
    public void setMap(Map<String, Double> map) {
        this.map = map;
    }

    /**
     * 
     * @param list
     */
    public void setList(List<Object> list) {
        this.list = list;
    }

    /**
     * Rappresenta i vari i tipi di errori che si possono riscontrare durante una determinata operazione
     */
    public enum Error {
        // Login utente
        USERNAME_NON_TROVATO, PASSWORD_ERRATA,
        //Registrazione eventi avversi
        EVENTO_GIA_INSERITO,
        //Registrazione utente
        CF_ID_NON_VALIDI, CITTADINO_GIA_REGISTRATO, EMAIL_GIA_IN_USO, USERID_GIA_IN_USO,
        //Registrazione vaccinato
        CF_GIA_IN_USO, IDVAC_GIA_IN_USO,
        // Generico
        NOME_IN_USO,
    }

    /**
     * Rappresenta i vari tipi di operazioni
     */
    public enum Operation {
        // Sezione operatore
        LOGIN_OPERATORE, REGISTRAZIONE_VACCINATO, RISULTATO_COMUNI, RISULTATO_CENTRI, REGISTRAZIONE_CENTRO,
        // Sezione cittadino
        LOGIN_CITTADINO, REGISTRAZIONE_CITTADINO, RICERCA_CENTRO, REGISTRA_EVENTO_AVVERSO, AGGIORNA_PASSWORD_CITTADINO, LEGGI_EVENTI_AVVERSI
    }
}
