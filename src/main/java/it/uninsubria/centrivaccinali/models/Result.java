package it.uninsubria.centrivaccinali.models;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {

    private static final long serialVersionUID = 1L;
    private Operation opType;
    private boolean result;
    private List<Error> extendedResult;
    private Cittadino cittadino;
    private List<String> resultComuni;
    private List<CentroVaccinale> resultCentri;

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

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public Cittadino getCittadino() {
        return cittadino;
    }

    public void setCittadino(Cittadino c) {
        cittadino = c;
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

    public enum Error {
        // Login utente
        USERNAME_NON_TROVATO, PASSWORD_ERRATA,
        //Registrazione utente
        CF_NON_VALIDO, IDVAC_NON_VALIDO, CF_ID_NON_VALIDI, CITTADINO_GIA_REGISTRATO, EMAIL_GIA_IN_USO, USERID_GIA_IN_USO,
        //Registrazione vaccinato
        CF_GIA_IN_USO, IDVAC_GIA_IN_USO,
        // Generico
        NOME_IN_USO,
    }

    public enum Operation {
        // Sezione operatore
        LOGIN_OPERATORE,REGISTRAZIONE_VACCINATO,RISULTATO_COMUNI,RISULTATO_CENTRI,REGISTRAZIONE_CENTRO,
        // Sezione cittadino
        LOGIN_CITTADINO, REGISTRAZIONE_CITTADINO, RICERCA_CENTRO,
    }
}
