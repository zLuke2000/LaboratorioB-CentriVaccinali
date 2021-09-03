package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

public class RegistraCV {

        @FXML private Label L_titolo;
        @FXML private TextField TF_nome;
        @FXML private Label L_infoIndirizzo;
        @FXML private ChoiceBox<TipologiaCentro> CB_qualificatore;
        @FXML private TextField TF_indirizzo;
        @FXML private TextField TF_civico;
        @FXML private TextField TF_comune;
        @FXML private TextField TF_comune1;
        @FXML private TextField TF_comune2;
        @FXML private Label L_infoIndirizzo1;
        @FXML private RadioButton RB_Ospedaliero;
        @FXML private ToggleGroup TG_tipologia;
        @FXML private RadioButton RB_aziendale;
        @FXML private RadioButton RB_hub;
        @FXML private Button B_indietro;
        @FXML private Button B_conferma;

        @FXML public void initialize() {
            CB_qualificatore.getItems().addAll(TipologiaCentro.OSPEDALIERO, TipologiaCentro.AZIENDALE, TipologiaCentro.HUB);
        }

    public void backTo(MouseEvent mouseEvent) {

    }
}