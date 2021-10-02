package it.uninsubria.centrivaccinali.util;

import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class CssHelper {

    private static CssHelper instance=null;

    private CssHelper(){  }

    public static CssHelper getInstance(){
        if(instance == null){
            instance = new CssHelper();
        }
        return instance;
    }

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
     * Imposta a "verde" il contorno della casella passata come parametro
     * Resetta in automatico a default in caso fosse "rosso"
     * Rimuove il tooltip di errore
     * @param c parametro generico per molteplici <code>text input controls</code>
     */
    public void toValid(Control c) {
        toDefault(c);
        c.getStyleClass().add("field-valid");
    }

    /**
     * Reimposta a default il contorno della casella passata come parametro
     * @param c parametro generico per molteplici <code>text input controls</code>
     */
    public void toDefault(Control c) {
        c.getStyleClass().remove("field-error");
        c.getStyleClass().remove("field-valid");
        c.setTooltip(null);
    }
}
