package it.uninsubria.centrivaccinali.util;

import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class CssHelper {
    public CssHelper() {}

    /**
     * Imposta a "rosso" il contorno della casella passata come parametro
     * Imposta un tooltip con il messaggio di errore (se non nullo)
     * @param tic parametro generico per molteplici <code>text input controls</code>
     * @param tooltip parametro per assegnazione di tooltip (pu&ograve essere null)
     */
    public void toError(TextInputControl tic, Tooltip tooltip) {
        toDefault(tic);
        tic.getStyleClass().add("field-error");
        if(tooltip != null) {
            tooltip.getStyleClass().add("tooltip-error");
            tooltip.setShowDelay(new Duration(0));
            tic.setTooltip(tooltip);
        }
    }

    /**
     * Imposta a "verde" il contorno della casella passata come parametro
     * Resetta in automatico a default in caso fosse "rosso"
     * Rimuove il tooltip di errore
     * @param tic parametro generico per molteplici <code>text input controls</code>
     */
    public void toValid(TextInputControl tic) {
        toDefault(tic);
        tic.setTooltip(null);
        tic.getStyleClass().add("field-valid");
    }

    /**
     * Reimposta a default il contorno della casella passata come parametro
     * @param tic parametro generico per molteplici <code>text input controls</code>
     */
    public void toDefault(TextInputControl tic) {
        tic.getStyleClass().remove("field-error");
        tic.getStyleClass().remove("field-valid");
    }
}
