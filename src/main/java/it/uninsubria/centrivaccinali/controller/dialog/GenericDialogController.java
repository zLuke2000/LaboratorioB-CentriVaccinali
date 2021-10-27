package it.uninsubria.centrivaccinali.controller.dialog;

import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class GenericDialogController {

    @FXML public AnchorPane ap_root;
    @FXML public Label l_d_title;
    @FXML public Label l_d_description;
    @FXML public Button b_d_close;

    private DialogHelper dh;

    @FXML public void indietro() {
        dh.close();
    }

    public void setDH(DialogHelper dh) {
        this.dh = dh;
    }
}
