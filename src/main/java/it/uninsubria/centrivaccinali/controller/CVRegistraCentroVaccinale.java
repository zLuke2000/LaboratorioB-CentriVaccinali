package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.enumerator.Qualificatore;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Indirizzo;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

public class CVRegistraCentroVaccinale extends Controller {

        @FXML public GridPane gp_root;
        @FXML private TextField tf_nome;
        @FXML private ChoiceBox<Qualificatore> cb_qualificatore;
        @FXML private TextField tf_indirizzo;
        @FXML private TextField tf_civico;
        @FXML private TextField tf_comune;
        @FXML private TextField tf_provincia;
        @FXML private TextField tf_cap;
        @FXML private ToggleGroup tg_tipologia;
        @FXML private RadioButton rb_aziendale;
        @FXML private RadioButton rb_ospedaliero;
        @FXML private RadioButton rb_hub;

        private final ControlloParametri cp = ControlloParametri.getInstance();
        private ClientCV client;

        /**
         *
         */
        @FXML public void initialize() {
                cb_qualificatore.getItems().addAll(Qualificatore.values());
                cb_qualificatore.setValue(Qualificatore.VIA);
        }

        @Override
        public void initParameter(ClientCV client) {
                this.client = client;
        }

        @Override
        public void notifyController(Result result) {
                if (result.getResult()) {
                        System.out.println("Registrazione effettuata");
                } else {
                        if(result.getExtendedResult().contains(Result.Error.NOME_IN_USO)) {
                                System.err.println("Registrazione fallita - Centro vaccinale gia' registrato");
                                DialogHelper dh = new DialogHelper("REGISTRAZIONE FALLITA", "Centro vaccinale gia' registrato", DialogHelper.Type.ERROR);
                                dh.display(null);
                        }
                }
        }

        @FXML void realtimeCheck(KeyEvent ke) {
                if(ke.getSource().equals(tf_nome)) {
                        cp.testoSempliceConNumeri(tf_nome, 6, 50);
                } else if(ke.getSource().equals(tf_indirizzo)) {
                        cp.testoSempliceSenzaNumeri(tf_indirizzo, 3, 50);
                } else if(ke.getSource().equals(tf_civico)) {
                        cp.numeroCivico(tf_civico);
                } else if(ke.getSource().equals(tf_comune)) {
                        cp.testoSempliceSenzaNumeri(tf_comune, 3, 50);
                } else if(ke.getSource().equals(tf_provincia)) {
                        cp.provincia(tf_provincia);
                } else if(ke.getSource().equals(tf_cap)) {
                        cp.numeri(tf_cap, 5, 5);
                } else {
                        System.err.println("[RegistraCV] EXCEPTION realtimeCheck");
                }
        }

        @FXML void salvaCentro() {
                // Definizione e inizializzazione variabili
                String nomeCentro = tf_nome.getText().trim();
                String nomeIndirizzo = tf_indirizzo.getText().trim();
                String civico = tf_civico.getText().trim();
                String comune = tf_comune.getText().trim();
                String provincia = tf_provincia.getText().trim();
                String cap = tf_cap.getText().trim();

                CentroVaccinale centroVaccinale;
                if(cp.testoSempliceConNumeri(tf_nome, 6, 50) & cp.testoSempliceSenzaNumeri(tf_indirizzo, 3, 50) & cp.numeroCivico(tf_civico) & cp.testoSempliceSenzaNumeri(tf_comune, 3, 50) & cp.provincia(tf_provincia) & cp.numeri(tf_cap, 5, 5)) {
                        TipologiaCentro tipologiaSelezionata;
                        if(rb_ospedaliero.isSelected()) {
                                tipologiaSelezionata = TipologiaCentro.OSPEDALIERO;
                        } else if(rb_aziendale.isSelected()) {
                                tipologiaSelezionata = TipologiaCentro.AZIENDALE;
                        } else {
                                tipologiaSelezionata = TipologiaCentro.HUB;
                        }
                        centroVaccinale = new CentroVaccinale(nomeCentro, new Indirizzo(cb_qualificatore.getValue(), nomeIndirizzo, civico, comune, provincia, Integer.parseInt(cap)), tipologiaSelezionata);
                        client.registraCentroVaccinale(this, centroVaccinale);
                }
        }

        @FXML public void backTo() {
                CentriVaccinali.setRoot("CV_change");
                client.stopOperation();
        }
}