package it.uninsubria.centrivaccinali.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 *Controller per l'interfaccia di login del cittadino
 */
public class CIHome {

    /**PasswordField per la password di autenticazione del cittadino*/
    @FXML private PasswordField TF_CI_loginPassword;
    /**TextField per mostrare la password del cittadino*/
    @FXML private TextField TF_CI_loginPasswordVisible;

    /**FontIcon per nascondere la password*/
    @FXML private FontIcon FI_CI_hidePassword;
    /**FontIcon per mostrare la password*/
    @FXML private FontIcon FI_CI_showPassword;

    /**
     * Metodo per entrare con l'accesso libero dentro all'applicazione
     * @param mouseEvent
     */
    public void ToFreeAccess(MouseEvent mouseEvent) {
    }
    /**
     * Metodo per accedere con le credenziali del  cittadino
     * @param actionEvent
     */
    public void AccediCittadino(ActionEvent actionEvent) {
    }
    /**
     * Metodo per passare all'interfaccia di registrazione
     * @param mouseEvent
     */
    public void toRegistrazione(MouseEvent mouseEvent) {
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
}
