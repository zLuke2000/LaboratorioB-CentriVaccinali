package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.enumerator.Qualificatore;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Indirizzo;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistraCV {

        @FXML private Label L_titolo;
        @FXML private TextField TF_nome;
        @FXML private Label L_infoIndirizzo;
        @FXML private ChoiceBox<Qualificatore> CB_qualificatore;
        @FXML private TextField TF_indirizzo;
        @FXML private TextField TF_civico;
        @FXML private TextField TF_comune;
        @FXML private TextField TF_provincia;
        @FXML private TextField TF_cap;
        @FXML private Label L_infoIndirizzo1;
        @FXML private RadioButton RB_Ospedaliero;
        @FXML private ToggleGroup TG_tipologia;
        @FXML private RadioButton RB_aziendale;
        @FXML private RadioButton RB_hub;
        @FXML private Button B_indietro;
        @FXML private Button B_conferma;

        private final CssHelper cssHelper = CssHelper.getInstance();

        @FXML public void initialize() {
                CB_qualificatore.getItems().addAll(Qualificatore.values());
                CB_qualificatore.setValue(Qualificatore.VIA);
        }

        @FXML public void realtimeCheck(KeyEvent ke) {
                Pattern rPattern;
                Matcher rMatcher;
                if(ke.getSource().equals(TF_nome)) {
                        rPattern = Pattern.compile("[\\D]{6,50}");
                        rMatcher = rPattern.matcher(TF_nome.getText().trim());
                        if(rMatcher.matches()) {
                                cssHelper.toValid(TF_nome);
                        } else {
                                cssHelper.toError(TF_nome, new Tooltip("immettere da 6 a 50 caratteri"));
                        }
                } else if(ke.getSource().equals(TF_indirizzo)) {
                        rPattern = Pattern.compile("[\\D]{3,50}");
                        rMatcher = rPattern.matcher(TF_indirizzo.getText().trim());
                        if(rMatcher.matches()) {
                                cssHelper.toValid(TF_indirizzo);
                        } else {
                                cssHelper.toError(TF_indirizzo, new Tooltip("immettere da 3 a 50 caratteri"));
                        }
                } else if(ke.getSource().equals(TF_civico)) {
                        // Controllo numero civico (minimo UN numero ed eventualmente UNA lettera alla fine)
                        if(TF_civico.getText().trim().length() > 0) {
                                // Converto il numero in intero per controllare se composto da solo numeri
                                try {
                                        Integer.parseInt(TF_civico.getText());
                                        cssHelper.toValid(TF_civico);
                                } catch (NumberFormatException nfe1) {
                                        // Rimuovo Ultima lettera e controllo se composto da solo numeri
                                        try {
                                                Integer.parseInt(TF_civico.getText().replaceAll(".$", ""));
                                                cssHelper.toValid(TF_civico);
                                        } catch (NumberFormatException nfe2) {
                                                cssHelper.toError(TF_civico, new Tooltip("Consentita solo una lettera alla fine"));
                                        }
                                }
                        } else {
                                cssHelper.toError(TF_civico, new Tooltip("Inserire almeno UN numero"));
                        }
                } else if(ke.getSource().equals(TF_comune)) {
                        rPattern = Pattern.compile("[\\D]{3,50}");
                        rMatcher = rPattern.matcher(TF_comune.getText().trim());
                        if(rMatcher.matches()) {
                                cssHelper.toValid(TF_comune);
                        } else {
                                cssHelper.toError(TF_comune, new Tooltip("immettere da 3 a 50 caratteri"));
                        }
                } else if(ke.getSource().equals(TF_provincia)) {
                        rPattern = Pattern.compile("[A-Z]{2}");
                        rMatcher = rPattern.matcher(TF_provincia.getText().trim());
                        if(rMatcher.matches()) {
                                cssHelper.toValid(TF_provincia);
                        } else {
                                cssHelper.toError(TF_provincia, new Tooltip("solo 2 lettere maiuscole ammesse"));
                        }
                } else if(ke.getSource().equals(TF_cap)) {
                        rPattern = Pattern.compile("[\\d]{5}");
                        rMatcher = rPattern.matcher(TF_cap.getText().trim());
                        if(rMatcher.matches()) {
                                cssHelper.toValid(TF_cap);
                        } else {
                                cssHelper.toError(TF_cap, new Tooltip("immettere 5 numeri"));
                        }
                } else {
                        System.err.println("[RegistraCV] EXCEPTION realtimeCheck");
                }
        }

        @FXML public void salvaCentro() {
                // Definizione e inizializzazione variabili
                String nomeCentro = TF_nome.getText().trim();
                String nomeIndirizzo = TF_indirizzo.getText().trim();
                String civico = TF_civico.getText().trim();
                String comune = TF_comune.getText().trim();
                String provincia = TF_provincia.getText().trim();
                String cap = TF_cap.getText().trim();
                int capInt;

                CentroVaccinale centroVaccinale = new CentroVaccinale();
                boolean check = true;
        }

    public void backTo(MouseEvent mouseEvent) {

    }
}