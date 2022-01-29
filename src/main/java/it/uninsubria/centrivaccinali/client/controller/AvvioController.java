package it.uninsubria.centrivaccinali.client.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.Window;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.fxml.FXML;

/**
 * Controller dell'interafaccia d'avvio dell'applicazione.
 * @author ...
 */
public class AvvioController extends Controller {


    /**
     * Riferimento al client su cui si sta eseguendo l'applicazione.
     * @see ClientCV
     */
    private final ClientCV client = CentriVaccinali.client;


    /**
     * Metodo usato per accedere come operatore cittadino,
     * aprendo l'interfaccia di login.
     */
    @FXML
    private void accediCittadino() {
        if(client != null && client.getUtenteLoggato() != null) {
            Window.setRoot("CI_dashboard");
        } else {
            Window.setRoot("CI_home");
        }
    }


    /**
     * Metodo usato per accedere come operatore sanitario,
     * aprendo l'interfaccia di login.
     */
    @FXML
    private void accediOperatore() {
        Window.setRoot("CV_login");
    }


    //Metodo eridato dalla superclasse
    @Override
    public void notifyController(Result result) { }


    /**
     * Permette la chiusura dell'applicazione tramite la chiamata alla superclasse.
     * @see Controller
     */
    @FXML
    private void chiudiApp() {
        super.closeApp();
    }

}