package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CIRegistrazioneController extends Controller {

    @FXML
    public AnchorPane ap_root;
    private ClientCV client;
    private ControlloParametri cp;
    private FXMLLoader loader;


    // TextField per l'acquisizione dei dati
    @FXML private TextField tf_ci_nomeRegistrazione;
    @FXML private TextField tf_ci_cognomeRegistrazione;
    @FXML private TextField tf_ci_cfRegistrazione;
    @FXML private TextField tf_ci_usernameRegistrazione;
    @FXML private TextField tf_ci_emailRegistrazione;
    @FXML private TextField tf_ci_idvaccinazioneRegistrazione;

    //PasswordField per l'acquisizione della password
    @FXML private PasswordField pf_ci_password1;
    @FXML private PasswordField pf_ci_password2;
    @FXML private ProgressIndicator pi_ci_loadIdVaccinazione;
    @FXML private ProgressIndicator pi_ci_loadUsername;

    private ClientCV client;
    private FXMLLoader loader;
    private ControlloParametri cp = ControlloParametri.getInstance();
    private CssHelper cssh = CssHelper.getInstance();

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
    }

    @Override
    public void notifyController(Result result) {
        if (result.getResult()) {
            System.out.println("Registrazione effettuato");
            Platform.runLater(() -> CentriVaccinali.setRoot("CI_dashboard"));
        } else {
            System.err.println("Registrazione fallita");

            //TODO tooltips
            if (result.getExtendedResult().contains(Result.Error.CF_ID_NON_VALIDI)) {
                cssh.toError(tf_ci_cfRegistrazione, new Tooltip("ID vaccinazione e codice fiscale non associati ad alcun vaccinato"));
                cssh.toError(tf_ci_idvaccinazioneRegistrazione, new Tooltip("ID vaccinazione e codice fiscale non associati ad alcun vaccinato"));
            }
            if (result.getExtendedResult().contains(Result.Error.CITTADINO_GIA_REGISTRATO)) {
                //TODO mostra popup
                System.err.println("Cittadino gia' registrato");
            }
            if (result.getExtendedResult().contains(Result.Error.EMAIL_GIA_IN_USO)) {
                cssh.toError(tf_ci_emailRegistrazione, new Tooltip("Email gia' registrata"));
            }
            if (result.getExtendedResult().contains(Result.Error.USERID_GIA_IN_USO)) {
                cssh.toError(tf_ci_usernameRegistrazione, new Tooltip("Username gia' registrato"));
            }
        }
    }

    @FXML public void registraCittadino() {
        String nome = tf_ci_nomeRegistrazione.getText().trim();
        String cognome = tf_ci_cognomeRegistrazione.getText().trim();
        String cf = tf_ci_cfRegistrazione.getText().trim();
        String email = tf_ci_emailRegistrazione.getText().trim();
        String user = tf_ci_usernameRegistrazione.getText().trim();
        String password = pf_ci_password1.getText().trim();
        long idVac = Long.parseLong(tf_ci_idvaccinazioneRegistrazione.getText());
        Cittadino cittadino = new Cittadino(nome, cognome, cf, email, user, password, idVac);
        System.out.println("Registro cittadino");
        client.registraCittadino(this, cittadino);
    }

    @FXML public void showInfoUsername() throws IOException {
        //TODO da sistemare
        loader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/dialogs/D_infoUsername.fxml"));
        DialogPane infoUsername = loader.load();
        Dialog dialog = new Dialog();
        dialog.setDialogPane(infoUsername);
        dialog.setTitle("INFO!");
        dialog.showAndWait();
    }

    @FXML public void showInfoPassword() throws IOException {
        //TODO da sistemare
        loader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/dialogs/D_infoPassword.fxml"));
        DialogPane infoUsername = loader.load();
        Dialog dialog = new Dialog();
        dialog.setDialogPane(infoUsername);
        dialog.setTitle("INFO!");
        dialog.showAndWait();
    }

    @FXML void showInfoVaccinazione() throws IOException {
        //TODO da sistemare
        loader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/dialogs/D_infoIdVaccinazione.fxml"));
        DialogPane infoUsername = loader.load();
        Dialog dialog = new Dialog();
        dialog.setDialogPane(infoUsername);
        dialog.setTitle("INFO!");
        dialog.showAndWait();
    }

    @FXML public void realtimeCheck(KeyEvent keyEvent) {
        Object key = keyEvent.getSource();
        if (tf_ci_nomeRegistrazione.equals(key)) {
            cp.testoSempliceConNumeri(tf_ci_nomeRegistrazione, 1, 50);
        }
        if (tf_ci_cognomeRegistrazione.equals(key)) {
            cp.testoSempliceSenzaNumeri(tf_ci_cognomeRegistrazione, 1, 50);
        }
        if(tf_ci_cfRegistrazione.equals(key)) {
            cp.codiceFiscale(tf_ci_cfRegistrazione);
        }
        if(tf_ci_emailRegistrazione.equals(key)) {
            cp.email(tf_ci_emailRegistrazione);
        }
        if(pf_ci_password1.equals(key)) {
            cp.password(pf_ci_password1);
            if(!pf_ci_password2.getText().isBlank()) {
                cp.checkSamePassword(pf_ci_password1, pf_ci_password2);
            }
        }
        if(pf_ci_password2.equals(key)) {
            cp.checkSamePassword(pf_ci_password1, pf_ci_password2);
        }
        if(tf_ci_usernameRegistrazione.equals(key)) {
            cp.testoSempliceConNumeri(tf_ci_usernameRegistrazione, 1, 16);
        }
        if(tf_ci_idvaccinazioneRegistrazione.equals(key)) {
            cp.numeri(tf_ci_idvaccinazioneRegistrazione, 16, 16);
        }
    }

    @FXML public void backTo() { CentriVaccinali.setRoot("CI_home"); }
}
