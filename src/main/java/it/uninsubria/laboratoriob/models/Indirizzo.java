package it.uninsubria.laboratoriob.models;

import it.uninsubria.laboratoriob.enumerator.Qualificatore;

import java.io.Serializable;

public class Indirizzo implements Serializable {

    private Qualificatore qualificatore;
    private String nomeVia;
    private String civico;
    private String comune;
    private String provincia;
    private int cap;

    public Indirizzo() {}

    public Indirizzo(Qualificatore qualificatore, String nomeVia, String civico, String comune, String provincia, int cap) {
        this.qualificatore = qualificatore;
        this.nomeVia = nomeVia;
        this.civico = civico;
        this.comune = comune;
        this.provincia = provincia;
        this.cap = cap;
    }

    public Qualificatore getQualificatore() {
        return qualificatore;
    }

    public void setQualificatore(Qualificatore qualificatore) {
        this.qualificatore = qualificatore;
    }

    public String getNomeVia() {
        return nomeVia;
    }

    public void setNomeVia(String nomeVia) {
        this.nomeVia = nomeVia;
    }

    public String getCivico() {
        return civico;
    }

    public void setCivico(String civico) {
        this.civico = civico;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public int getCap() {
        return cap;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }
}
