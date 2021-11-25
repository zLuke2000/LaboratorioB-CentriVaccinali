package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 *Controller per l'interfaccia di login del cittadino
 */
public class CIHomeController extends Controller {

    @FXML public AnchorPane ap_root;
    private ClientCV client;

    private final ControlloParametri cp = ControlloParametri.getInstance();

    /**TextField per l'username del cittadino*/
    @FXML private TextField tf_ci_loginUsername;

    /**PasswordField per la password di autenticazione del cittadino*/
    @FXML private PasswordField tf_ci_loginPassword;

    /**TextField per mostrare la password del cittadino*/
    @FXML private TextField tf_ci_loginPasswordVisible;

    /**FontIcon per nascondere la password*/
    @FXML private FontIcon fi_ci_hidePassword;

    /**FontIcon per mostrare la password*/
    @FXML private FontIcon fi_ci_showPassword;

    private CssHelper cssh = CssHelper.getInstance();

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
    }

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
                cssh.toError(tf_ci_loginUsername, new Tooltip("Username non corretto"));
                cssh.toDefault(tf_ci_loginPasswordVisible);
                cssh.toDefault(tf_ci_loginPassword);
            }
            if (result.getExtendedResult().contains(Result.Error.PASSWORD_ERRATA)) {
                System.out.println("Password errata");
                cssh.toError(tf_ci_loginPassword, new Tooltip("Password non corretta"));
                cssh.toError(tf_ci_loginPasswordVisible, new Tooltip("Password non corretta"));
            }
        }
    }

    /**
     * Metodo per entrare con l'accesso libero dentro all'applicazione
     */
    @FXML
    public void toFreeAccess() {
        CentriVaccinali.setRoot("CI_dashboard");
    }

    /**
     * Metodo per accedere con le credenziali del cittadino
     */
    @FXML
    public void loginCittadino() {
        //effettuo login se username e password non sono vuoti
        //e sono state inserite credenziali valide
        if (cp.testoSempliceConNumeri(tf_ci_loginUsername,4, 16) & (cp.password(tf_ci_loginPassword) || cp.password(tf_ci_loginPasswordVisible))){
            String username= tf_ci_loginUsername.getText();
            String password;
            if (!tf_ci_loginPassword.isVisible()){
                password= tf_ci_loginPasswordVisible.getText();
            } else {
                password= tf_ci_loginPassword.getText();
            }
            if (!username.isBlank() && !password.isBlank()){
                CentriVaccinali.scene.setCursor(Cursor.WAIT);
                client.loginUtente(this, username, password);
            }
        }
    }

    /**
     * Metodo per passare all'interfaccia di registrazione
     */
    @FXML
    public void toRegistrazione() {
        CentriVaccinali.setRoot("CI_registrazione");
    }

    /**
     * Metodo per nascondere la password
     */
    @FXML
    public void hidePassword() {
        //copia la password nel PasswordField
        //e cambia le visibilita' dei componenti
        String password = tf_ci_loginPasswordVisible.getText();
        tf_ci_loginPassword.setText(password);
        tf_ci_loginPasswordVisible.setVisible(false);
        tf_ci_loginPassword.setVisible(true);
        fi_ci_showPassword.setVisible(true);
        fi_ci_hidePassword.setVisible(false);
    }


    /**
     * Metodo per mostrare la password
     */
    @FXML
    public void showPassword() {
        //copia la password nel TextField
        //e cambia le visibilita' dei componenti
        String password= tf_ci_loginPassword.getText();
        tf_ci_loginPasswordVisible.setText(password);
        tf_ci_loginPasswordVisible.setVisible(true);
        tf_ci_loginPassword.setVisible(false);
        fi_ci_showPassword.setVisible(false);
        fi_ci_hidePassword.setVisible(true);
    }

    @FXML
    public void backTo() {
        CentriVaccinali.setRoot("Avvio");
        client.stopOperation();
    }

    @FXML
    void chiudi() {
        super.closeApp(client);
    }
}
