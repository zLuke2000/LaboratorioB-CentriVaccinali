package it.uninsubria.centrivaccinali.controller.dialog;

import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class GenericDialogController {

    @FXML public AnchorPane ap_root;
    @FXML public Label l_d_title;
    @FXML public Label l_d_description;
    @FXML public Button b_d_close;
    @FXML public FlowPane fp_buttons;

    private DialogHelper dh;

    @FXML public void indietro() {
        dh.close();
    }

    public void setDH(DialogHelper dh) {
        this.dh = dh;
    }

    public void addButton(Button b){
        fp_buttons.getChildren().add(b);
        b_d_close.setText("NO");

    }
}
