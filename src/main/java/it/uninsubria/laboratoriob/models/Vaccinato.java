package it.uninsubria.laboratoriob.models;

import it.uninsubria.laboratoriob.enumerator.Vaccino;

import java.sql.Date;

public class Vaccinato {

    private String nomeCentro;
    private String nome;
    private String cogonme;
    private String codiceFiscale;
    private Date dataSomministrazione;
    private Vaccino vaccinoSomministrato;
    private long idVaccino;

    public Vaccinato() {}

    public Vaccinato(String nomeCentro, String nome, String cogonme, String codiceFiscale, Date dataSomministrazione, Vaccino vaccinoSomministrato, long idVaccino) {
        this.nomeCentro = nomeCentro;
        this.nome = nome;
        this.cogonme = cogonme;
        this.codiceFiscale = codiceFiscale;
        this.dataSomministrazione = dataSomministrazione;
        this.vaccinoSomministrato = vaccinoSomministrato;
        this.idVaccino = idVaccino;
    }

    public String getNomeCentro() {
        return nomeCentro;
    }

    public void setNomeCentro(String nomeCentro) {
        this.nomeCentro = nomeCentro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCogonme() {
        return cogonme;
    }

    public void setCogonme(String cogonme) {
        this.cogonme = cogonme;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public Date getDataSomministrazione() {
        return dataSomministrazione;
    }

    public void setDataSomministrazione(Date dataSomministrazione) {
        this.dataSomministrazione = dataSomministrazione;
    }

    public Vaccino getVaccinoSomministrato() {
        return vaccinoSomministrato;
    }

    public void setVaccinoSomministrato(Vaccino vaccinoSomministrato) {
        this.vaccinoSomministrato = vaccinoSomministrato;
    }

    public long getIdVaccino() {
        return idVaccino;
    }

    public void setIdVaccino(long idVaccino) {
        this.idVaccino = idVaccino;
    }
}
