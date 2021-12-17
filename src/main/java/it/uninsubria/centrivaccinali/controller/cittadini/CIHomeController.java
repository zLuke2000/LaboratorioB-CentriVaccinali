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
 *Controller per l'interfaccia di login del cittadino
 */
public class CIHomeController extends Controller {

    /**TextField per l'username del cittadino*/
    @FXML private TextField tf_loginUsername;

    /**PasswordField per la password di autenticazione del cittadino*/
    @FXML private PasswordField tf_loginPassword;

    /**TextField per mostrare la password del cittadino*/
    @FXML private TextField tf_loginPasswordVisible;

    /**FontIcon per nascondere la password*/
    @FXML private FontIcon fi_nascondiPassword;

    /**FontIcon per mostrare la password*/
    @FXML private FontIcon fi_mostraPassword;

    private final ClientCV client = CentriVaccinali.client;
    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final CssHelper css = CssHelper.getInstance();


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
     * Metodo per entrare con l'accesso libero dentro all'applicazione
     */
    @FXML
    private void toFreeAccess() {
        CentriVaccinali.setRoot("CI_dashboard");
    }

    /**
     * Metodo per accedere con le credenziali del cittadino
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
     * Metodo per passare all'interfaccia di registrazione
     */
    @FXML
    private void toRegistrazione() {
        CentriVaccinali.setRoot("CI_registrazione");
    }

    /**
     * Metodo per nascondere la password
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
     * Metodo per mostrare la password
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

    @FXML
    private void backTo() {
        CentriVaccinali.setRoot("Avvio");
        client.stopOperation();
    }

    @FXML
    private void chiudiApp() {
        super.closeApp();
    }

    @FXML
    private void checkEnter(KeyEvent keyEvent) {
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            loginCittadino();
        }
    }
}
