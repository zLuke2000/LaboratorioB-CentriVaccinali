package it.uninsubria.centrivaccinali.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Result implements Serializable {

    private static final long serialVersionUID = 1L;
    private Operation opType;
    private boolean result;
    private List<Error> extendedResult = new ArrayList<>();
    private Cittadino cittadino;
    private String centroCittadino;
    private List<String> resultComuni;
    private List<CentroVaccinale> resultCentri;
    private List<EventoAvverso> resultEA;

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

    public List<String> getResultComuni() {
        return resultComuni;
    }

    public void setResultComuni(List<String> resultComuni) {
        this.resultComuni = resultComuni;
    }

    public List<CentroVaccinale> getResultCentri() {
        return resultCentri;
    }

    public void setResultCentri(List<CentroVaccinale> resultCentri) {
        this.resultCentri = resultCentri;
    }

    public List<EventoAvverso> getListaEA() {
        return resultEA;
    }

    public void setListaEA(List<EventoAvverso> resultEA) {
        this.resultEA = resultEA;
    }

    public Map<String, Double> getMap() {
        return map;
    }

    public void setMap(Map<String, Double> map) {
        this.map = map;
    }

    public enum Error {
        // Login utente
        USERNAME_NON_TROVATO, PASSWORD_ERRATA,
        //Registrazione eventi avversi
        EVENTO_GIA_INSERITO,
        //Registrazione utente
        CF_NON_VALIDO, IDVAC_NON_VALIDO, CF_ID_NON_VALIDI, CITTADINO_GIA_REGISTRATO, EMAIL_GIA_IN_USO, USERID_GIA_IN_USO,
        //Registrazione vaccinato
        CF_GIA_IN_USO, IDVAC_GIA_IN_USO,
        // Generico
        NOME_IN_USO,
    }

    public enum Operation {
        // Sezione operatore
        LOGIN_OPERATORE, REGISTRAZIONE_VACCINATO, RISULTATO_COMUNI, RISULTATO_CENTRI, REGISTRAZIONE_CENTRO,
        // Sezione cittadino
        LOGIN_CITTADINO, REGISTRAZIONE_CITTADINO, RICERCA_CENTRO, REGISTRA_EVENTO_AVVERSO, LEGGI_EVENTI_AVVERSI
    }
}
