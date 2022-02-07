//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.client.controller.dialog;

import it.uninsubria.centrivaccinali.client.Window;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

/**
 * Controller per dialog generici
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele 742495
 * @author Pintonello Christian 741112
 */
public class GenericDialogController {


    /**
     * <code>AnchorPane</code> contenitore della seguente interfaccia.
     * @see AnchorPane
     */
    @FXML public AnchorPane ap_root;


    /**
     * <code>Label</code> che mostra il titolo del dialog.
     * @see Label
     */
    @FXML public Label l_d_title;


    /**
     * <code>Label</code> che mostra la descrizione del dialog.
     * @see Label
     */
    @FXML public Label l_d_description;


    /**
     * <code>Button</code> per chiudere l'interfaccia, valore di default "OK".
     * @see Button
     */
    @FXML public Button b_d_close;


    /**
     * <code>FlowPane</code> contenitore dei pulsanti, utilizzato
     * per aggiungere ulteriori pulsanti
     * @see FlowPane
     */
    @FXML public FlowPane fp_buttons;


    /**
     * Riferimento all'oggetto DialogHelper associato al seguente Dialog
     * @see DialogHelper
     */
    private DialogHelper dh;


    /**
     * Metodo associato al pulsante OK per chiudere il dialog
     */
    @FXML public void indietro() {
        dh.close();
        Window.scene.setCursor(Cursor.DEFAULT);
    }


    /**
     * Associa il controller preso da fxmlLoader al campo dh.
     * @param dh dialog preso da fxmlLoader.
     */
    public void setDH(DialogHelper dh) {
        this.dh = dh;
    }


    /**
     * Permette di aggiungere un bottone al dialog.
     * @param b bottone da aggiungere.
     */
    public void addButton(Button b){
        fp_buttons.getChildren().add(b);
        b_d_close.setText("NO");
    }
}
