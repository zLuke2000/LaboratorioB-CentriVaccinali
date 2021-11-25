package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class CIRicercaHomeController extends Controller{

    private ClientCV client;
    private ControlloParametri cp = ControlloParametri.getInstance();
    private CIDashboardController parent;

    @FXML
    private AnchorPane ci_ap_tc;

    @FXML
    private AnchorPane ci_ap_tcmini;

    @FXML
    private TextField ci_tf_ricercaNomeCV;

    @FXML
    private TextField ci_tf_ricercaNomeCVmini;

    @FXML
    private ComboBox ci_cb_sceltaRicerca;

    @FXML
    private ComboBox ci_cb_sceltaTipologia;

    @FXML
    private ComboBox ci_cb_sceltaTipologiamini;

    @FXML
    private TextField ci_tf_ricercaComune;

    @FXML
    private TextField ci_tf_ricercaComunemini;

    @FXML
    private ComboBox ci_cb_sceltaRicercamini;

    @FXML void initialize () {
        this.ci_cb_sceltaRicerca.getItems().addAll("Per nome", "Per comune e tipologia");
        this.ci_cb_sceltaRicerca.getSelectionModel().selectFirst();
        //ci_tf_ricercaNomeCV.setVisible(true);
        this.ci_cb_sceltaTipologia.getItems().addAll(TipologiaCentro.values());
        this.ci_cb_sceltaTipologia.getSelectionModel().selectFirst();
    }

    @Override
    public void initParameter(ClientCV client, Scene scene) {
        this.client = client;
    }

    public void notifyController(Result result) {

    }

    public void cambiaRicerca(Event e) {
        if (ci_cb_sceltaRicerca.getSelectionModel().getSelectedItem().equals("Per nome")) {
            ci_tf_ricercaNomeCV.clear();
            ci_tf_ricercaNomeCV.setVisible(true);
            ci_ap_tc.setVisible(false);
        } else if (ci_cb_sceltaRicerca.getSelectionModel().getSelectedItem().equals("Per comune e tipologia")) {
            ci_tf_ricercaNomeCV.setVisible(false);
            ci_cb_sceltaTipologia.getSelectionModel().selectFirst();
            ci_tf_ricercaComune.clear();
            ci_ap_tc.setVisible(true);
        }
    }

    @FXML
    public void cercaCentroVaccinale() {
        String ricerca = (String) ci_cb_sceltaRicerca.getValue();
        if (ricerca.equals("Per nome") && !ci_tf_ricercaNomeCV.getText().isBlank()) {
            System.out.println("Ric: " + client);
            System.out.println(parent + ci_tf_ricercaNomeCV.getText());
            client.ricercaPerNome(parent, ci_tf_ricercaNomeCV.getText());
        }
        else if (ricerca.equals("Per comune e tipologia") && !ci_tf_ricercaComune.getText().isBlank()) {
            client.ricercaPerComuneTipologia(parent, ci_tf_ricercaComune.getText(),
                    TipologiaCentro.getValue((String) ci_cb_sceltaTipologia.getValue()));
        }

        //TODO (idea) chiamare qua la ricerca dei centri vaccinali, e notifivare la dashboard
        // con la notifyStatus facendo i divuti cambiamenti, così da non doverci preoccupare di
        // dati e controller da portare in giro ( facendo fiventare il controller della dashboard
        // un singleton )

    }

    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }

    //TODO metodo per chiudere applicazione

    //TODO metodo per tornare indietro


}
