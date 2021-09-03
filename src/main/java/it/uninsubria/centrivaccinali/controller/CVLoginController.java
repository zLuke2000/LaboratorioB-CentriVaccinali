package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Controller per l'interfaccia di autenticazione degli operatori vaccinali
 */
public class CVLoginController {

    private final String INFO = "Le credenziali vengono fornite solo se la persona è un operatore sanitario e " +
            "ha  il diritto di esercitare queste azioni:" + "\n" + "- Registrare un hub vaccinale" + "\n" + "- Registrare " +
            "un cittadino che si è vaccinato nel proprio sito";

    @FXML private Text T_CV_infoLogin;

    /**
     *
     */
    @FXML private TextField L_CV_username;

    /**
     *
     */
    @FXML private PasswordField L_CV_password;

    /**
     *
     */
    @FXML private ProgressIndicator PI_CV_load;

    

    /**
     *
     */
    private ClientCV client;


    /**
     *
     */
    private final Tooltip genericTooltip = new Tooltip("Inserire almeno un carattere");


    /**
     *
     */
    private static final Duration tooltipDelay = new Duration(0);

    /**
     *
     * @param client
     */
    public void initParameter(ClientCV client) {
        this.client = client;
        T_CV_infoLogin.setText(INFO);
    }

    /**
     *
     * @param event
     */
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

    /**
     *
     * @param status
     */
    public void authStatus(Boolean status) {
        PI_CV_load.setVisible(false);
        if(status) {
            CentriVaccinali.setRoot("CV_change");
        } else {

        }
    }

    /**
     *
     * @param mouseEvent
     */
    public void BackTo(MouseEvent mouseEvent) {
        System.out.println("Indietro");
        CentriVaccinali.setRoot("Avvio");
    }


    /**
     * @param mouseEvent
     */
    public void ShowInfo(MouseEvent mouseEvent) {
        if(T_CV_infoLogin.isVisible()) {
            T_CV_infoLogin.setVisible(false);
        } else {
            T_CV_infoLogin.setVisible(true);
        }
    }
}
