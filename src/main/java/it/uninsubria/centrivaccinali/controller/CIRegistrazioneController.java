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
import java.io.IOException;

public class CIRegistrazioneController extends Controller {

    // TextField per l'acquisizione dei dati
    @FXML private TextField tf_ci_nomeRegistrazione;
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
                cssh.toError(TF_CI_cfRegistrazione, new Tooltip("ID vaccinazione e codice fiscale non associati ad alcun vaccinato"));
                cssh.toError(TF_CI_idvaccinazioneRegistrazione, new Tooltip("ID vaccinazione e codice fiscale non associati ad alcun vaccinato"));
            }
            if (result.getExtendedResult().contains(Result.Error.CITTADINO_GIA_REGISTRATO)) {
                //TODO mostra popup
                System.err.println("Cittadino gia' registrato");
            }
            if (result.getExtendedResult().contains(Result.Error.EMAIL_GIA_IN_USO)) {
                cssh.toError(TF_CI_emailRegistrazione, new Tooltip("Email gia' registrata"));
            }
            if (result.getExtendedResult().contains(Result.Error.USERID_GIA_IN_USO)) {
                cssh.toError(TF_CI_usernameRegistrazione, new Tooltip("Username gia' registrato"));
            }
        }
    }

    @FXML public void registraCittadino() {
        String nome = tf_ci_nomeRegistrazione.getText().trim();
        String cognome = TF_CI_cognomeRegistrazione.getText().trim();
        String cf = TF_CI_cfRegistrazione.getText().trim();
        String email = TF_CI_emailRegistrazione.getText().trim();
        String user = TF_CI_usernameRegistrazione.getText().trim();
        String password = TF_CI_password1.getText().trim();
        long idVac = Long.parseLong(TF_CI_idvaccinazioneRegistrazione.getText());
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
            if(!TF_CI_password2.getText().isBlank()) {
                cp.checkSamePassword(TF_CI_password1, TF_CI_password2);
            }
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

    @FXML public void backTo() { CentriVaccinali.setRoot("CI_home"); }
}
