package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Cittadino;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CIDashboardController {

    private Cittadino CittadinoConesso = null;

    private ClientCV client;

    private final ObservableList<String> itemResearch = FXCollections.observableArrayList("Scegli tipo di ricerca", "Per nome", "Per comune e tipologia");

    private final ObservableList<String> itemCV = FXCollections.observableArrayList("Tipologia centro", "Ospedaliero", "Aziendale" ,"Hub");

    @FXML
    private ComboBox<String> CI_CB_SceltaRicerca;

    @FXML
    private ComboBox<String> CI_CB_ricercaTipologia;

    @FXML
    private TextField CI_TF_ricercaNomeCV;

    @FXML
    private TextField CI_TF_ricercaComune;

    @FXML
    private Button CI_B_ricerca;

    public void initParameter(ClientCV client) {
        this.client = client;
        this.CI_CB_SceltaRicerca.setItems(itemResearch);
        this.CI_CB_ricercaTipologia.setItems(itemCV);
    }

    public void cercaCentroVaccinale(ActionEvent actionEvent) {

    }


    public void changeResearch(ActionEvent actionEvent) {
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
