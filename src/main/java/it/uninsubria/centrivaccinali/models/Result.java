//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.models;

import java.io.Serial;
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
    @Serial
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
     * Ritorna una lista di oggetti T partendo da una lista di oggetti Object.
     * @param customClass classe per la conversione.
     * @param <T> tipo di classe per la conversione.
     * @return una lista di oggetti T in base al parametro fornito.
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
     * Ritorna la mappa
     * @return mappa contenete dati a seguito di un'operazione
     */
    public Map<String, Double> getMap() {
        return map;
    }

    /**
     * Imposta la mappa
     * @param map mappa risultante a seguito di un'operazione
     */
    public void setMap(Map<String, Double> map) {
        this.map = map;
    }

    /**
     * Imposta la lista generica di oggetti.
     * @param list lista risultante a seguito di un'operazione
     */
    public void setList(List<Object> list) {
        this.list = list;
    }

    /**
     * Rappresenta i vari i tipi di errori che si possono riscontrare durante una determinata operazione
     */
    public enum Error {
        // Login utente
        /**
         * Errore username non trovato
         */
        USERNAME_NON_TROVATO,
        /**
         * Errore password errata
         */
        PASSWORD_ERRATA,
        //Registrazione eventi avversi
        /**
         * Errore evento avverso gi&amp;agrave registrato
         */
        EVENTO_GIA_INSERITO,
        //Registrazione utente
        /**
         * Errore codice fiscale o id vaccinazione non validi
         */
        CF_ID_NON_VALIDI,
        /**
         * Errore cittadino gi&amp;agrave registrato
         */
        CITTADINO_GIA_REGISTRATO,
        /**
         * Errore email gi&amp;agrave registrata
         */
        EMAIL_GIA_IN_USO,
        /**
         * Errore userID gi&amp;agrave registrato
         */
        USERID_GIA_IN_USO,
        //Registrazione vaccinato
        /**
         * Errore codice fiscale gi&amp;agrave registrato
         */
        CF_GIA_IN_USO,
        /**
         * Errore ID vaccinazione gi&amp;agrave registrato
         */
        IDVAC_GIA_IN_USO,
        // Generico
        /**
         * Errore nome gi&amp;agrave registrato
         */
        NOME_IN_USO,
    }

    /**
     * Rappresenta i vari tipi di operazioni
     */
    public enum Operation {
        // Sezione operatore
        /**
         * Operazione login operatore
         */
        LOGIN_OPERATORE,
        /**
         * Operazione registra cittadino a seguito di una vaccinazione
         */
        REGISTRAZIONE_VACCINATO,
        /**
         * Operazione ricerca comuni
         */
        RICERCA_COMUNI,
        /**
         * Operazione ricerca centri vaccinali
         */
        RICERCA_CENTRI,
        /**
         * Operazione registrazione centro vaccinale
         */
        REGISTRAZIONE_CENTRO,
        // Sezione cittadino
        /**
         * Operazione login cittadino
         */
        LOGIN_CITTADINO,
        /**
         * Operazione registrazione nuovo cittadino
         */
        REGISTRAZIONE_CITTADINO,
        /**
         * Operazione ricerca centro singolo
         */
        RICERCA_CENTRO,
        /**
         * Operazione registra evento avverso
         */
        REGISTRA_EVENTO_AVVERSO,
        /**
         * Operazione aggiorna password del cittadino
         */
        AGGIORNA_PASSWORD_CITTADINO,
        /**
         * Operazione leggi eventi avversi
         */
        LEGGI_EVENTI_AVVERSI
    }
}
