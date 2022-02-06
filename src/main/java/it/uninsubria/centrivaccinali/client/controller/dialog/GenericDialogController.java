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
 *
 */
public class GenericDialogController {
    /**
     *
     */
    @FXML public AnchorPane ap_root;


    /**
     *
     */
    @FXML public Label l_d_title;


    /**
     *
     */
    @FXML public Label l_d_description;


    /**
     *
     */
    @FXML public Button b_d_close;


    /**
     *
     */
    @FXML public FlowPane fp_buttons;


    /**
     *
     */
    private DialogHelper dh;


    /**
     *
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
