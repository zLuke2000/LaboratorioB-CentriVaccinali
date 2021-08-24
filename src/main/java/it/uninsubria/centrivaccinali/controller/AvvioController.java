package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AvvioController {

        @FXML private Button B_accediCittadino; // Value injected by FXMLLoader
        @FXML private Button B_accediOperatori; // Value injected by FXMLLoader

        @FXML
        void accediCittadino(ActionEvent event) {
                System.out.println("Accesso cittadino");
                CentriVaccinali.setRoot("CV_cittadino");
        }

        @FXML
        void AccediOperatori(ActionEvent event) {
                System.out.println("Accesso operatore");
                CentriVaccinali.setRoot("CV_login");
        }
}
