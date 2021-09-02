package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AvvioController {

        /**
         * Metodo usato per accedere come operatore cittadino,
         * aprendo l'iterfaccia di login
         * @param event
         */
        @FXML
        void accediCittadino(ActionEvent event) {
                System.out.println("Accesso cittadino");
                CentriVaccinali.setRoot("CI_home");
        }


        /**
         * Metodo usato per accedere come operatore sanitario,
         * aprendo l'iterfaccia di login
         * @param event
         */
        @FXML
        void accediOperatore(ActionEvent event) {
                System.out.println("Accesso operatore");
                CentriVaccinali.setRoot("CV_login");
        }
}
