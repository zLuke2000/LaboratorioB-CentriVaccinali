package it.uninsubria.centrivaccinali.controller;

import com.jfoenix.controls.JFXComboBox;
import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.*;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.*;
import it.uninsubria.centrivaccinali.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

public class CIDashboardController extends Controller {

    private Cittadino cittadinoConesso = null;

    private ClientCV client;

    private ControlloParametri cp = ControlloParametri.getInstance();

    private final ObservableList<String> itemResearch = FXCollections.observableArrayList("Per nome", "Per comune e tipologia");

    private final ObservableList<String> itemCV = FXCollections.observableArrayList("Ospedaliero", "Aziendale" ,"Hub");

    @FXML
    private AnchorPane CI_AP_container;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ChoiceBox<String> CI_CB_SceltaRicerca;

    @FXML
    private ChoiceBox<String> CI_CB_ricercaTipologia;

    @FXML
    private ChoiceBox<String> CI_CB_ricercaTipologiaSearch;

    @FXML
    private JFXComboBox<String> CI_CB_SceltaRicerca2;

    @FXML
    private TextField CI_TF_userDash;

    @FXML
    private TextField CI_TF_ricercaNomeCV;

    @FXML
    private TextField CI_TF_ricercaComune;

    @FXML
    private TextField CI_TF_ricercaNomeCVSearch;

    @FXML
    private TextField CI_TF_ricercaComuneSearch;

    @FXML
    private PasswordField CI_TF_passwordDash;

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

    @FXML void initialize() {
        this.CI_CB_SceltaRicerca.getItems().addAll(itemResearch);
        this.CI_CB_SceltaRicerca.getSelectionModel().selectFirst();
        this.CI_CB_SceltaRicerca2.getItems().addAll(itemResearch);
        this.CI_CB_ricercaTipologia.getItems().addAll(itemCV);
        this.CI_CB_ricercaTipologia.getSelectionModel().selectFirst();
        this.CI_CB_ricercaTipologiaSearch.getItems().addAll(itemCV);
    }

    @Override
    public void initParameter(ClientCV client) {
        this.client =  client;
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
    }

    @Override
    public void notifyController(Result result) {
        if (result.getOpType() == Result.LOGIN_UTENTE){
            CI_TF_userDash.setVisible(false);
            CI_TF_passwordDash.setVisible(false);
            CI_B_loginDash.setVisible(false);
            CI_PI_loadLoginDash.setVisible(false);
            CI_MB_SiCitt.setText(client.getUtenteLoggato().getUserid());
            CI_MB_SiCitt.setVisible(true);
        }
        else {
            if (!result.getResultCentri().isEmpty()) {
                for (CentroVaccinale cv: result.getResultCentri()) {
                    System.out.println(cv);
                }
            }
            else {
                System.err.println("nessun risultato");
                CI_L_NessunRisultato.setVisible(true);
            }
        }
    }

