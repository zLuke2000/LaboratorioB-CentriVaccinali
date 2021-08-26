package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CVChange {

    @FXML private Button B_CV_registracv;
    @FXML private Button B_CV_registracittadino;

    @FXML void registraNuovoVaccinato(ActionEvent event) {
    }

    @FXML void registraNuovoCentro(ActionEvent event) {
        CentriVaccinali.setRoot("CV_registraCentroVaccinale");
    }
}
