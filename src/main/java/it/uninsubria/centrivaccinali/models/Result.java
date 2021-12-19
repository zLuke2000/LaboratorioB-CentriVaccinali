package it.uninsubria.centrivaccinali.models;

import java.io.Serializable;
import java.util.*;

public class Result implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final List<Error> extendedResult = new ArrayList<>();
    private List<Object> list;
    private Map<String, Double> map;
    private Operation opType;
    private boolean result;
    private Cittadino cittadino;
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
     * Ritorna la lista che contiene tutte le informazioni dell'operazione effettuata
     * @return la lista con tutte le informazioni riguardanti l'esito della operazione appena effattuata
     */
    public List<Error> getExtendedResult() {
        return extendedResult;
    }

    /**
     * Setta nella lista una specifica informazione riguardante l'esito dell'operazione
     * @param enumerator
     */
    public void setExtendedResult(Error enumerator) {
        this.extendedResult.add(enumerator);
    }

    /**
     *
     * @return
     */
    public Operation getOpType() {
        return opType;
    }

    /**
     *
     * @param opType
     */
    @SuppressWarnings("unused")
    public void setOpType(Operation opType) {
        this.opType = opType;
    }

    /**
     *
     * @return
     */
    public Cittadino getCittadino() {
        return cittadino;
    }

    /**
     *
     * @param c
     */
    public void setCittadino(Cittadino c) {
        cittadino = c;
    }

    /**
     *
     * @return
     */
    public String getCentroCittadino() {
        return centroCittadino;
    }

    /**
     *
     * @param centroCittadino
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
