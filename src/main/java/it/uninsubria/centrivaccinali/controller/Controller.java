package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.application.Platform;

/**
 *
 */
public abstract class Controller {
    /**
     *
     * @param result
     */
    public abstract void notifyController(Result result);

    /**
     *
     */
    public void closeApp() {
        new Thread(() -> {
            CentriVaccinali.client.stopOperation();
            CentriVaccinali.client.disconnetti();
            System.exit(0);
        }).start();
    }
}
