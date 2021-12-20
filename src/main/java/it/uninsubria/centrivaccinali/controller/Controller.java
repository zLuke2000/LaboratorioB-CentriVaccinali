package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.application.Platform;

/**
 * Superclasse astratta per tutti i controller dell'applicazione.
 * @author ...
 */
public abstract class Controller {
    /**
     * Permette di notificare un dato controller, a seguito del completamento di una operazione da parte del server.
     * @param result rappresenta l'operazione appena eseguita.
     */
    public abstract void notifyController(Result result);

    /**
     * Permette la chiusura dell'applicazione.
     */
    public void closeApp() {
        new Thread(() -> {
            CentriVaccinali.client.stopOperation();
            CentriVaccinali.client.disconnetti();
            System.exit(0);
        }).start();
    }
}
