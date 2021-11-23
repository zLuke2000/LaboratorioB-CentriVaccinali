package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CIRicercaHomeController extends Controller{

    private ClientCV client;
    private ControlloParametri cp = ControlloParametri.getInstance();

    @FXML
    private TextField ci_tf_ricercaNomeCV;

    @FXML
    private ComboBox ci_cb_sceltaRicerca;

    @FXML
    private ComboBox ci_cb_sceltaTipologia;

    @FXML
    private TextField ci_tf_ricercaComune;

    @FXML void initialize () {
        this.ci_cb_sceltaRicerca.getItems().addAll("Per nome", "Per comune e tipologia");
        this.ci_cb_sceltaRicerca.getSelectionModel().selectFirst();
        this.ci_cb_sceltaTipologia.getItems().addAll(TipologiaCentro.values());
        this.ci_cb_sceltaTipologia.getSelectionModel().selectFirst();
    }

    @Override
    public void initParameter(ClientCV client, Scene scene) {
        this.client = client;
    }

    public void notifyController(Result result) {

    }

    public void cambiaRicerca() {
        if(ci_cb_sceltaRicerca.getSelectionModel().getSelectedItem().equals("Per nome")) {
            ci_tf_ricercaNomeCV.clear();
            ci_tf_ricercaNomeCV.setVisible(true);
            //CI_B_ricerca.setVisible(true);
            ci_cb_sceltaTipologia.setVisible(false);
            ci_tf_ricercaComune.setVisible(false);
        } else if(ci_cb_sceltaRicerca.getSelectionModel().getSelectedItem().equals("Per comune e tipologia")) {
            ci_tf_ricercaNomeCV.setVisible(false);
            //CI_B_ricerca.setVisible(true);
            ci_cb_sceltaTipologia.getSelectionModel().selectFirst();
            ci_cb_sceltaTipologia.setVisible(true);
            ci_tf_ricercaComune.clear();
            ci_tf_ricercaComune.setVisible(true);
        }
    }

    @FXML
    public void cercaCentroVaccinale() {
        //TODO (idea) chiamare qua la ricerca dei centri vaccinali, e notifivare la dashboard
        // con la notifyStatus facendo i divuti cambiamenti, così da non doverci preoccupare di
        // dati e controller da portare in giro ( facendo fiventare il controller della dashboard
        // un singleton )

//        if(ci_cb_sceltaRicerca.getSelectionModel().getSelectedItem().equals("Per nome")) {
//            String nome= ci_tf_ricercaNomeCV.getText().trim();
//        } else {
//            //....
//        }
        parentController.prova();
    }

    //TODO metodo per chiudere applicazione

    //TODO metodo per tornare indietro


    public void switchIterfaces(boolean interfaces) {
        this.interfaces = interfaces;
    }
}
