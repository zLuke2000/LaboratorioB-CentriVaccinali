package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 */
public class CVChangeController {

    /**
     * Metodo per avviare l'interfaccia di registrazione di  un cittadino vaccinato
     *
     * @param actionEvent
     */
    public void RegistraVaccinato(ActionEvent actionEvent) {
        System.out.println("Interfaccia per registrazione di un vaccinato");
        CentriVaccinali.setRoot("CV_registraVaccinato");
    }

    /**
     * Metodo per avviare l'interfaccia di registrazione di un centro vaccinale
     *
     * @param actionEvent click sul bottone "Registra centro vaccinale"
     */
    public void RegistraCentroVaccinale(ActionEvent actionEvent) {
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
}
