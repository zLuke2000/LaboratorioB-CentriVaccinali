//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.models;

import it.uninsubria.centrivaccinali.enumerator.Vaccino;

import java.io.Serial;
import java.io.Serializable;

/**
 * Classe per descrivere eventuali eventi avversi verificatisi
 * a seguito della vaccinazione.
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele
 * @author Pintonello Christian 741112
 */
public class EventoAvverso implements Serializable {


    /**
     * Varabile per identificare serial version RMI.
     */
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * Rappresenta l'id del vaccino associato all'evento avverso.
     */
    private long idVaccino;


    /**
     * Descrive l'evento avverso avvenuto.
     */
    private String tipologiaEvento;


    /**
     * Rappresenta il valore da 1 a 5 dell'intensit&amp;agrave del valore avverso.
     */
    private int severita;


    /**
     * Note opzionali per descrivere l'evento.
     */
    private String note;


    /**
     * Tipologia del vaccino.
     */
    private Vaccino tipoVac;


    /**
     * Costruttore primario per EventoAvverso (fase registrazione).
     * @param idVaccino id univoco vaccinazione.
     * @param evento la stringa che descrive l'evento.
     * @param severita l'intero che rappresent&amp;agrave l'intensita dell'evento.
     * @param note note opzionali per descrivere l'evento.
     */
    public EventoAvverso(long idVaccino, String evento, int severita, String note) {
        this.idVaccino = idVaccino;
        this.tipologiaEvento = evento;
        this.severita = severita;
        this.note = note;
    }


    /**
     * Costruttore primario per EventoAvverso (fase lettura).
     * @param evento la stringa che descrive l'evento.
     * @param severita l'intero che rappresent&amp;agrave l'intensita dell'evento.
     * @param note note opzionali per descrivere l'evento.
     * @param tipoVac tipologia di vaccino utilizzato.
     */
    public EventoAvverso(String evento, int severita, String note, Vaccino tipoVac) {
        this.tipologiaEvento = evento;
        this.severita = severita;
        this.note = note;
        this.tipoVac=tipoVac;
    }


    /**
     * Getter per l'id di vaccinazione.
     * @return id di vaccinazione.
     */
    public long getIdVaccino() {
        return idVaccino;
    }


    /**
     * Setter per l'id di vaccinazione.
     * @param idVaccino id di vaccinazione da settare.
     */
    @SuppressWarnings("unused")
    public void setIdVaccino(long idVaccino) {
        this.idVaccino = idVaccino;
    }


    /**
     *  Metodo getter per il tipo di evento.
     * @return la stringa che rappresenta l'evento.
     */
    public String getTipologiaEvento() {
        return tipologiaEvento;
    }


    /**
     * Metodo setter per il tipo di evento.
     * @param tipologiaEvento la stringa che rappresenta il nuovo tipo di evento.
     */
    @SuppressWarnings("unused")
    public void setTipologiaEvento(String tipologiaEvento) {
        this.tipologiaEvento = tipologiaEvento;
    }


    /**
     * Metodo getter per la severit&amp;agrave dell'evento.
     * @return l'intero che rappresenta la severit&amp;agrave dell'evento.
     */
    public int getSeverita() {
        return severita;
    }


    /**
     * Metodo setter per la severit&amp;agrave dell'evento.
     * @param severita l'intero che rappresenta la severit&amp;agrave dell'evento.
     */
    @SuppressWarnings("unused")
    public void setSeverita(int severita) {
        this.severita = severita;
    }


    /**
     * Metodo getter per le note opzionali.
     * @return le note inserite.
     */
    public String getNote() {
        return note;
    }


    /**
     * Metodo setter per le note opzionali.
     * @param note le nuove note da inserire
     */
    @SuppressWarnings("unused")
    public void setNote(String note) {
        this.note = note;
    }


    /**
     * Getter per il tipo di vaccino.
     * @return tipologia del vaccino.
     */
    public Vaccino getTipoVac() {
        return tipoVac;
    }


    /**
     * Restituisce la stringa che rappresenta l'evento avverso.
     * @return stringa di questo oggetto.
     */
    @Override
    public String toString() {
        return "EventoAvverso{" +
                "idVaccino=" + idVaccino +
                ", evento='" + tipologiaEvento + '\'' +
                ", severita=" + severita +
                ", note='" + note + '\'' +
                '}';
    }
}
