package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.ClientCV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;


public class CVLoginController {

    @FXML private TextField L_CV_username;
    @FXML private PasswordField L_CV_password;

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
            if (client.autenticaOperatore(username, password)) {
                System.out.println("AUTH OK");
            } else {
                System.err.println("AUTH KO");
            }
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

}
