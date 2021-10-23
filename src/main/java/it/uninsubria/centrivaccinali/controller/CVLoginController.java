package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Controller per l'interfaccia di autenticazione degli operatori vaccinali
 */
public class CVLoginController extends Controller {

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

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
        T_CV_infoLogin.setText(INFO);
    }

    @Override
    public void notifyController(Result result) {
        PI_CV_load.setVisible(false);
        if(result.getResult()) {
            Platform.runLater(() -> CentriVaccinali.setRoot("CV_change"));
        } else {
            //TODO mostra errore
            System.err.println("[CVLogin] AUTH ERROR");
        }
    }

    @FXML void autenticazioneOperatore() {
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

    @FXML public void BackTo() {
        System.out.println("Indietro");
        client.stopOperation();
        CentriVaccinali.setRoot("Avvio");
    }

    @FXML public void ShowInfo() {
            T_CV_infoLogin.setVisible(!T_CV_infoLogin.isVisible());
    }
}
