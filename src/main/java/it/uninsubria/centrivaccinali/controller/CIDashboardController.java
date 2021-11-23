package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;

public class CIDashboardController extends Controller {

    public MenuButton CI_MB_SiCitt;
    public Button CI_B_loginDash;
    private Cittadino cittadinoConesso = null;
    private ClientCV client;
    private ControlloParametri cp = ControlloParametri.getInstance();
    private CIRicercaResultController resultController = null;

    //Container
    @FXML
    public Pane ci_p_container;

    /*
    @FXML
    private AnchorPane CI_AP_container;

    @FXML
    private AnchorPane CI_AP_ricercaHome;

    @FXML
    private AnchorPane CI_AP_ricerca2;

    @FXML
    private ChoiceBox<String> CI_CB_SceltaRicerca;

    @FXML
    private ChoiceBox<String> CI_CB_ricercaTipologia;

    @FXML
    private ChoiceBox<String> CI_CB_ricercaTipologiaSearch;

    @FXML
    private JFXComboBox<String> CI_CB_SceltaRicerca2;
     */

    @FXML
    private TextField CI_TF_userDash;

    /*
    @FXML
    private TextField CI_TF_ricercaNomeCV;

    @FXML
    private TextField CI_TF_ricercaComune;

    @FXML
    private TextField CI_TF_ricercaNomeCVSearch;

    @FXML
    private TextField CI_TF_ricercaComuneSearch;*/

    @FXML
    private PasswordField CI_TF_passwordDash;

    /*
    @FXML
    private Button CI_B_ricerca;

    @FXML
    private Button CI_B_loginDash;

    @FXML
    private Label target_Ricerca;

    @FXML
    private Label CI_L_NessunRisultato;

    @FXML
    private MenuButton CI_MB_SiCitt;

    @FXML
    private ProgressIndicator CI_PI_loadLoginDash;

    @FXML
    private FontIcon  CI_FI_ricercaNomeCV2;

    @FXML
    private FontIcon CI_FI_research2;

    @FXML
    private VBox CI_HB_containerItem;

    @FXML
    private ScrollPane ci_scrollpane;

    private Controller controllerRicerca;*/

    @FXML void initialize() {
    }

    @Override
    public void initParameter(ClientCV client, Scene scene) {
        this.client =  client;
        System.out.println("Dash: " + client);
        this.cittadinoConesso = client.getUtenteLoggato();
        if(cittadinoConesso != null) {
            System.out.println(cittadinoConesso.getUserid());
            CI_MB_SiCitt.setVisible(true);
            CI_MB_SiCitt.setText(cittadinoConesso.getUserid());
        } else {
            CI_MB_SiCitt.setVisible(false);
            CI_TF_userDash.setVisible(true);
            CI_TF_passwordDash.setVisible(true);
            CI_B_loginDash.setVisible(true);
        }
        //TODO sistemare magari fuori da initParameter
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class
                .getResource("fxml/fragments/fragmentDashboard/F_CI_ricercaHome.fxml"));
        try {
            AnchorPane ap = fxmlLoader.load();
            ci_p_container.getChildren().add(ap);
            CIRicercaHomeController c = fxmlLoader.getController();
            c.setParent(this);
            c.initParameter(client, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyController(Result result) {
        if (result.getOpType() == Result.Operation.LOGIN_CITTADINO) {
            Platform.runLater(() -> {
                CI_TF_userDash.setVisible(false);
                CI_TF_passwordDash.setVisible(false);
                CI_B_loginDash.setVisible(false);
                CI_MB_SiCitt.setText(client.getUtenteLoggato().getUserid());
                CI_MB_SiCitt.setVisible(true);
            });
        }
        else {
            if (resultController == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class
                        .getResource("fxml/fragments/fragmentDashboard/Idea1Doppia.fxml"));
                try {
                    AnchorPane ap = fxmlLoader.load();
                    Platform.runLater(() -> {
                        ci_p_container.getChildren().clear();
                        ci_p_container.getChildren().add(ap);
                    });
                    resultController = fxmlLoader.getController();
                    resultController.setParent(this);
                    resultController.initParameter(client, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resultController.setData(result.getResultCentri());
        }
    }

    public void showInfoMB(ActionEvent actionEvent) throws IOException {
    }

    public void logoutInfoMB(ActionEvent actionEvent) {

    }


    public void loginDash(ActionEvent actionEvent) {
        if (!CI_TF_userDash.getText().isBlank() && !CI_TF_passwordDash.getText().isBlank()) {
            String username = CI_TF_userDash.getText().trim();
            String password = CI_TF_passwordDash.getText().trim();
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

    //FIXME togliere
    public void prova() {
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class
                .getResource("fxml/fragments/fragmentDashboard/F_CI_ricercaResult.fxml"));
        try {
            AnchorPane paneRicerca = fxmlLoader.load();
            FadeTransition transition = new FadeTransition(Duration.millis(1000), paneRicerca);
            transition.setFromValue(0);
            transition.setToValue(1);
            transition.setInterpolator(Interpolator.EASE_IN);
            ci_p_container.getChildren().add(paneRicerca);
            transition.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chiudi() {
        super.closeApp();
    }

}

