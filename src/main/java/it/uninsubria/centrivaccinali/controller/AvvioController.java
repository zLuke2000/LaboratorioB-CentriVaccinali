package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class AvvioController extends Controller {

    @FXML public AnchorPane ap_root;
    private ClientCV client = CentriVaccinali.client;

    /**
     * Metodo usato per accedere come operatore cittadino,
     * aprendo l'interfaccia di login
     */
    @FXML
    void accediCittadino() {
        if(client != null && client.getUtenteLoggato() != null) {
            CentriVaccinali.setRoot("CI_dashboard");
        } else {
            CentriVaccinali.setRoot("CI_home");
        }
    }

    /**
     * Metodo usato per accedere come operatore sanitario,
     * aprendo l'interfaccia di login
     */
    @FXML
    void accediOperatore() {
        CentriVaccinali.setRoot("CV_login");
    }


    @Override
    public void notifyController(Result result) { }

    @FXML
    void chiudi() {
        super.closeApp(null);
    }
    
}
