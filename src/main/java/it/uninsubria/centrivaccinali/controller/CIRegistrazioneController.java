package it.uninsubria.centrivaccinali.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class CIRegistrazioneController {

    private final String INFO_USERNAME =  "Controllare la disponibilità  dell'username, ricordalo  che ti servirà" +
            " all'autenticazione";
    private final String INFO_PASSWORD =  "- Min 8 caratteri, max 16 caretteri" + "\n" + "- Almeno un carattere speciale (!, ?, &, $, £...ecc)" +"\n"+
            "- Almeno un carattere maiuscolo" +  "\n" + " - Almeno un numero";
    private final String INFO_ID_VACCINAZIONE =  "L'ID della vaccinazione è stato fornito al momento della somministrazione";

    //Testo per le info dei vari campi
    /**
     *
     */
    @FXML private Text T_CI_infoRegistrazione;

    // TextField per l'acquisizione dei dati
    /**
     *
     */
    @FXML private TextField TX_CI_nomeRegistrazione;

    /**
     *
     */
    @FXML private TextField TX_CI_cognomeRegistrazione;

    /**
     *
     */
    @FXML private TextField TX_CI_cfRegistrazione;

    /**
     *
     */
    @FXML private TextField TX_CI_usernameRegistrazione;

    /**
     *
     */
    @FXML private TextField TX_CI_emailRegistrazione;

    /**
     *
     */
    @FXML private TextField TF_CI_password1Visible;

    /**
     *
     */
    @FXML private TextField TF_CI_password2Visible;

    /**
     *
     */
    @FXML private TextField TF_CI_idvaccinazioneRegistrazione;


    //PasswordField per l'acquisizione della password
    /**
     *
     */
    @FXML private PasswordField TF_CI_password1;

    /**
     *
     */
    @FXML private PasswordField TF_CI_password2;

    public void registraCittadino(ActionEvent actionEvent) {
    }

    public void showInfoUsername(MouseEvent mouseEvent) {
        if(!T_CI_infoRegistrazione.isVisible()) {
            T_CI_infoRegistrazione.setText(INFO_USERNAME);
            T_CI_infoRegistrazione.setVisible(true);
        } else if(T_CI_infoRegistrazione.isVisible() && !T_CI_infoRegistrazione.getText().equals(INFO_USERNAME)){
                    T_CI_infoRegistrazione.setText(INFO_USERNAME);
                } else {
                    T_CI_infoRegistrazione.setVisible(false);
                }
    }

    public void showInfoPassword(MouseEvent mouseEvent) {
        if(!T_CI_infoRegistrazione.isVisible()) {
            T_CI_infoRegistrazione.setText(INFO_PASSWORD);
            T_CI_infoRegistrazione.setVisible(true);
        } else if(T_CI_infoRegistrazione.isVisible() && !T_CI_infoRegistrazione.getText().equals(INFO_PASSWORD)){
                    T_CI_infoRegistrazione.setText(INFO_PASSWORD);
                } else {
                    T_CI_infoRegistrazione.setVisible(false);
                }
    }

    public void showInfoVaccinazione(MouseEvent mouseEvent) {
        if(!T_CI_infoRegistrazione.isVisible()) {
            T_CI_infoRegistrazione.setText(INFO_ID_VACCINAZIONE);
            T_CI_infoRegistrazione.setVisible(true);
        } else if(T_CI_infoRegistrazione.isVisible() && !T_CI_infoRegistrazione.getText().equals(INFO_ID_VACCINAZIONE)){
                    T_CI_infoRegistrazione.setText(INFO_ID_VACCINAZIONE);
                } else {
                    T_CI_infoRegistrazione.setVisible(false);
                }
    }
}
