package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.*;
import it.uninsubria.centrivaccinali.models.*;
import it.uninsubria.centrivaccinali.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.javafx.FontIcon;

public class CIDashboardController {

    private Cittadino CittadinoConesso = null;

    private CssHelper cssh;

    private ClientCV client;

    private ControlloParametri cp = ControlloParametri.getInstance();

    private final ObservableList<String> itemResearch = FXCollections.observableArrayList("Per nome", "Per comune e tipologia");

    private final ObservableList<String> itemCV = FXCollections.observableArrayList("Ospedaliero", "Aziendale" ,"Hub");

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
    private FontIcon CI_FI_dismiss;

    @FXML
    private MenuButton CI_MB_NoCitt;

    @FXML
    private MenuButton CI_MB_SiCitt;

    public void initParameter(ClientCV client) {
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
        cssh = CssHelper.getInstance();
        this.CI_CB_SceltaRicerca.getItems().addAll(itemResearch);
        this.CI_CB_SceltaRicerca.getSelectionModel().selectFirst();
        this.CI_CB_ricercaTipologia.getItems().addAll(itemCV);
        this.CI_CB_ricercaTipologia.getSelectionModel().selectFirst();
    }

    public void cercaCentroVaccinale(ActionEvent actionEvent) {
        if(CI_TF_ricercaNomeCV.isVisible()) {
            if (cp.testoSempliceSenzaNumeri(CI_TF_ricercaNomeCV, 6, 50)) {
                String nomeCV = CI_TF_ricercaNomeCV.getText().trim();
                //client.cercaPerNome
            }
        } else {
            String tipologiaCV = null;
            String comuneCV;
            if(CI_CB_ricercaTipologia.getSelectionModel().getSelectedItem().toString().equals("Ospedaliero")) {
                tipologiaCV = "Ospedaliero";
            } else if(CI_CB_ricercaTipologia.getSelectionModel().getSelectedItem().equals("Hub")) {
                tipologiaCV = "Hub";
                    } else if(CI_CB_ricercaTipologia.getSelectionModel().getSelectedItem().equals("Aziendale")) {
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
        if(actionEvent.getSource().equals(CI_CB_SceltaRicerca)) {
            if(CI_CB_SceltaRicerca.getSelectionModel().getSelectedItem().equals("Per nome")) {
                System.out.println("2");
                CI_FI_dismiss.setVisible(true);
                target_Ricerca.setVisible(false);
                CI_CB_SceltaRicerca.setVisible(false);
                CI_TF_ricercaNomeCV.clear();
                CI_TF_ricercaNomeCV.setVisible(true);
                CI_B_ricerca.setVisible(true);
                CI_CB_ricercaTipologia.setVisible(false);
                CI_TF_ricercaComune.setVisible(false);
            } else if(CI_CB_SceltaRicerca.getSelectionModel().getSelectedItem().equals("Per comune e tipologia")) {
                        System.out.println("3");
                        CI_FI_dismiss.setVisible(true);
                        target_Ricerca.setVisible(false);
                        CI_CB_SceltaRicerca.setVisible(false);
                        CI_TF_ricercaNomeCV.setVisible(false);
                        CI_B_ricerca.setVisible(true);
                        CI_CB_ricercaTipologia.getSelectionModel().selectFirst();
                        CI_CB_ricercaTipologia.setVisible(true);
                        CI_TF_ricercaComune.clear();
                        CI_TF_ricercaComune.setVisible(true);
            }
        }
    }
    public void dismissResearch(MouseEvent mouseEvent) {
        CI_CB_SceltaRicerca.getSelectionModel().selectFirst();
        CI_FI_dismiss.setVisible(false);
        target_Ricerca.setVisible(true);
        CI_CB_SceltaRicerca.setVisible(true);
        CI_TF_ricercaNomeCV.setVisible(false);
        CI_B_ricerca.setVisible(false);
        CI_CB_ricercaTipologia.setVisible(false);
        CI_TF_ricercaComune.setVisible(false);
    }
}
