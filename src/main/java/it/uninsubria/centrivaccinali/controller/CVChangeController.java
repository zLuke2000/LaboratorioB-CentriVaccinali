package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.fxml.FXML;

public class CVChangeController extends Controller {

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
     *
     * @param mouseEvent click sull'icona per tornare indietro
     */
    public void BackTo(MouseEvent mouseEvent) {
        System.out.printf("Indietro");
        CentriVaccinali.setRoot("Avvio");
    }

    @Override
    public void initParameter(ClientCV client) {  }

    @Override
    public void notifyController(Result result) {  }
}
