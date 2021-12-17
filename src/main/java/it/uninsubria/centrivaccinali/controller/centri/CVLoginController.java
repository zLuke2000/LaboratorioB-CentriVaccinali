package it.uninsubria.centrivaccinali.controller.centri;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.CssHelper;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

/**
 * Controller per l'interfaccia di autenticazione degli operatori vaccinali
 *
 * Le credenziali vengono fornite solo se la persona è un operatore sanitario e ha il diritto di esercitare queste azioni:
 * - Registrare un hub vaccinale
 * - Registrare un cittadino che si è vaccinato nel proprio sito
 */
public class CVLoginController extends Controller {

    @FXML public Button b_accedi;
    @FXML private TextField l_username;
    @FXML private PasswordField l_password;

    private final ClientCV client = CentriVaccinali.client;
    private final CssHelper cssHelper = CssHelper.getInstance();

    @Override
    public void notifyController(Result result) {
        CentriVaccinali.scene.setCursor(Cursor.DEFAULT);
        b_accedi.setDisable(false);
        if(result.getResult()) {
            Platform.runLater(() -> CentriVaccinali.setRoot("CV_home"));
        } else {
            System.err.println("[CVLogin] AUTH ERROR");
            new DialogHelper("ERRORE", "Credenziali d'accesso non corrette", DialogHelper.Type.ERROR).display();
        }
    }

    @FXML
    private void autenticazioneOperatore() {
        String username = l_username.getText().trim();
        String password = l_password.getText().trim();
        boolean check = true;

        if(username.length() == 0) {
            cssHelper.toError(l_username, new Tooltip("Inserire almeno un carattere"));
            check = false;
        } else {
            cssHelper.toDefault(l_username);
        }
        if(password.length() == 0) {
            cssHelper.toError(l_password, new Tooltip("Inserire almeno un carattere"));
            check = false;
        } else {
            cssHelper.toDefault(l_password);
        }

        if(check) {
            CentriVaccinali.scene.setCursor(Cursor.WAIT);
            b_accedi.setDisable(true);
            client.autenticaOperatore(this, username, password);
        }
    }

    @FXML
    private void backTo() {
        client.stopOperation();
        CentriVaccinali.setRoot("Avvio");
    }

    @FXML
    private void ShowInfo() {
        new DialogHelper("Aiuto password", "La password verrà fornita solo a operatori sanitari che possono:\n- registrare un centro vaccinale\n- registrare un cittadino vaccinato", DialogHelper.Type.INFO).display();
    }

    @FXML
    private void chiudiApp() {
        super.closeApp();
    }
}
