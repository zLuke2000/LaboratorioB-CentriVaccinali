package it.uninsubria.centrivaccinali.models;

import java.io.Serializable;
import java.util.*;

public class Result implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<Error> extendedResult = new ArrayList<>();
    private List<Object> list;
    private Map<String, Double> map;
    private Operation opType;
    private boolean result;
    private Cittadino cittadino;
    private String centroCittadino;

    public Result(boolean result, Operation opType) {
        this.result = result;
        this.opType = opType;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<Error> getExtendedResult() {
        return extendedResult;
    }

    public void setExtendedResult(Error enumerator) {
        this.extendedResult.add(enumerator);
    }

    public Operation getOpType() {
        return opType;
    }

    @SuppressWarnings("unused")
    public void setOpType(Operation opType) {
        this.opType = opType;
    }

    public Cittadino getCittadino() {
        return cittadino;
    }

    public void setCittadino(Cittadino c) {
        cittadino = c;
    }

    public String getCentroCittadino() {
        return centroCittadino;
    }

    public void setCentroCittadino(String centroCittadino) {
        this.centroCittadino = centroCittadino;
    }

    @SuppressWarnings({"unused", "unchecked"})
    public <T> List<T> getList(Class<T> customClass) {
        List<T> lista = new ArrayList<>();
        for(Object o: list) {
            lista.add(((T) o));
        }
        return lista;
    }

    public Map<String, Double> getMap() {
        return map;
    }

    public void setMap(Map<String, Double> map) {
        this.map = map;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

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

    public enum Operation {
        // Sezione operatore
        LOGIN_OPERATORE, REGISTRAZIONE_VACCINATO, RISULTATO_COMUNI, RISULTATO_CENTRI, REGISTRAZIONE_CENTRO,
        // Sezione cittadino
        LOGIN_CITTADINO, REGISTRAZIONE_CITTADINO, RICERCA_CENTRO, REGISTRA_EVENTO_AVVERSO, AGGIORNA_PASSWORD_CITTADINO, LEGGI_EVENTI_AVVERSI
    }
}
