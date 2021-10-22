package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 *Controller per l'interfaccia di login del cittadino
 */
public class CIHomeController extends Controller {

    private ClientCV client;

    private ControlloParametri cp = ControlloParametri.getInstance();

    /**TextField per l'username del cittadino*/
    @FXML private TextField TF_CI_loginUsername;

    /**PasswordField per la password di autenticazione del cittadino*/
    @FXML private PasswordField TF_CI_loginPassword;

    /**TextField per mostrare la password del cittadino*/
    @FXML private TextField TF_CI_loginPasswordVisible;

    /**FontIcon per nascondere la password*/
    @FXML private FontIcon FI_CI_hidePassword;

    /**FontIcon per mostrare la password*/
    @FXML private FontIcon FI_CI_showPassword;

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
    }

    @Override
    public void notifyController(Result result) {
        if (result.getResult()) {
            Platform.runLater(() -> {
                CentriVaccinali.setRoot("CI_dashboard");
            });
        }
        else {
            System.out.println("Login fallito");
            if (result.getExtendedResult() == Result.USERNAME_NON_TROVATO) {
                System.out.println("Username non trovato");
                //TODO popup
            } else if (result.getExtendedResult() == Result.PASSWORD_ERRATA) {
                System.out.println("Password errata");
                //TODO popup
            }
        }
    }

    /**
     * Metodo per entrare con l'accesso libero dentro all'applicazione
     * @param mouseEvent
     */
    public void ToFreeAccess(MouseEvent mouseEvent) {
        CentriVaccinali.setRoot("CI_dashboard");
    }

    /**
     * Metodo per accedere con le credenziali del  cittadino
     * @param actionEvent
     */
    public void loginCittadino(ActionEvent actionEvent) {
        //effettuo login se username e password non sono vuoti
        //e sono state inserite credenziali valide
        if (cp.testoSempliceConNumeri(TF_CI_loginUsername,4, 16) & (cp.password(TF_CI_loginPassword) || cp.password(TF_CI_loginPasswordVisible))){
            String username=TF_CI_loginUsername.getText();
            String password="";
            if (!TF_CI_loginPassword.isVisible()){
                password=TF_CI_loginPasswordVisible.getText();
            } else {
                password=TF_CI_loginPassword.getText();
            }
            if (!username.isBlank() && !password.isBlank()){
                client.loginUtente(this, username, password);
            }
        }
    }

    /**
     * Metodo per passare all'interfaccia di registrazione
     * @param mouseEvent
     */
    public void toRegistrazione(MouseEvent mouseEvent) {
        CentriVaccinali.setRoot("CI_registrazione");
    }

    /**
     * Metodo per nascondere la password 
     * @param mouseEvent click sull'icona per nascondere la password
     */
    public void hidePassword(MouseEvent mouseEvent) {
        //copia la password nel PasswordField
        //e cambia le visibilita' dei componenti
        String password = TF_CI_loginPasswordVisible.getText();
        TF_CI_loginPassword.setText(password);
        TF_CI_loginPasswordVisible.setVisible(false);
        TF_CI_loginPassword.setVisible(true);
        FI_CI_showPassword.setVisible(true);
        FI_CI_hidePassword.setVisible(false);
    }


    /**
     * Metodo per mostrare la password
     * @param mouseEvent click sull'icona per mostrare la password
     */
    public void showPassword(MouseEvent mouseEvent) {
        //copia la password nel TextField
        //e cambia le visibilita' dei componenti
        String password=TF_CI_loginPassword.getText();
        TF_CI_loginPasswordVisible.setText(password);
        TF_CI_loginPasswordVisible.setVisible(true);
        TF_CI_loginPassword.setVisible(false);
        FI_CI_showPassword.setVisible(false);
        FI_CI_hidePassword.setVisible(true);
    }

    public void backTo(MouseEvent mouseEvent) {
        CentriVaccinali.setRoot("Avvio");
        client.stopOperation();
    }
}
