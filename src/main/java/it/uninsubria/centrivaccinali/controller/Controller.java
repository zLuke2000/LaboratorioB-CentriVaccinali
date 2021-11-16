package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;

public abstract class Controller {
    public abstract void initParameter(ClientCV client);
    public abstract void notifyController(Result result);
    public void closeApp() {
        //TODO unexport objects
        System.exit(0);
    }
}
