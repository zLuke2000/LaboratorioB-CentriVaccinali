package it.uninsubria.centrivaccinali.controller.cittadini;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Controller per l'interfaccia di login del cittadino.
 * @author ...
 */
public class CIHomeController extends Controller {

    /**
     * <code>TextField</code> per l'username del cittadino.
     */
    @FXML private TextField tf_loginUsername;
    /**
     * <code>PasswordField</code> per la password di autenticazione del cittadino.
     */
    @FXML private PasswordField tf_loginPassword;
    /**
     * <code>TextField</code> per mostrare al cittadino la password attualmente inserita.
     */
    @FXML private TextField tf_loginPasswordVisible;
    /**
     * <code>FontIcon</code> per nascondere la password.
     */
    @FXML private FontIcon fi_nascondiPassword;
    /**
     * <code>FontIcon</code> per mostrare la password.
     */
    @FXML private FontIcon fi_mostraPassword;
    /**
     * Riferimento al client su cui si sta eseguendo l'applicazione.
     * @see ClientCV
     */
    private final ClientCV client = CentriVaccinali.client;
    /**
     * Singleton di <code>ControlloParametri</code> che permette di controllare che le credenziali inserite rispettino i requisiti richiesti.
     * @see ControlloParametri
     */
    private final ControlloParametri cp = ControlloParametri.getInstance();
    /**
     * Rifermento al singleton <code>CssHelper</code> che permette la gestione degli stili per i vari componenti grafici.
     * @see CssHelper
     */
    private final CssHelper css = CssHelper.getInstance();

    /**
     * Notifica l'interfaccia quando una operazione di login &egrave stata completata.
     * @param result rappresenta l'operazione appena completata dal server.
     */
    @Override
    public void notifyController(Result result) {
        CentriVaccinali.scene.setCursor(Cursor.DEFAULT);
        if (result.getResult()) {
            Platform.runLater(() -> CentriVaccinali.setRoot("CI_dashboard"));
        }
        else {
            System.out.println("Login fallito");
            if (result.getExtendedResult().contains(Result.Error.USERNAME_NON_TROVATO)) {
                System.out.println("Username non trovato");
                css.toError(tf_loginUsername, new Tooltip("Username non corretto"));
                css.toDefault(tf_loginPasswordVisible);
                css.toDefault(tf_loginPassword);
            }
            if (result.getExtendedResult().contains(Result.Error.PASSWORD_ERRATA)) {
                System.out.println("Password errata");
                css.toError(tf_loginPassword, new Tooltip("Password non corretta"));
                css.toError(tf_loginPasswordVisible, new Tooltip("Password non corretta"));
            }
        }
    }

    /**
     * Metodo per entrare con l'accesso libero dentro all'applicazione.
     */
    @FXML
    private void toFreeAccess() {
        CentriVaccinali.setRoot("CI_dashboard");
    }

    /**
     * Metodo per accedere con le credenziali del cittadino.
     */
    @FXML
    private void loginCittadino() {
        //effettuo login se username e password non sono vuoti
        //e sono state inserite credenziali valide
        if (cp.testoSempliceConNumeri(tf_loginUsername,4, 16) & (cp.password(tf_loginPassword) || cp.password(tf_loginPasswordVisible))){
            String username= tf_loginUsername.getText();
            String password;
            if (!tf_loginPassword.isVisible()){
                password= tf_loginPasswordVisible.getText();
            } else {
                password= tf_loginPassword.getText();
            }
            if (!username.isBlank() && !password.isBlank()){
                CentriVaccinali.scene.setCursor(Cursor.WAIT);
                client.loginUtente(this, username, cp.encryptPassword(password));
            }
        }
    }

    /**
     * Metodo per passare all'interfaccia di registrazione.
     * @see CIRegistrazioneController
     */
    @FXML
    private void toRegistrazione() {
        CentriVaccinali.setRoot("CI_registrazione");
    }

    /**
     * Metodo per nascondere la password.
     */
    @FXML
    private void nascondiPassword() {
        //copia la password nel PasswordField
        //e cambia le visibilita' dei componenti
        tf_loginPassword.setText(tf_loginPasswordVisible.getText());
        tf_loginPasswordVisible.setVisible(false);
        tf_loginPassword.setVisible(true);
        fi_mostraPassword.setVisible(true);
        fi_nascondiPassword.setVisible(false);
    }

    /**
     * Metodo per mostrare la password.
     */
    @FXML
    private void mostraPassword() {
        //copia la password nel TextField
        //e cambia le visibilita' dei componenti
        tf_loginPasswordVisible.setText(tf_loginPassword.getText());
        tf_loginPasswordVisible.setVisible(true);
        tf_loginPassword.setVisible(false);
        fi_mostraPassword.setVisible(false);
        fi_nascondiPassword.setVisible(true);
    }

    /**
     * Metodo per tornare all'interfaccia precedente.
     */
    @FXML
    private void backTo() {
        CentriVaccinali.setRoot("Avvio");
        client.stopOperation();
    }

    /**
     * Permette la chiusura dell'applicazione tramite la chiamata alla superclasse.
     * @see Controller
     */
    @FXML
    private void chiudiApp() {
        super.closeApp();
    }

    /**
     * Permette di effettuare la login tramite il tasto invio.
     * @param keyEvent evento sollevato alla pressione di un tasto.
     */
    @FXML
    private void checkEnter(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            loginCittadino();
        }
    }
}
