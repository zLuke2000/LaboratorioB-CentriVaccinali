package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class CIRegistrazioneController {

    private ClientCV client;
    private CssHelper csshelper;
    private ControlloParametri cp;
    /*
    private final String INFO_USERNAME =  "Controllare la disponibilità  dell'username, ricordalo  che ti servirà" +
            " all'autenticazione";

     */
    /*
    private final String INFO_PASSWORD =  "Password non valida" + "\n" + "Deve contenre:" + "\n"  +
            "- Almeno 8 caratteri"  + "\n"
            + "- Almeno una lettere maiuscola" +
            "\n" + "- Almeno una lettere minuscola" + "\n" +
            "- Almeno un numero" + "\n" +"- Può contenere valori speciali";

     */

    // private final String INFO_ID_VACCINAZIONE =  "L'ID della vaccinazione è stato fornito al momento della somministrazione";

    //Testo per le info dei vari campi
    @FXML private Text T_CI_infoRegistrazione;

    // TextField per l'acquisizione dei dati
    @FXML private TextField TF_CI_nomeRegistrazione;
    @FXML private TextField TF_CI_cognomeRegistrazione;
    @FXML private TextField TF_CI_cfRegistrazione;
    @FXML private TextField TF_CI_usernameRegistrazione;
    @FXML private TextField TF_CI_emailRegistrazione;
    @FXML private TextField TF_CI_idvaccinazioneRegistrazione;

    //PasswordField per l'acquisizione della password
    @FXML private PasswordField TF_CI_password1;
    @FXML private PasswordField TF_CI_password2;
    @FXML private ProgressIndicator PI_CI_loadIdVaccinazione;
    @FXML private ProgressIndicator PI_CI_loadUsername;




    public void initParameter(ClientCV client) {
        this.client = client;
        //non usato
        this.csshelper = CssHelper.getInstance();
        this.cp = ControlloParametri.getInstance();
    }

    /**
     * @param actionEvent
     */
    public void registraCittadino(ActionEvent actionEvent) {
        String nome = TF_CI_nomeRegistrazione.getText().trim();
        String cognome = TF_CI_cognomeRegistrazione.getText().trim();
        String cf = TF_CI_cfRegistrazione.getText().trim();
        String email = TF_CI_emailRegistrazione.getText().trim();
        String user = TF_CI_usernameRegistrazione.getText().trim();
        String password = TF_CI_password1.getText().trim();
        long idVac = Long.parseLong(TF_CI_idvaccinazioneRegistrazione.getText());
        Cittadino cittadino = new Cittadino(nome, cognome, cf, email, user, password, idVac);
        client.registraCittadino(cittadino);
    }

    /**
     *
     * @param mouseEvent
     */
    public void showInfoUsername(MouseEvent mouseEvent) {
        /*
        if(!T_CI_infoRegistrazione.isVisible()) {
            T_CI_infoRegistrazione.setText(INFO_USERNAME);
            T_CI_infoRegistrazione.setVisible(true);
        } else if(T_CI_infoRegistrazione.isVisible() && !T_CI_infoRegistrazione.getText().equals(INFO_USERNAME)){
                    T_CI_infoRegistrazione.setText(INFO_USERNAME);
                } else {
                    T_CI_infoRegistrazione.setVisible(false);
                }
         */

    }

    /**
     *
     * @param mouseEvent
     */
    public void showInfoPassword(MouseEvent mouseEvent) {
        /*
        if(!T_CI_infoRegistrazione.isVisible()) {
            T_CI_infoRegistrazione.setText(INFO_PASSWORD);
            T_CI_infoRegistrazione.setVisible(true);
        } else if(T_CI_infoRegistrazione.isVisible() && !T_CI_infoRegistrazione.getText().equals(INFO_PASSWORD)){
                    T_CI_infoRegistrazione.setText(INFO_PASSWORD);
                } else {
                    T_CI_infoRegistrazione.setVisible(false);
                }
         */
    }

    /**
     *
     * @param mouseEvent
     */
    public void showInfoVaccinazione(MouseEvent mouseEvent) {
        /*
        if(!T_CI_infoRegistrazione.isVisible()) {
            T_CI_infoRegistrazione.setText(INFO_ID_VACCINAZIONE);
            T_CI_infoRegistrazione.setVisible(true);
        } else if(T_CI_infoRegistrazione.isVisible() && !T_CI_infoRegistrazione.getText().equals(INFO_ID_VACCINAZIONE)){
                    T_CI_infoRegistrazione.setText(INFO_ID_VACCINAZIONE);
                } else {
                    T_CI_infoRegistrazione.setVisible(false);
                }
         */
    }

    @FXML public void realtimeCheck(KeyEvent keyEvent) {
        Object key = keyEvent.getSource();
        if (TF_CI_nomeRegistrazione.equals(key)) {
            cp.testoSempliceConNumeri(TF_CI_nomeRegistrazione, 1, 50);
        }
        if (TF_CI_cognomeRegistrazione.equals(key)) {
            cp.testoSempliceSenzaNumeri(TF_CI_cognomeRegistrazione, 1, 50);
        }
        if(TF_CI_cfRegistrazione.equals(key)) {
            cp.codiceFiscale(TF_CI_cfRegistrazione);
        }
        if(TF_CI_emailRegistrazione.equals(key)) {
            cp.email(TF_CI_emailRegistrazione);
        }
        if(TF_CI_password1.equals(key)) {
            cp.password(TF_CI_password1);
        }
        if(TF_CI_password2.equals(key)) {
            cp.checkSamePassword(TF_CI_password1, TF_CI_password2);
        }
        if(TF_CI_usernameRegistrazione.equals(key)) {
            cp.testoSempliceConNumeri(TF_CI_usernameRegistrazione, 1, 16);
        }
        if(TF_CI_idvaccinazioneRegistrazione.equals(key)) {
            cp.numeri(TF_CI_idvaccinazioneRegistrazione, 16, 16);
        }
    }

    public void backTo(MouseEvent mouseEvent) { CentriVaccinali.setRoot("CI_home"); }
}
