package it.uninsubria.laboratoriob.models;

import it.uninsubria.laboratoriob.enumerator.TipologiaCentro;

public class CentroVaccinale {

    private String nome;
    private Indirizzo indirizzo;
    private TipologiaCentro tipologia;

    public CentroVaccinale() {}

    public CentroVaccinale(String nome, Indirizzo indirizzo, TipologiaCentro tipologia) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.tipologia = tipologia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
    }

    public TipologiaCentro getTipologia() {
        return tipologia;
    }

    public void setTipologia(TipologiaCentro tipologia) {
        this.tipologia = tipologia;
    }
}
