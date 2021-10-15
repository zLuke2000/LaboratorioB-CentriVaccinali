package it.uninsubria.centrivaccinali.models;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    // Sezione operatore sanitario
    public static final int LOGIN_OPERATORE = 1;
    public static final int REGISTRAZIONE_VACCINATO = 2;
    public static final int RISULTATO_COMUNI = 3;
    public static final int RISULTATO_CENTRI = 4;
    public static final int REGISTRAZIONE_CENTRO = 5;
    // Sezione cittadino
    public static final int LOGIN_UTENTE = 11;
    public static final int REGISTRAZIONE_CITTADINO = 12;

    private int opType;
    private boolean result;
    private int extendedResult;
    private Cittadino cittadino;
    private List<String> resultComuni;
    private List<CentroVaccinale> resultCentri;

    public Result(boolean result, int opType) {
        this.result = result;
        this.opType = opType;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getExtendedResult() {
        return extendedResult;
    }

    public void setExtendedResult(int extendedResult) {
        this.extendedResult = extendedResult;
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
        cittadino=c;
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
}
