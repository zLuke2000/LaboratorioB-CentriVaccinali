package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.CssHelper;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Controller per l'interfaccia di autenticazione degli operatori vaccinali
 */
public class CVLoginController extends Controller {

    private final String INFO = "Le credenziali vengono fornite solo se la persona è un operatore sanitario e " +
            "ha  il diritto di esercitare queste azioni:" + "\n" + "- Registrare un hub vaccinale" + "\n" + "- Registrare " +
            "un cittadino che si è vaccinato nel proprio sito";

    @FXML public AnchorPane ap_root;
    @FXML private Text t_cv_infoLogin;
    @FXML private TextField l_cv_username;
    @FXML private PasswordField l_cv_password;
    @FXML private ProgressIndicator pi_cv_load;
    @FXML private FontIcon fi_cv_info;

    private ClientCV client;
    private final CssHelper cssHelper = CssHelper.getInstance();

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
        t_cv_infoLogin.setText(INFO);
    }

    @Override
    public void notifyController(Result result) {
        pi_cv_load.setVisible(false);
        if(result.getResult()) {
            Platform.runLater(() -> CentriVaccinali.setRoot("CV_change"));
        } else {
            System.err.println("[CVLogin] AUTH ERROR");
            DialogHelper dh = new DialogHelper("ERRORE", "Credenziali d'accesso non corrette", DialogHelper.Type.ERROR);
            dh.display(ap_root);
        }
    }

    @FXML void autenticazioneOperatore() {
        String username = l_cv_username.getText().trim();
        String password = l_cv_password.getText().trim();
        boolean check = true;

        if(username.length() == 0) {
            cssHelper.toError(l_cv_username, new Tooltip("Inserire almeno un carattere"));
            check = false;
        } else {
            cssHelper.toDefault(l_cv_username);
        }
        if(password.length() == 0) {
            cssHelper.toError(l_cv_password, new Tooltip("Inserire almeno un carattere"));
            check = false;
        } else {
            cssHelper.toDefault(l_cv_password);
        }

        if(check) {
            pi_cv_load.setVisible(true);
            client.autenticaOperatore(this, username, password);
        }
    }

    @FXML public void BackTo() {
        System.out.println("Indietro");
        client.stopOperation();
        CentriVaccinali.setRoot("Avvio");
    }

    @FXML public void ShowInfo() {
            t_cv_infoLogin.setVisible(!t_cv_infoLogin.isVisible());
    }

    @FXML
    void chiudi() {
        super.closeApp();
    }
}
