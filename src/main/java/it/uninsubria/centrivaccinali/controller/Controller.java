package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;

public abstract class Controller {

    public abstract void notifyController(Result result);

    public void closeApp() {
        CentriVaccinali.client.stopOperation();
        CentriVaccinali.client.disconnetti();
        System.exit(0);
    }
}