    public void cercaCentroVaccinale(ActionEvent actionEvent) {
        System.out.println(actionEvent.getSource().toString());
        /*
        if(CI_TF_ricercaNomeCV.isVisible() && cp.testoSempliceSenzaNumeri(CI_TF_ricercaNomeCV, 6, 50)) {
                String nomeCV = CI_TF_ricercaNomeCV.getText().trim();
                client.ricercaPerNome(this, nomeCV);
                CI_CB_SceltaRicerca.setVisible(false);
                target_Ricerca.setVisible(false);
                CI_TF_ricercaNomeCV.setVisible(false);
                CI_B_ricerca.setVisible(false);
                CI_TF_ricercaNomeCVSearch.setVisible(true);
                CI_FI_ricercaNomeCV2.setVisible(true);
                CI_TF_ricercaNomeCVSearch.setText(nomeCV);
                CI_CB_SceltaRicerca2.setVisible(true);
                CI_CB_SceltaRicerca2.setValue("Per nome");
        } else if (CI_CB_ricercaTipologia.isVisible() && cp.testoSempliceSenzaNumeri(CI_TF_ricercaComune,3, 50)) {
                    String comuneCV = CI_TF_ricercaComune.getText().trim();
                    TipologiaCentro tipologiaCV = TipologiaCentro.valueOf(CI_CB_ricercaTipologia.getValue().toUpperCase());
                    client.ricercaPerComuneTipologia(this, comuneCV, tipologiaCV);
                    CI_CB_SceltaRicerca.setVisible(false);
                    target_Ricerca.setVisible(false);
                    CI_CB_ricercaTipologia.setVisible(false);
                    CI_TF_ricercaComune.setVisible(false);
                    CI_B_ricerca.setVisible(false);
                    CI_CB_ricercaTipologiaSearch.setVisible(true);
                    CI_CB_ricercaTipologiaSearch.setValue(CI_CB_ricercaTipologia.getValue());
                    CI_TF_ricercaComuneSearch.setVisible(true);
                    CI_TF_ricercaComuneSearch.setText(comuneCV);
                    CI_FI_research2.setVisible(true);
                    CI_CB_SceltaRicerca2.setVisible(true);
                    CI_CB_SceltaRicerca2.setValue("Per comune e tipologia");
            } else if(CI_TF_ricercaNomeCVSearch.isVisible() && cp.testoSempliceSenzaNumeri(CI_TF_ricercaNomeCVSearch, 6, 50)) {
                        String nomeCV = CI_TF_ricercaNomeCVSearch.getText().trim();
                        client.ricercaPerNome(this, nomeCV);
                    } else if(CI_CB_ricercaTipologiaSearch.isVisible() && cp.testoSempliceSenzaNumeri(CI_TF_ricercaComuneSearch, 3, 50)){
                                String comuneCV = CI_TF_ricercaComuneSearch.getText().trim();
                                TipologiaCentro tipologiaCV = TipologiaCentro.valueOf(CI_CB_ricercaTipologiaSearch.getValue().toUpperCase());
                                client.ricercaPerComuneTipologia(this, comuneCV, tipologiaCV);
                            }
         */
    }

    public void changeResearch(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(CI_CB_SceltaRicerca)) {
            if(CI_CB_SceltaRicerca.getSelectionModel().getSelectedItem().equals("Per nome")) {
                CI_TF_ricercaNomeCV.clear();
                CI_TF_ricercaNomeCV.setVisible(true);
                CI_B_ricerca.setVisible(true);
                CI_CB_ricercaTipologia.setVisible(false);
                CI_TF_ricercaComune.setVisible(false);
            } else if(CI_CB_SceltaRicerca.getSelectionModel().getSelectedItem().equals("Per comune e tipologia")) {
                        CI_TF_ricercaNomeCV.setVisible(false);
                        CI_B_ricerca.setVisible(true);
                        CI_CB_ricercaTipologia.getSelectionModel().selectFirst();
                        CI_CB_ricercaTipologia.setVisible(true);
                        CI_TF_ricercaComune.clear();
                        CI_TF_ricercaComune.setVisible(true);
            }
        }
    }

    public void changeResearch2(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(CI_CB_SceltaRicerca2)) {
            if(CI_CB_SceltaRicerca2.getSelectionModel().getSelectedItem().equals("Per nome")) {
                RicercaPerNome();
            } else if(CI_CB_SceltaRicerca2.getSelectionModel().getSelectedItem().equals("Per comune e tipologia")) {
                RicercaPerTipoCom();
            }
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
            CI_PI_loadLoginDash.setVisible(true);
            client.loginUtente(this, username, password);
        }
    }

    public void quitInfo(ActionEvent actionEvent) {
        CI_AP_container.setMouseTransparent(true);
        CI_AP_container.getChildren().removeAll(anchorPane);
        CI_AP_container.setVisible(false);
    }

    public void loginMB(ActionEvent actionEvent) {
    }

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
    }
}

