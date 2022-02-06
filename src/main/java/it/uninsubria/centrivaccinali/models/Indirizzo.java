//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.models;

import it.uninsubria.centrivaccinali.enumerator.Qualificatore;

import java.io.Serial;
import java.io.Serializable;

/**
 * Rappresenta un indirizzo civico
 *
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele
 * @author Pintonello Christian 741112
 */

public class Indirizzo implements Serializable {

    /**
     * Varabile per identificare serial version RMI.
     */
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * Rappresenta il qualificatore della via tramite la classe <code>Qualificatore</code>
     * @see Qualificatore
     */
    private Qualificatore qualificatore;


    /**
     * Rappresenta il nome della "via" dell'indirizzo
     */
    private String nome;


    /**
     * Rappresenta il civico dell'indirizzo
     */
    private String civico;


    /**
     * Rappresenta il comune dell'indirizzo
     */
    private String comune;


    /**
     * Rappresenta la provincia dell'indirizzo
     */
    private String provincia;


    /**
     * Rappresenta il cap dell'indirizzo
     */
    private int cap;


    /**
     * Costruttore vuoto dell'oggetto <code>Indirizzo</code>
     */
    public Indirizzo() {}


    /**
     * Costruttore dell'oggetto <code>CentroVaccinale</code>
     *
     * @param qualificatore qualificatore dell'indirizzo
     * @param nome nome dell'indirizzo
     * @param civico numero civico dell'indirizzo
     * @param comune comune dell'indirizzo
     * @param provincia provincia dell'indirizzo
     * @param cap cap dell'indirizzo
     *
     */
    public Indirizzo(Qualificatore qualificatore, String nome, String civico, String comune, String provincia, int cap) {
        this.qualificatore = qualificatore;
        this.nome = nome;
        this.civico = civico;
        this.comune = comune;
        this.provincia = provincia;
        this.cap = cap;
    }


    /**
     * Metodo getter per il campo <code>Qualificatore</code>
     * @return ritorna il qualificatore dell'indirizzo
     */
    public Qualificatore getQualificatore() {
        return qualificatore;
    }


    /**
     * Metodo setter per il campo <code>Qualificatore</code>
     * @param qualificatore il nuovo qualificatore dell'indirizzo
     */
    public void setQualificatore(Qualificatore qualificatore) {
        this.qualificatore = qualificatore;
    }


    /**
     * Metodo getter per il campo <code>nome</code>
     * @return ritorna il nome dell'indirizzo
     */
    public String getNome() {
        return nome;
    }


    /**
     * Metodo setter per il campo <code>nome</code>
     * @param nome il nuovo nome dell'indirizzo
     */
    public void setNome(String nome) {
        this.nome = nome;
    }


    /**
     * Metodo getter per il campo <code>civico</code>
     * @return ritorna il numero civico dell'indirizzo
     */
    public String getCivico() {
        return civico;
    }


    /**
     * Metodo setter per il campo <code>civico</code>
     * @param civico il nuovo numero civico dell'indirizzo
     */
    public void setCivico(String civico) {
        this.civico = civico;
    }


    /**
     * Metodo getter per il campo <code>comune</code>
     * @return ritorna il comune dell'indirizzo
     */
    public String getComune() {
        return comune;
    }


    /**
     * Metodo setter per il campo <code>comune</code>
     * @param comune il nuovo comune dell'indirizzo
     */
    public void setComune(String comune) {
        this.comune = comune;
    }


    /**
     * Metodo getter per il campo <code>provincia</code>
     * @return ritorna la provincia dell'indirizzo
     */
    public String getProvincia() {
        return provincia;
    }


    /**
     * Metodo setter per il campo <code>provincia</code>
     * @param provincia la nuova provincia dell'indirizzo
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }


    /**
     * Metodo getter per il campo <code>cap</code>
     * @return ritorna il cap dell'indirizzo
     */
    public int getCap() {
        return cap;
    }


    /**
     * Metodo setter per il campo <code>cap</code>
     * @param cap il nuovo cap dell'indirizzo
     */
    public void setCap(Integer cap) {
        this.cap = cap;
    }


    /**
     * Ritorna la stringa che rappresenta l'indirizzo civico
     * @return la stringa che rappresenta l'indirizzo civico
     */
    @Override
    public String toString() {
        return (String.join(" ", qualificatore.toString(), nome, civico) + ", " + String.join(" ", comune, String.valueOf(cap), ("(" + provincia + ")")));
    }

}
