package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.scene.Scene;

public abstract class Controller {
    public abstract void initParameter(ClientCV client);
    public abstract void notifyController(Result result);
    public void closeApp(ClientCV client) {
        //TODO unexport object
        if(client != null) {
            client.stopOperation();
        }
        System.exit(0);
    }
}
