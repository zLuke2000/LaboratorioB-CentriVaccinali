package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
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

    @FXML void initialize() {
        this.client = CentriVaccinali.client;
        cittadinoConesso = client.getUtenteLoggato();
        if(cittadinoConesso != null) {
            mb_utente.setVisible(true);
            vb_free.setVisible(false);
            mb_utente.setText(cittadinoConesso.getUserid());
        } else {
            mb_utente.setVisible(false);
            vb_free.setVisible(true);
        }

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
    public void initParameter(ClientCV client) {
        /*this.client =  client;
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
        }*/
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
                resultController.setData(result.getResultCentri());
                break;
            default:
                break;
        }
    }

    public void showInfoMB(ActionEvent actionEvent) throws IOException {
    }

    public void logoutInfoMB() {
        client.LogoutUtente();
        System.out.println("L'utente ha eseguito il logout");
        vb_free.setVisible(true);
        mb_utente.setVisible(false);
    }


    public void loginDash() {
        //cp.testoSempliceConNumeri(tf_ci_loginUsername,4, 16) & (cp.password(tf_ci_loginPassword)
        if (!tf_loginUsername.getText().isBlank() && !tf_loginPassword.getText().isBlank()) {
            String username = tf_loginUsername.getText().trim();
            String password = tf_loginPassword.getText().trim();
            client.loginUtente(this, username, password);
        }
    }

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

