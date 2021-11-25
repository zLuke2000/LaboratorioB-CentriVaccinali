package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

public class CIRicercaHomeController extends Controller{

    private ClientCV client;
    private CssHelper css = CssHelper.getInstance();
    private CIDashboardController parent;

    //Se falso vuol dire che non ha fatto nessuna ricerca e che quindi è nell'interffaccia di home,
    //se vero vuol dire che ha fatto almeno una ricerca e cio vuol dire che si ritrova nell'interfaccia
    //della seconda ricerca
    private boolean interfaces = false;

    @FXML private AnchorPane ci_ap_tc;
    @FXML private TextField ci_tf_ricercaNomeCV;
    @FXML private ComboBox<String> ci_cb_sceltaRicerca;
    @FXML private ComboBox<TipologiaCentro> ci_cb_sceltaTipologia;
    @FXML private TextField ci_tf_ricercaComune;

    @FXML void initialize () {
        // Popolo ComboBox con le possibili scelte
        ci_cb_sceltaRicerca.getItems().addAll("Per nome", "Per comune e tipologia");
        ci_cb_sceltaRicerca.getSelectionModel().selectFirst();
        // Popolo ComboBox con i tipi di centri vaccinali
        ci_cb_sceltaTipologia.getItems().addAll(TipologiaCentro.values());
        ci_cb_sceltaTipologia.getSelectionModel().selectFirst();
        ci_tf_ricercaNomeCV.setVisible(true);
    }

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
    }

    public void notifyController(Result result) { }

    public void cambiaRicerca() {
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
        String ricerca = ci_cb_sceltaRicerca.getValue();
        if (ricerca.equals("Per nome")) {
            if(!ci_tf_ricercaNomeCV.getText().isBlank()) {
                client.ricercaPerNome(parent, ci_tf_ricercaNomeCV.getText());
                css.toDefault(ci_tf_ricercaNomeCV);
            } else {
                css.toError(ci_tf_ricercaNomeCV, new Tooltip("Immettere almeno un carattere"));
            }
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


    public void switchIterfaces(boolean interfaces) {
        this.interfaces = interfaces;
    }
}
