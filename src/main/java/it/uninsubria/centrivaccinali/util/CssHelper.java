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

    public void toError(Control tic, Tooltip tooltip) {
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
    public void toValid(Control tic) {
        toDefault(tic);
        tic.getStyleClass().add("field-valid");
    }

    /**
     * Reimposta a default il contorno della casella passata come parametro
     * @param tic parametro generico per molteplici <code>text input controls</code>
     */
    public void toDefault(Control tic) {
        tic.getStyleClass().remove("field-error");
        tic.getStyleClass().remove("field-valid");
        tic.setTooltip(null);
    }
}
