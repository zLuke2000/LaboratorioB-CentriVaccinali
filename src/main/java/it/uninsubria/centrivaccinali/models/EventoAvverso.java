package it.uninsubria.centrivaccinali.models;

import it.uninsubria.centrivaccinali.enumerator.Vaccino;
import java.io.Serializable;

/**
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele
 * @author Pintonello Christian 741112
 *
 * Classe per descrivere eventuali eventi avversi verificatisi
 * a seguito della vaccinazione
 */
public class EventoAvverso implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Rappresenta l'id del vaccino associato all'evento avverso
     */
    private long idVaccino;

    /**
     * Descrive l'evento avverso avvenuto
     */
    private String evento;

    /**
     * Rappresenta il valore da 1 a 5 dell'intensit&agrave del valore avverso
     */
    private int severita;

    /**
     * Note opzionali per descrivere l'evento
     */
    private String note;


    private Vaccino tipoVac;

    /**
     * Costruttore primario per EventoAvverso (fase registrazione)
     * @param idVaccino id univoco vaccinazione
     * @param evento la stringa che descrive l'evento
     * @param severita l'intero che rappresent&agrave l'intensita dell'evento
     * @param note note opzionali per descrivere l'evento
     */
    public EventoAvverso(long idVaccino, String evento, int severita, String note) {
        this.idVaccino = idVaccino;
        this.evento = evento;
        this.severita = severita;
        this.note = note;
    }

    /**
     * Costruttore primario per EventoAvverso (fase lettura)
     * @param evento la stringa che descrive l'evento
     * @param severita l'intero che rappresent&agrave l'intensita dell'evento
     * @param note note opzionali per descrivere l'evento
     * @param tipoVac tipologia di vaccino utilizzato
     */
    public EventoAvverso(String evento, int severita, String note, Vaccino tipoVac) {
        this.evento = evento;
        this.severita = severita;
        this.note = note;
        this.tipoVac=tipoVac;
    }

    public long getIdVaccino() {
        return idVaccino;
    }

    @SuppressWarnings("unused")
    public void setIdVaccino(long idVaccino) {
        this.idVaccino = idVaccino;
    }

    /**
     *  Metodo getter per il tipo di evento
     * @return la stringa che rappresenta l'evento
     */
    public String getEvento() {
        return evento;
    }

    /**
     * Metodo setter per il tipo di evento
     * @param evento la stringa che rappresenta il nuovo tipo di evento
     */
    @SuppressWarnings("unused")
    public void setEvento(String evento) {
        this.evento = evento;
    }

    /**
     * Metodo getter per la severit&agrave dell'evento
     * @return l'intero che rappresenta la severit&agrave dell'evento
     */
    public int getSeverita() {
        return severita;
    }

    /**
     * Metodo setter per la severit&agrave dell'evento
     * @param severita l'intero che rappresenta la severit&agrave dell'evento
     */
    @SuppressWarnings("unused")
    public void setSeverita(int severita) {
        this.severita = severita;
    }

    /**
     * Metodo getter per le note opzionali
     * @return le note inserite
     */
    public String getNote() {
        return note;
    }

    /**
     * Metodo setter per le note opzionali
     * @param note le nuove note da inserire
     */
    @SuppressWarnings("unused")
    public void setNote(String note) {
        this.note = note;
    }


    public Vaccino getTipoVac() {
        return tipoVac;
    }


    @Override
    public String toString() {
        return "EventoAvverso{" +
                "idVaccino=" + idVaccino +
                ", evento='" + evento + '\'' +
                ", severita=" + severita +
                ", note='" + note + '\'' +
                '}';
    }
}
