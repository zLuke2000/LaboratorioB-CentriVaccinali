package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.fxml.FXML;

public class AvvioController extends Controller {

        /**
         * Metodo usato per accedere come operatore cittadino,
         * aprendo l'interfaccia di login
         */
        @FXML
        void accediCittadino() {
                CentriVaccinali.setRoot("CI_home");
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
        public void initParameter(ClientCV client) {  }

        @Override
        public void notifyController(Result result) {  }
}
