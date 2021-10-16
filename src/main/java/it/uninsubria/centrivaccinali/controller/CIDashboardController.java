package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.*;
import it.uninsubria.centrivaccinali.models.*;
import it.uninsubria.centrivaccinali.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.kordamp.ikonli.javafx.FontIcon;

public class CIDashboardController {

    private Cittadino CittadinoConesso = null;

    private CssHelper cssh = CssHelper.getInstance();

    private ClientCV client;

    private ControlloParametri cp = ControlloParametri.getInstance();

    private final ObservableList<String> itemResearch = FXCollections.observableArrayList("Per nome", "Per comune e tipologia");

    private final ObservableList<String> itemCV = FXCollections.observableArrayList("Ospedaliero", "Aziendale" ,"Hub");

    @FXML
    private AnchorPane CI_AP_container;

    @FXML
    private ChoiceBox<String> CI_CB_SceltaRicerca;

    @FXML
    private ChoiceBox<String> CI_CB_ricercaTipologia;

    @FXML
    private TextField CI_TF_ricercaNomeCV;

    @FXML
    private TextField CI_TF_ricercaComune;

    @FXML
    private Button CI_B_ricerca;

    @FXML
    private Label target_Ricerca;

    @FXML
    private MenuButton CI_MB_NoCitt;

    @FXML
    private MenuButton CI_MB_SiCitt;

    public void initParameter(ClientCV client) {
        this.client =  client;
        this.CittadinoConesso = client.getUtenteLoggato();
        if(CittadinoConesso != null) {
            System.out.println(CittadinoConesso.getUserid());
            CI_MB_SiCitt.setVisible(true);
            CI_MB_NoCitt.setVisible(false);
            CI_MB_SiCitt.setText(CittadinoConesso.getUserid());
        } else {
            System.out.println("No");
            CI_MB_SiCitt.setVisible(false);
            CI_MB_NoCitt.setVisible(true);
        }
    }

    @FXML void initialize() {
        //this.CI_TF_ricercaNomeCV.getStyleClass().add("field-error");
        //this.CI_TF_ricercaNomeCV.setStyle("-fx-border-color: red");
        this.CI_CB_SceltaRicerca.getItems().addAll(itemResearch);
        this.CI_CB_SceltaRicerca.getSelectionModel().selectFirst();
        this.CI_CB_ricercaTipologia.getItems().addAll(itemCV);
        this.CI_CB_ricercaTipologia.getSelectionModel().selectFirst();
    }

    public void cercaCentroVaccinale(ActionEvent actionEvent) {
        String nomeCV = null;
        String tipologiaCV = null;
        String comuneCV = null;
        if(CI_TF_ricercaNomeCV.isVisible()) {
            //System.out.println("Ok1");
            if (cp.testoSempliceSenzaNumeri(CI_TF_ricercaNomeCV, 6, 50)) {
                //System.out.println("Ok2");
                nomeCV = CI_TF_ricercaNomeCV.getText().trim();
                //client.cercaPerNome
            } else {
                CI_TF_ricercaNomeCV.setStyle("-fx-border-color: red");
            }
        } else {
            tipologiaCV = CI_CB_ricercaTipologia.getValue();
            if(cp.testoSempliceSenzaNumeri(CI_TF_ricercaComune, 6, 50)) {
                comuneCV = CI_TF_ricercaComune.getText().trim();
            } else {
                CI_TF_ricercaComune.setStyle("-fx-border-color: red");
            }
        }
        if((comuneCV != null && tipologiaCV != null) || nomeCV != null ) {
            System.out.println(comuneCV + " " + tipologiaCV + " " + nomeCV);
        }
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
}

