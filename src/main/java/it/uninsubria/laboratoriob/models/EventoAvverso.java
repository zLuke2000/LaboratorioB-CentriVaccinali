package it.uninsubria.laboratoriob.models;

/**
 * Classe per descrivere eventuali eventi avversi verificatisi
 * a seguito della vaccinazione
 */
public class EventoAvverso {
    /**Descrive l'evento avverso avvenuto*/
    private String evento;
    /**Rappresenta il valore da 1 a 5 dell'intensita del valore avverso*/
    private int severita;
    /**Note opzionali per descrivere l'evento*/
    private String note="";

    public EventoAvverso() {}

    public EventoAvverso(String evento, int severita, String note) {
        this.evento = evento;
        this.severita = severita;
        this.note = note;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public int getSeverita() {
        return severita;
    }

    public void setSeverita(int severita) {
        this.severita = severita;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
