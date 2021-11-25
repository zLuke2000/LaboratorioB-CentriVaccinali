package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CIDashboardController extends Controller {

    private Cittadino cittadinoConesso = null;
    private ClientCV client;
    private ControlloParametri cp = ControlloParametri.getInstance();
    private CIRicercaResultController resultController = null;

    @FXML private MenuButton mb_utente;
    @FXML private VBox vb_free;
    @FXML private Button b_login;
    //Container
    @FXML private Pane p_container;
    // TextField
    @FXML private TextField tf_loginUsername;
    // PasswordField
    @FXML private PasswordField tf_loginPassword;

    @FXML void initialize() { }

    @Override
    public void initParameter(ClientCV client) {
        this.client =  client;
        cittadinoConesso = client.getUtenteLoggato();
        if(cittadinoConesso != null) {
            mb_utente.setVisible(true);
            vb_free.setVisible(false);
            mb_utente.setText(cittadinoConesso.getUserid());
        } else {
            mb_utente.setVisible(false);
            vb_free.setVisible(true);
        }
        //TODO sistemare magari fuori da initParameter
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class
                .getResource("fxml/fragments/fragmentDashboard/F_CI_ricercaHome.fxml"));
        try {
            AnchorPane ap = fxmlLoader.load();
            p_container.getChildren().add(ap);
            CIRicercaHomeController c = fxmlLoader.getController();
            c.setParent(this);
            c.initParameter(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyController(Result result) {
        switch (result.getOpType()) {
            case LOGIN_CITTADINO:
                if(result.getResult()) {
                    Platform.runLater(() -> {
                        System.out.println("cambio un sacco di roba");
                        vb_free.setVisible(false);
                        mb_utente.setVisible(true);
                        mb_utente.setText(client.getUtenteLoggato().getUserid());
                    });
                }
                break;
            case RICERCA_CENTRO:
                break;
            default:
                if (resultController == null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class
                            .getResource("fxml/fragments/fragmentDashboard/Idea1Doppia.fxml"));
                    try {
                        AnchorPane ap = fxmlLoader.load();
                        Platform.runLater(() -> {
                            p_container.getChildren().clear();
                            p_container.getChildren().add(ap);
                        });
                        resultController = fxmlLoader.getController();
                        resultController.setParent(this);
                        resultController.initParameter(client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            //resultController.setData(result.getResultCentri());
        }
    }

    public void showInfoMB(ActionEvent actionEvent) throws IOException {
    }

    public void logoutInfoMB(ActionEvent actionEvent) {

    }


    public void loginDash(ActionEvent actionEvent) {
        if (!tf_loginUsername.getText().isBlank() && !tf_loginPassword.getText().isBlank()) {
            String username = tf_loginUsername.getText().trim();
            String password = tf_loginPassword.getText().trim();
            client.loginUtente(this, username, password);
        }
    }

    public void loginMB(ActionEvent actionEvent) {
    }

    /*
    private void RicercaPerNome()  {
        CI_TF_ricercaNomeCVSearch.setVisible(true);
        CI_FI_ricercaNomeCV2.setVisible(true);
        CI_TF_ricercaComuneSearch.setVisible(false);
        CI_CB_ricercaTipologiaSearch.setVisible(false);
        CI_FI_research2.setVisible(false);
    }

    private void RicercaPerTipoCom() {
        CI_TF_ricercaNomeCVSearch.setVisible(false);
        CI_FI_ricercaNomeCV2.setVisible(false);
        CI_TF_ricercaComuneSearch.setVisible(true);
        CI_CB_ricercaTipologiaSearch.setVisible(true);
        CI_FI_research2.setVisible(true);
    } */

    @FXML
    public void backTo() {
        if (cittadinoConesso != null) {
            CentriVaccinali.setRoot("Avvio");
        } else {
            CentriVaccinali.setRoot("CI_home");
        }
        client.stopOperation();
    }

    public void chiudi() {
        super.closeApp(client);
    }

}

