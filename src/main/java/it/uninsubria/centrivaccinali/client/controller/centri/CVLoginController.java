//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.client.controller.centri;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.client.Window;
import it.uninsubria.centrivaccinali.client.controller.Controller;
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
    /**
     * Riferimento al <code>Button</code> per effettuare l'acceso.
     */
    @FXML public Button b_accedi;


    /**
     * Riferimento alla <code>TextField</code> in cui inserire l'username.
     */
    @FXML private TextField tf_username;


    /**
     * Riferimento alla <code>PasswordField</code> in cui inserire la password.
     */
    @FXML private PasswordField pf_password;


    /**
     * Riferimento al client su cui si sta eseguendo l'applicazione.
     * @see ClientCV
     */
    private final ClientCV client = CentriVaccinali.client;


    /**
     * Rifermento al singleton <code>CssHelper</code> che permette la gestione degli stili per i vari componenti grafici.
     * @see CssHelper
     */
    private final CssHelper cssHelper = CssHelper.getInstance();


    /**
     * Permette di notificare l'interfaccia quando una operazione di login &amp;egrave stata completata.
     * @param result rappresenta l'operazione completata.
     */
    @Override
    public void notifyController(Result result) {
        Window.scene.setCursor(Cursor.DEFAULT);
        b_accedi.setDisable(false);
        if(result.getResult()) {
            Platform.runLater(() -> Window.setRoot("CV_home"));
        } else if (result.getOpType() == Result.Operation.LOGIN_OPERATORE) {
            System.err.println("[CVLogin] AUTH ERROR");
            Platform.runLater(() -> new DialogHelper("ERRORE", "Credenziali d'accesso non corrette", DialogHelper.Type.ERROR).display());
        }
    }


    /**
     * Metodo per effettuare il controllo delle credenziali secondo i requisiti richiesti.
     * Se le credenziali sono conformi ai requisiti richiesti viene quindi effettuata una richiesta di login al server.
     */
    @FXML
    private void autenticazioneOperatore() {
        String username = tf_username.getText().trim();
        String password = pf_password.getText().trim();
        boolean check = true;

        if(username.length() == 0) {
            cssHelper.toError(tf_username, new Tooltip("Inserire almeno un carattere"));
            check = false;
        } else {
            cssHelper.toDefault(tf_username);
        }
        if(password.length() == 0) {
            cssHelper.toError(pf_password, new Tooltip("Inserire almeno un carattere"));
            check = false;
        } else {
            cssHelper.toDefault(pf_password);
        }

        if(check) {
            Window.scene.setCursor(Cursor.WAIT);
            client.autenticaOperatore(this, username, password);
        }
    }


    /**
     * Metodo per tornare all'interfaccia precedente.
     */
    @FXML
    private void backTo() {
        client.stopOperation();
        Window.setRoot("Avvio");
    }


    /**
     * Mostra un pop-up informativo.
     */
    @FXML
    private void ShowInfo() {
        new DialogHelper("Aiuto password", "La password verrà fornita solo a operatori sanitari che possono:\n- registrare un centro vaccinale\n- registrare un cittadino vaccinato", DialogHelper.Type.INFO).display();
    }


    /**
     * Permette la chiusura dell'applicazione tramite la chiamata alla superclasse.
     * @see Controller
     */
    @FXML
    private void chiudiApp() {
        super.closeApp();
    }
}
