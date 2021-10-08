package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.*;
import it.uninsubria.centrivaccinali.models.*;
import it.uninsubria.centrivaccinali.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CIDashboardController {

    private Cittadino CittadinoConesso = null;

    private CssHelper cssh;

    private ClientCV client;

    private final ObservableList<String> itemResearch = FXCollections.observableArrayList("Scegli tipo di ricerca", "Per nome", "Per comune e tipologia");

    private final ObservableList<String> itemCV = FXCollections.observableArrayList("Tipologia centro", "Ospedaliero", "Aziendale" ,"Hub");

    @FXML
    private ChoiceBox<String> CI_CB_SceltaRicerca;

    @FXML
    private ComboBox<String> CI_CB_ricercaTipologia;

    @FXML
    private TextField CI_TF_ricercaNomeCV;

    @FXML
    private TextField CI_TF_ricercaComune;

    @FXML
    private Button CI_B_ricerca;

    public void initParameter(ClientCV client) {
        this.client = client;;
    }

    @FXML void initialize() {
        //System.out.println(CI_CB_SceltaRicerca);
        this.CI_CB_SceltaRicerca.getItems().clear();
        //this.CI_CB_SceltaRicerca.getItems().addAll(itemResearch);
        System.out.println(this.CI_CB_SceltaRicerca.getItems());
    }

    public void cercaCentroVaccinale(ActionEvent actionEvent) {
        if(CI_TF_ricercaNomeCV.isVisible()) {
            String nomeCV = CI_TF_ricercaNomeCV.getText().trim();
            if(nomeCV.isEmpty()) {
                cssh.toError(CI_TF_ricercaNomeCV, new Tooltip("Inserisci nome"));
            } else {

            }

        } else {
            String tipologiaCV = null;
            String comuneCV;
            if(CI_CB_ricercaTipologia.getSelectionModel().getSelectedItem().toString().equals("Ospedaliero")) {
                tipologiaCV = "Ospedaliero";
            } else if(CI_CB_ricercaTipologia.getSelectionModel().getSelectedItem().toString().equals("Hub")) {
                tipologiaCV = "Hub";
                    } else if(CI_CB_ricercaTipologia.getSelectionModel().getSelectedItem().toString().equals("Aziendale")) {
                                tipologiaCV = "Aziendale";
                            } else {
                                cssh.toError(CI_CB_ricercaTipologia, new Tooltip("Metti tipologia"));
            }
            comuneCV = CI_TF_ricercaComune.getText().trim();
            if(comuneCV != null && tipologiaCV != null) {

            }
        }
    }


    public void changeResearch(ActionEvent actionEvent) {
        this.CI_CB_SceltaRicerca.getItems().addAll(itemResearch);
        String s = CI_CB_SceltaRicerca.getSelectionModel().getSelectedItem().toString();
        if(s.equals("Scegli tipo di ricerca")) {
            CI_CB_SceltaRicerca.setVisible(false);
            CI_TF_ricercaNomeCV.setVisible(false);
            CI_B_ricerca.setVisible(false);
            CI_CB_ricercaTipologia.setVisible(false);
            CI_TF_ricercaComune.setVisible(false);
            } else if(s.equals("Per nome")){
                        CI_CB_SceltaRicerca.setVisible(false);
                        CI_TF_ricercaNomeCV.setVisible(true);
                        CI_B_ricerca.setVisible(true);
                        CI_CB_ricercaTipologia.setVisible(false);
                        CI_TF_ricercaComune.setVisible(false);
                    } else if(s.equals("Per comune e tipologia")) {
                                CI_CB_SceltaRicerca.setVisible(false);
                                CI_TF_ricercaNomeCV.setVisible(false);
                                CI_B_ricerca.setVisible(true);
                                CI_CB_ricercaTipologia.setVisible(true);
                                CI_TF_ricercaComune.setVisible(true);
                            }
    }
}
