package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.enumerator.Qualificatore;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Indirizzo;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class CVRegistraCentroVaccinale extends Controller {

        @FXML private TextField TF_nome;
        @FXML private ChoiceBox<Qualificatore> CB_qualificatore;
        @FXML private TextField TF_indirizzo;
        @FXML private TextField TF_civico;
        @FXML private TextField TF_comune;
        @FXML private TextField TF_provincia;
        @FXML private TextField TF_cap;
        @FXML private ToggleGroup TG_tipologia;
        @FXML private RadioButton RB_aziendale;
        @FXML private RadioButton RB_ospedaliero;
        @FXML private RadioButton RB_hub;

        private final ControlloParametri cp = ControlloParametri.getInstance();
        private ClientCV client;

        /**
         *
         */
        @FXML public void initialize() {
                CB_qualificatore.getItems().addAll(Qualificatore.values());
                CB_qualificatore.setValue(Qualificatore.VIA);
        }

        @Override
        public void initParameter(ClientCV client) {
                this.client = client;
        }

        @Override
        public void notifyController(Result result) {
                if (result.getResult()) {
                        System.out.println("Registrazione effettuata");
                }
                //TODO mostrare errori
        }

        @FXML void realtimeCheck(KeyEvent ke) {
                //TODO controllo realtime della provincia
                if(ke.getSource().equals(TF_nome)) {
                        cp.testoSempliceConNumeri(TF_nome, 6, 50);
                } else if(ke.getSource().equals(TF_indirizzo)) {
                        cp.testoSempliceSenzaNumeri(TF_indirizzo, 3, 50);
                } else if(ke.getSource().equals(TF_civico)) {
                        cp.numeroCivico(TF_civico);
                } else if(ke.getSource().equals(TF_comune)) {
                        cp.testoSempliceSenzaNumeri(TF_comune, 3, 50);
                } else if(ke.getSource().equals(TF_provincia)) {
                        cp.provincia(TF_provincia);
                } else if(ke.getSource().equals(TF_cap)) {
                        cp.numeri(TF_cap, 5, 5);
                } else {
                        System.err.println("[RegistraCV] EXCEPTION realtimeCheck");
                }
        }

        @FXML void salvaCentro() {
                // Definizione e inizializzazione variabili
                String nomeCentro = TF_nome.getText().trim();
                String nomeIndirizzo = TF_indirizzo.getText().trim();
                String civico = TF_civico.getText().trim();
                String comune = TF_comune.getText().trim();
                String provincia = TF_provincia.getText().trim();
                String cap = TF_cap.getText().trim();

                System.out.println("1");

                CentroVaccinale centroVaccinale;
                if(cp.testoSempliceConNumeri(TF_nome, 6, 50) & cp.testoSempliceSenzaNumeri(TF_indirizzo, 3, 50) & cp.numeroCivico(TF_civico) & cp.testoSempliceSenzaNumeri(TF_comune, 3, 50) & cp.provincia(TF_provincia) & cp.numeri(TF_cap, 5, 5)) {
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
                        client.registraCentroVaccinale(this, centroVaccinale);
                        // TODO da separare gli errori possibili
                }
        }

        public void backTo(MouseEvent mouseEvent) {
                CentriVaccinali.setRoot("CV_change");
                client.stopOperation();
        }
}