package it.uninsubria.centrivaccinali.models;

import it.uninsubria.centrivaccinali.enumerator.Vaccino;

import java.io.Serializable;
import java.sql.Date;

/**
 * Rappresenta il cittadino vaccinato
 *
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele
 * @author Pintonello Christian 741112
 */

public class Vaccinato implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;


    /**
     * Rappresenta il nome del centro presso cui &amp;egrave stato somministrato il vaccino
     */
    private String nomeCentro;


    /**
     * Rappresenta il nome del cittadino vaccinato
     */
    private String nome;


    /**
     * Rappresenta il cognome del cittadino vaccinato
     */
    private String cognome;


    /**
     * Rappresenta il codice fiscale del cittadino vaccinato
     */
    private String codiceFiscale;


    /**
     * Rappresenta la data di somministrazione del vaccino
     */
    private Date dataSomministrazione;


    /**
     * Rappresenta il tipo di vaccino somministrato
     */
    private Vaccino vaccinoSomministrato;

    /**
     * Rappresenta l'id del vaccino associato al cittadino
     */
    private long idVaccino;


    /**
     * Costruttore vuoto per la classe <code>Vaccinato</code>
     */
    public Vaccinato() {}


    /**
     * Costruttore principale per Vaccinato
     * @param nomeCentro il nome del centro
     * @param nome il nome del cittadino vaccinato
     * @param cognome il cognome del cittadino vaccinato
     * @param codiceFiscale il codice fiscale del cittadino vaccinato
     * @param dataSomministrazione la data di somministrazione del vaccino
     * @param vaccinoSomministrato il tipo di vaccino somministrato
     * @param idVaccino l'id del vaccino associato al cittadino
     */
    public Vaccinato(String nomeCentro, String nome, String cognome, String codiceFiscale, Date dataSomministrazione, Vaccino vaccinoSomministrato, long idVaccino) {
        this.nomeCentro = nomeCentro;
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.dataSomministrazione = dataSomministrazione;
        this.vaccinoSomministrato = vaccinoSomministrato;
        this.idVaccino = idVaccino;
    }


    /**
     * Metodo getter per il nome del centro vaccinale
     * @return il nome del centro vaccinale presso cui &amp;egrave stata fatta la vaccinazione
     */
    public String getNomeCentro() {
        return nomeCentro;
    }


    /**
     * Metodo setter per il nome del centro vaccinale
     * @param nomeCentro il nuovo nome del centro vaccinale
     */
    public void setNomeCentro(String nomeCentro) {
        this.nomeCentro = nomeCentro;
    }


    /**
     * Metodo getter per il nome del cittadino vaccinato
     * @return il nome del cittadino vaccinato
     */
    public String getNome() {
        return nome;
    }


    /**
     * Metodo setter per il nome del cittadino vaccinato
     * @param nome il nuovo nome del cittadino vaccinato
     */
    public void setNome(String nome) {
        this.nome = nome;
    }


    /**
     * Metodo getter per il cognome del cittadino vaccinato
     * @return il cognome del cittadino vaccinato
     */
    public String getCognome() {
        return cognome;
    }


    /**
     * Metodo setter per il cognome del cittadino vaccinato
     * @param cognome il nuovo cognome del cittadino vaccinato
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }


    /**
     * Metodo getter per il codice fiscale del cittadino vaccinato
     * @return il codice fiscale del cittadino vaccinato
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }


    /**
     * Metodo setter per il codice fiscale del cittadino vaccinato
     * @param codiceFiscale il nuovo codice fiscale del cittadino vaccinato
     */
    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }


    /**
     * Metodo getter per la data di vaccinazione
     * @return la data di vaccinazione
     */
    public Date getDataSomministrazione() {
        return dataSomministrazione;
    }


    /**
     * Metodo setter per la data di vaccinazione
     * @param dataSomministrazione la nuova data di vaccinazione
     */
    public void setDataSomministrazione(Date dataSomministrazione) {
        this.dataSomministrazione = dataSomministrazione;
    }


    /**
     * Metodo getter per il tipo di vaccino somministrato
     * @return il tipo di vaccino somministrato
     */
    public Vaccino getVaccinoSomministrato() {
        return vaccinoSomministrato;
    }


    /**
     * Metodo setter per il tipo di vaccino somministrato
     * @param vaccinoSomministrato il nuovo tipo di vaccino somministrato
     */
    public void setVaccinoSomministrato(Vaccino vaccinoSomministrato) {
        this.vaccinoSomministrato = vaccinoSomministrato;
    }


    /**
     * Metodo getter per l'id del vaccino associato al cittadino
     * @return l'id di vaccinazione associato al cittadino
     */
    public long getIdVaccino() {
        return idVaccino;
    }


    /**
     * Metodo setter per l'id del vaccino associato al cittadino
     * @param idVaccino il nuovo id di vaccinazione
     */
    public void setIdVaccino(long idVaccino) {
        this.idVaccino = idVaccino;
    }


    /**
     * Ritorna la stringa corrispondente al cittadino vaccinato
     * @return la stringa che rappresenta il cittadino vaccinato
     */
    @Override
    public String toString() {
        return "Vaccinato{" +
                "nomeCentro='" + nomeCentro + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", codiceFiscale='" + codiceFiscale + '\'' +
                ", dataSomministrazione=" + dataSomministrazione +
                ", vaccinoSomministrato=" + vaccinoSomministrato +
                ", idVaccino=" + idVaccino +
                '}';
    }
}


