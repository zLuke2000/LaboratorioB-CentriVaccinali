//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.util;

import javafx.scene.control.*;
import javafx.util.Duration;

/**
 * Oggetto singleton che permette di gestire e modificare gli stili delle componenti grafiche dell'interfacce.
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele 742495
 * @author Pintonello Christian 741112
 */
public class CssHelper {


    /**
     * Instanza della classe stessa
     */
    private static CssHelper instance = null;


    /**
     * Costruttore primario della classe
     */
    private CssHelper(){ }


    /**
     * Ritorna l'oggetto singleton di questa classe.
     * @return Istanza della classe stessa
     */
    public static CssHelper getInstance(){
        if(instance == null){
            instance = new CssHelper();
        }
        return instance;
    }


    /**
     * Modifica lo stile di un componente dell'interfaccia mostrando un contorno rosso.
     * @param c componente grafico da modificare.
     * @param tooltip messaggio da mostrare.
     */
    public void toError(Control c, Tooltip tooltip) {
        toDefault(c);
        c.getStyleClass().add("field-error");
        if(tooltip != null) {
            tooltip.getStyleClass().add("tooltip-error");
            tooltip.setShowDelay(new Duration(0));
            c.setTooltip(tooltip);
        }
    }


    /**
     * Modifica lo stile di un componente dell'interfaccia mostrando un contorno verde.
     * @param c componente grafico da modificare.
     */
    public void toValid(Control c) {
        toDefault(c);
        c.getStyleClass().add("field-valid");
    }


    /**
     * Rimuove gli stili aggiunti ad un dato componente dell'interfaccia
     * @param c componente grafico da modificare.
     */
    public void toDefault(Control c) {
        c.getStyleClass().remove("field-error");
        c.getStyleClass().remove("field-valid");
        c.setTooltip(null);
    }


    /**
     * Modifica lo stile di due bottoni mostrandone uno selezionato e l'altro no.
     * @param bSelected bottone da mostrare come selezionato.
     * @param bNotSelected bottone da mostrare come non selezionato.
     */
    public void toggle(Button bSelected, Button bNotSelected) {
        // Rimuovo tutte le classi
        bSelected.getStyleClass().remove("bottone-info-centri-selected");
        bSelected.getStyleClass().remove("bottone-info-centri-not-selected");
        bNotSelected.getStyleClass().remove("bottone-info-centri-selected");
        bNotSelected.getStyleClass().remove("bottone-info-centri-not-selected");

        // Aggiungo solo la classe necessaria
        bSelected.getStyleClass().add("bottone-info-centri-selected");
        bNotSelected.getStyleClass().add("bottone-info-centri-not-selected");
    }
}
