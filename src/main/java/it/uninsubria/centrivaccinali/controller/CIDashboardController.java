package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.*;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.*;
import it.uninsubria.centrivaccinali.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class CIDashboardController extends Controller {

    private Cittadino cittadinoConesso = null;

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

    @FXML void initialize() {
        this.CI_CB_SceltaRicerca.getItems().addAll(itemResearch);
        this.CI_CB_SceltaRicerca.getSelectionModel().selectFirst();
        this.CI_CB_ricercaTipologia.getItems().addAll(itemCV);
        this.CI_CB_ricercaTipologia.getSelectionModel().selectFirst();
    }

    @Override
    public void initParameter(ClientCV client) {
        this.client =  client;
        this.cittadinoConesso = client.getUtenteLoggato();
        if(cittadinoConesso != null) {
            System.out.println(cittadinoConesso.getUserid());
            CI_MB_SiCitt.setVisible(true);
            CI_MB_NoCitt.setVisible(false);
            CI_MB_SiCitt.setText(cittadinoConesso.getUserid());
        } else {
            System.out.println("No");
            CI_MB_SiCitt.setVisible(false);
            CI_MB_NoCitt.setVisible(true);
        }
    }

    //TODO notifica della ricerca
    @Override
    public void notifyController(Result result) {
        if (!result.getResultCentri().isEmpty()) {
            for (CentroVaccinale cv: result.getResultCentri()) {
                System.out.println(cv);
            }
        }
        else {
            System.err.println("nessun risultato");
        }
    }

    public void cercaCentroVaccinale(ActionEvent actionEvent) {
        if(CI_TF_ricercaNomeCV.isVisible()) {
            if (cp.testoSempliceSenzaNumeri(CI_TF_ricercaNomeCV, 6, 50)) {
                String nomeCV = CI_TF_ricercaNomeCV.getText().trim();
                client.ricercaPerNome(this, nomeCV);
            }
        } else {
            if(cp.testoSempliceSenzaNumeri(CI_TF_ricercaComune,3, 50)) {
                String comuneCV = CI_TF_ricercaComune.getText().trim();
                TipologiaCentro tipologiaCV = TipologiaCentro.valueOf(CI_CB_ricercaTipologia.getValue().toUpperCase());
                client.ricercaPerComuneTipologia(this, comuneCV, tipologiaCV);
            }
        }
    }
    //TODO ritorna la lista dei centri trovati
    public void notifyResearch() {

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

