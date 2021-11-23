package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class CVChangeController extends Controller {

    @FXML
    public AnchorPane ap_root;

    /**
     * Metodo per avviare l'interfaccia di registrazione di un cittadino vaccinato
     */
    @FXML public void RegistraVaccinato() {
        System.out.println("Interfaccia per registrazione di un vaccinato");
        CentriVaccinali.setRoot("CV_registraVaccinato");
    }

    /**
     * Metodo per avviare l'interfaccia di registrazione di un centro vaccinale
     *
     */
    @FXML public void RegistraCentroVaccinale() {
        System.out.println("Interfaccia per registrazione di centro vaccinale");
        CentriVaccinali.setRoot("CV_registraCentroVaccinale");
    }

    /**
     * Metodo per tornare all'interfaccia precedente
     */
    @FXML public void backTo() {
        CentriVaccinali.setRoot("Avvio");
    }

    @FXML
    void chiudi() {
        super.closeApp();
    }

    @Override
    public void initParameter(ClientCV client, Scene scene) {  }

    @Override
    public void notifyController(Result result) {  }
}
