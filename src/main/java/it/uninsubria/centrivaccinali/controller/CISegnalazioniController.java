package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.scene.Scene;

public class CISegnalazioniController extends Controller{

    private ClientCV client;

    @Override
    public void initParameter(ClientCV client, Scene scene) {
        this.client = client;
    }

    @Override
    public void notifyController(Result result) {

    }
}
