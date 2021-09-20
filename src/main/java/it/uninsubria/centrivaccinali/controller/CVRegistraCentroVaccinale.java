package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.enumerator.Qualificatore;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Indirizzo;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

public class CVRegistraCentroVaccinale {

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
        @FXML private ToggleGroup TG_tipologia;
        @FXML private RadioButton RB_aziendale;
        @FXML private RadioButton RB_ospedaliero;
        @FXML private RadioButton RB_hub;
        @FXML private Button B_indietro;
        @FXML private Button B_conferma;

        private ControlloParametri cp = new ControlloParametri();
        private ClientCV client;

        @FXML public void initialize() {
                CB_qualificatore.getItems().addAll(Qualificatore.values());
                CB_qualificatore.setValue(Qualificatore.VIA);
        }

        public void initParameter(ClientCV client) {
                this.client = client;
        }

        @FXML public void realtimeCheck(KeyEvent ke) {
                if(ke.getSource().equals(TF_nome)) {
                        cp.testoSemplice(TF_nome, 6, 50);
                } else if(ke.getSource().equals(TF_indirizzo)) {
                        cp.testoSemplice(TF_indirizzo, 3, 50);
                } else if(ke.getSource().equals(TF_civico)) {
                        cp.numeroCivico(TF_civico);
                } else if(ke.getSource().equals(TF_comune)) {
                        cp.testoSemplice(TF_comune, 3, 50);
                } else if(ke.getSource().equals(TF_provincia)) {
                        cp.provincia(TF_provincia);
                } else if(ke.getSource().equals(TF_cap)) {
                        cp.numeri(TF_cap, 5, 5);
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

                System.out.println("1");

                CentroVaccinale centroVaccinale;
                if(cp.testoSemplice(TF_nome, 6, 50) & cp.testoSemplice(TF_indirizzo, 3, 50) & cp.numeroCivico(TF_civico) & cp.testoSemplice(TF_comune, 3, 50) & cp.provincia(TF_provincia) & cp.numeri(TF_cap, 5, 5)) {
                        System.out.println("2");
                        TipologiaCentro tipologiaSelezionata;
                        if(RB_ospedaliero.isSelected()) {
                                tipologiaSelezionata = TipologiaCentro.OSPEDALIERO;
                        } else if(RB_aziendale.isSelected()) {
                                tipologiaSelezionata = TipologiaCentro.AZIENDALE;
                        } else {
                                tipologiaSelezionata = TipologiaCentro.HUB;
                        }
                        centroVaccinale = new CentroVaccinale(nomeCentro, new Indirizzo(CB_qualificatore.getValue(), nomeIndirizzo, civico, comune, provincia, Integer.parseInt(cap)), tipologiaSelezionata);
                        if(client.registraCentroVaccinale(centroVaccinale)) {
                                System.out.println("REGISTRAZIONE EFFETTUATA");
                        } else {
                                System.out.println("3");
                        }
                }
        }
}