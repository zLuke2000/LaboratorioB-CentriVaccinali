package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class CVLoginController {

    @FXML private TextField L_CV_username;
    @FXML private PasswordField L_CV_password;
    @FXML private ProgressIndicator PI_CV_load;
    @FXML private DialogPane DP_CV_info;

    private ClientCV client;
    private final CssHelper cssHelper = new CssHelper();

    public void initParameter(ClientCV client) {
        this.client = client;
    }

    @FXML
    void autenticazioneOperatore(ActionEvent event) {
        String username = L_CV_username.getText().trim();
        String password = L_CV_password.getText().trim();
        boolean check = true;

        if(username.length() == 0) {
            cssHelper.toError(L_CV_username, new Tooltip("Inserire almeno un carattere"));
            check = false;
        } else {
            cssHelper.toDefault(L_CV_username);
        }
        if(password.length() == 0) {
            cssHelper.toError(L_CV_password, new Tooltip("Inserire almeno un carattere"));
            check = false;
        } else {
            cssHelper.toDefault(L_CV_password);
        }

        if(check) {
            PI_CV_load.setVisible(true);
            client.autenticaOperatore(this, username, password);
        }
    }

    @FXML public void toDefault(KeyEvent ke) {
        cssHelper.toDefault((TextInputControl) ke.getSource());
    }

    public void authStatus(Boolean status) {
        PI_CV_load.setVisible(false);
        if(status) {
            CentriVaccinali.setRoot("CV_change");
        } else {
            System.err.println("[CVLogin] AUTH ERROR");
        }
    }

    @FXML  public void ShowInfo(MouseEvent mouseEvent) {
        DP_CV_info.setVisible(!DP_CV_info.isVisible());
    }
}
