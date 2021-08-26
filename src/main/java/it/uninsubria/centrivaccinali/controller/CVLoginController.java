package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class CVLoginController {

    @FXML private TextField L_CV_username;
    @FXML private PasswordField L_CV_password;
    @FXML private ProgressIndicator PI_CV_load;
    @FXML private DialogPane DP_CV_info;

    private ClientCV client;
    private final Tooltip genericTooltip = new Tooltip("Inserire almeno un carattere");
    private static final Duration tooltipDelay = new Duration(0);

    public void initParameter(ClientCV client) {
        this.client = client;
    }

    @FXML
    void autenticazioneOperatore(ActionEvent event) {
        String username = L_CV_username.getText().trim();
        String password = L_CV_password.getText().trim();
        boolean check = true;

        if(username.length() == 0) {
            toError(L_CV_username);
            check = false;
        } else {
            toDefault(L_CV_username);
        }
        if(password.length() == 0) {
            toError(L_CV_password);
            check = false;
        } else {
            toDefault(L_CV_password);
        }

        if(check) {
            PI_CV_load.setVisible(true);
            client.autenticaOperatore(this, username, password);
        }
    }

    /**
     * Imposta a "rosso" il contorno della casella passata come parametro
     * @param tic parametro generico per molteplici <code>text input controls</code>
     */
    public void toError(TextInputControl tic) {
        tic.getStyleClass().add("field-error");
        genericTooltip.getStyleClass().add("tooltip-error");
        genericTooltip.setShowDelay(tooltipDelay);
        tic.setTooltip(genericTooltip);
    }

    /**
     * Reimposta a default il contorno della casella passata come parametro
     * @param tic parametro generico per molteplici <code>text input controls</code>
     */
    public void toDefault(TextInputControl tic) {
        tic.getStyleClass().remove("field-error");
    }

    public void authStatus(Boolean status) {
        PI_CV_load.setVisible(false);
        if(status) {
            CentriVaccinali.setRoot("CV_change");
        } else {

        }
    }

    public void ShowInfo(MouseEvent mouseEvent) {
        DP_CV_info.setVisible(!DP_CV_info.isVisible());
    }
}
