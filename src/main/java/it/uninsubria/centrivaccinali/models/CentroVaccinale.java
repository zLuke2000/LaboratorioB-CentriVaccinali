package it.uninsubria.centrivaccinali.models;

import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;

import java.io.Serializable;

/**
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele
 * @author Pintonello Christian 741112
 */
public class CentroVaccinale implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**  Rappresenta il nome del centro */
    private String nome;
    /**
     * Rappresenta il modello <code>Indirizzo</code>
     *
     * @see Indirizzo
     */
    private Indirizzo indirizzo;
    /**
     * Rappresenta il tipo enumerativo <code>TipologiaCentro</code>
     *
     * @see TipologiaCentro
     */
    private TipologiaCentro tipologia;

    /**
     * Costruttore vuoto dell'oggetto <code>CentroVaccinale</code>
     */
    public CentroVaccinale() {}

    /**
     * Costruttore dell'oggetto <code>CentroVaccinale</code>
     *
     * @param nome nome del centro
     * @param indirizzo indirizzo del centro
     * @param tipologia tipologia del centro
     */
    public CentroVaccinale(String nome, Indirizzo indirizzo, TipologiaCentro tipologia) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.tipologia = tipologia;
    }

    /**
     * Metodo getter per il campo <code>nome</code>
     * @return ritorna il nome del centro
     */
    public String getNome() {
        return nome;
    }

    /**
     * Metodo setter per il campo <code>nome</code>
     * @param nome il nuovo nome del centro
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Metodo getter per il campo <code>Indirizzo</code>
     * @return ritorna il modello <code>Indirizzo</code> del centro
     */
    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    /**
     * Metodo setter per il campo <code>Indirizzo</code>
     * @param indirizzo il nuovo indirizzo per il centro
     */
    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
    }

    /**
     * Metodo getter per il campo <code>Tipologia</code>
     * @return ritorna la tipologia del centro
     */
    public TipologiaCentro getTipologia() {
        return tipologia;
    }

    /**
     * Metodo setter per il campo <code>Tipologia</code>
     * @param tipologia il nuova tipologia del centro
     */
    public void setTipologia(TipologiaCentro tipologia) {
        this.tipologia = tipologia;
    }

    @Override
    public String toString() {
        return nome + " - " + indirizzo + " - " + tipologia;
    }
}
