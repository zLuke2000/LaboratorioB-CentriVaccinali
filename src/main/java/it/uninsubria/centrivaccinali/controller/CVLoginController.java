package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Controller per l'interfaccia di autenticazione degli operatori vaccinali
 */
public class CVLoginController {

    private final String INFO = "Le credenziali vengono fornite solo se la persona è un operatore sanitario e " +
            "ha  il diritto di esercitare queste azioni:" + "\n" + "- Registrare un hub vaccinale" + "\n" + "- Registrare " +
            "un cittadino che si è vaccinato nel proprio sito";

    @FXML private Text T_CV_infoLogin;
    @FXML private TextField L_CV_username;
    @FXML private PasswordField L_CV_password;
    @FXML private ProgressIndicator PI_CV_load;
    @FXML private FontIcon FI_CV_info;

    private ClientCV client;
    private final CssHelper cssHelper = CssHelper.getInstance();

    public void initParameter(ClientCV client) {
        this.client = client;
        T_CV_infoLogin.setText(INFO);
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
            Platform.runLater(() -> CentriVaccinali.setRoot("CV_change"));
        } else {
            System.err.println("[CVLogin] AUTH ERROR");
        }
    }

    public void BackTo(MouseEvent mouseEvent) {
        System.out.println("Indietro");
        client.stopOperation();
        CentriVaccinali.setRoot("Avvio");
    }

    @FXML public void ShowInfo() {
            T_CV_infoLogin.setVisible(!T_CV_infoLogin.isVisible());
    }
}
