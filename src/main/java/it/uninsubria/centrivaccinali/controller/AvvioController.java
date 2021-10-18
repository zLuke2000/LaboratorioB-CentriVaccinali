package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AvvioController extends Controller {

        /**
         * Metodo usato per accedere come operatore cittadino,
         * aprendo l'iterfaccia di login
         * @param event
         */
        @FXML
        void accediCittadino(ActionEvent event) {
                CentriVaccinali.setRoot("CI_home");
        }

        /**
         * Metodo usato per accedere come operatore sanitario,
         * aprendo l'iterfaccia di login
         * @param event
         */
        @FXML
        void accediOperatore(ActionEvent event) {
                CentriVaccinali.setRoot("CV_login");
        }

        @Override
        public void initParameter(ClientCV client) {  }

        @Override
        public void notifyController(Result result) {  }
}
