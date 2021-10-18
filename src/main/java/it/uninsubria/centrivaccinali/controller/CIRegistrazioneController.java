package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.rmi.RemoteException;

public class CIRegistrazioneController extends Controller {

    private ClientCV client;
    private CssHelper csshelper;
    private ControlloParametri cp;
    private FXMLLoader loader;


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

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
        this.cp = ControlloParametri.getInstance();
    }

    @Override
    public void notifyController(Result result) {
        if (result.getResult()) {
            System.out.println("Registrazione effettuato");
            Platform.runLater(() -> {
                CentriVaccinali.setRoot("CI_dashboard");
            });
        }
        System.err.println("Registrazione fallita");
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
        System.out.println("Registro cittadino");
        client.registraCittadino(this, cittadino);
    }

    /**
     *
     * @param mouseEvent
     */
    public void showInfoUsername(MouseEvent mouseEvent) throws IOException {
        loader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/dialogs/D_infoUsername.fxml"));
        DialogPane infoUsername = loader.load();
        Dialog dialog = new Dialog();
        dialog.setDialogPane(infoUsername);
        dialog.setTitle("INFO!");
        dialog.showAndWait();
    }

    /**
     *
     * @param mouseEvent
     */
    public void showInfoPassword(MouseEvent mouseEvent) throws IOException {
        loader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/dialogs/D_infoPassword.fxml"));
        DialogPane infoUsername = loader.load();
        Dialog dialog = new Dialog();
        dialog.setDialogPane(infoUsername);
        dialog.setTitle("INFO!");
        dialog.showAndWait();
    }

    /**
     *
     * @param mouseEvent
     */
    public void showInfoVaccinazione(MouseEvent mouseEvent) throws IOException {
        loader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/dialogs/D_infoIdVaccinazione.fxml"));
        DialogPane infoUsername = loader.load();
        Dialog dialog = new Dialog();
        dialog.setDialogPane(infoUsername);
        dialog.setTitle("INFO!");
        dialog.showAndWait();
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

    public void backTo(MouseEvent mouseEvent) { CentriVaccinali.setRoot("CI_home"); }
}
