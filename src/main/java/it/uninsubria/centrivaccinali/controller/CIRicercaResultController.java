package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class CIRicercaResultController extends Controller{

    private ClientCV client;

    private CIDashboardController parent;

    @FXML
    public VBox vb_risultati;

    @FXML
    private TextField ci_tf_ricercaNomeCV;

    @FXML
    private ComboBox ci_cb_sceltaRicerca;

    @FXML
    private AnchorPane ci_ap_tc;

    @FXML
    private ComboBox ci_cb_sceltaTipologia;

    @FXML
    private TextField ci_tf_ricercaComune;


    @FXML void initialize () {
        this.ci_cb_sceltaRicerca.getItems().addAll("Per nome", "Per comune e tipologia");
        this.ci_cb_sceltaRicerca.getSelectionModel().selectFirst();
        //ci_tf_ricercaNomeCV.setVisible(true);
        this.ci_cb_sceltaTipologia.getItems().addAll(TipologiaCentro.values());
        this.ci_cb_sceltaTipologia.getSelectionModel().selectFirst();
        Label lbl = new Label();
        lbl.setText("Nessun risultato");
        vb_risultati.getChildren().add(lbl);
    }

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
    }

    @Override
    public void notifyController(Result result) { }

    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }

    @FXML
    public void cercaCentroVaccinale() {
        if (ci_cb_sceltaRicerca.getValue().equals("Per nome") && !ci_tf_ricercaNomeCV.getText().isBlank()) {
            client.ricercaPerNome(parent, ci_tf_ricercaNomeCV.getText());
        }
        else if (ci_cb_sceltaRicerca.getValue().equals("Per comune e tipologia") && !ci_tf_ricercaComune.getText().isBlank()) {
            client.ricercaPerComuneTipologia(parent, ci_tf_ricercaComune.getText(),
                    TipologiaCentro.getValue((String) ci_cb_sceltaTipologia.getValue()));
        }
    }

    public void setData(List<CentroVaccinale> list) {
        Platform.runLater(() -> {
            vb_risultati.getChildren().clear();
            if (list.isEmpty()) {
                Label lbl = new Label();
                lbl.setText("Nessun risultato");
                vb_risultati.getChildren().add(lbl);
            }
            else {
                for (CentroVaccinale cv : list) {
                    FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/ItemList.fxml"));
                    try {
                        HBox item = fxmlLoader.load();
                        CIItemListController itemController = fxmlLoader.getController();
                        itemController.setData(cv);
                        vb_risultati.getChildren().add(item);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @FXML
    private void cambiaRicerca() {
        //Object e = ci_cb_sceltaRicerca.getSelectionModel().getSelectedItem();
        if (ci_cb_sceltaRicerca.getValue().equals("Per nome")) {
            ci_tf_ricercaNomeCV.clear();
            ci_tf_ricercaNomeCV.setVisible(true);
            ci_ap_tc.setVisible(false);
        } else if (ci_cb_sceltaRicerca.getValue().equals("Per comune e tipologia")) {
            ci_tf_ricercaNomeCV.setVisible(false);
            ci_cb_sceltaTipologia.getSelectionModel().selectFirst();
            ci_tf_ricercaComune.clear();
            ci_ap_tc.setVisible(true);
        }
    }
}
