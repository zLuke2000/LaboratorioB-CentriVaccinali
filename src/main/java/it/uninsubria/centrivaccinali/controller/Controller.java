package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.scene.Scene;

public abstract class Controller {
    public abstract void initParameter(ClientCV client, Scene scene);
    public abstract void notifyController(Result result);
    public void closeApp() {
        //TODO unexport objects
        System.exit(0);
    }
}
