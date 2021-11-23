package it.uninsubria.centrivaccinali.controller;

import com.jfoenix.controls.JFXComboBox;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.javafx.FontIcon;

public class CIRicercaResultController extends Controller{


    @FXML void initialize () {
    }

    @Override
    public void initParameter(ClientCV client) {

    }

    @Override
    public void initParameter(ClientCV client, Scene scene) {
        this.client = client;
    }

    @Override
    public void notifyController(Result result) {

    }
}
