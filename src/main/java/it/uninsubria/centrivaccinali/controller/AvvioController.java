package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AvvioController {

        @FXML private Button B_accediCittadino;
        @FXML private Button B_accediOperatori;

        @FXML
        void accediCittadino(ActionEvent event) {
                System.out.println("Accesso cittadino");
                //CentriVaccinali.setRoot("CV_cittadino");
        }

        @FXML
        void accediOperatore(ActionEvent event) {
                System.out.println("Accesso operatore");
                CentriVaccinali.setRoot("CV_login");
        }
}
