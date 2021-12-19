package it.uninsubria.centrivaccinali.controller.centri;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.enumerator.Qualificatore;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Indirizzo;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;

/**
 *
 */
public class CVRegistraCentroVaccinale extends Controller {
        /**
         *
         */
        @FXML private TextField tf_nome;
        /**
         *
         */
        @FXML private TextField tf_indirizzo;
        /**
         *
         */
        @FXML private TextField tf_civico;
        /**
         *
         */
        @FXML private TextField tf_comune;
        /**
         *
         */
        @FXML private TextField tf_provincia;
        /**
         *
         */
        @FXML private TextField tf_cap;
        /**
         *
         */
        @FXML private ToggleGroup tg_tipologia;
        /**
         *
         */
        @FXML private ComboBox<Qualificatore> cb_qualificatore;
        /**
         *
         */
        private final ControlloParametri cp = ControlloParametri.getInstance();
        /**
         *
         */
        private final ClientCV client = CentriVaccinali.client;

        /**
         *
         */
        @FXML
        private void initialize() {
                cb_qualificatore.getItems().addAll(Qualificatore.values());
                cb_qualificatore.setValue(Qualificatore.VIA);
        }

        /**
         *
         * @param result
         */
        @Override
        public void notifyController(Result result) {
                CentriVaccinali.scene.setCursor(Cursor.DEFAULT);
                if (result.getResult()) {
                        System.out.println("Registrazione effettuata");
                        Platform.runLater(() -> {
                                new DialogHelper("REGISTRAZIONE EFFETTUATA", "Centro vaccinale registrato con successo", DialogHelper.Type.INFO).display();
                                CentriVaccinali.setRoot("CV_registraCentroVaccinale");
                        });
                } else {
                        if(result.getExtendedResult().contains(Result.Error.NOME_IN_USO)) {
                                System.err.println("Registrazione fallita - Centro vaccinale gia' registrato");
                                Platform.runLater(() -> {
                                        DialogHelper dh = new DialogHelper("REGISTRAZIONE FALLITA", "Centro vaccinale gia' registrato", DialogHelper.Type.ERROR);
                                        dh.display();
                                });
                        }
                }
        }

        /**
         *
         * @param ke
         */
        @FXML
        private void realtimeCheck(KeyEvent ke) {
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

        /**
         *
         */
        @FXML
        private void salvaCentro() {
                // Definizione e inizializzazione variabili
                String nomeCentro = tf_nome.getText().trim();
                String nomeIndirizzo = tf_indirizzo.getText().trim();
                String civico = tf_civico.getText().trim();
                String comune = tf_comune.getText().trim();
                String provincia = tf_provincia.getText().trim();
                String cap = tf_cap.getText().trim();

                CentroVaccinale centroVaccinale;
                if(cp.testoSempliceConNumeri(tf_nome, 6, 50) & cp.testoSempliceSenzaNumeri(tf_indirizzo, 3, 50) & cp.numeroCivico(tf_civico) & cp.testoSempliceSenzaNumeri(tf_comune, 3, 50) & cp.provincia(tf_provincia) & cp.numeri(tf_cap, 5, 5)) {
                        TipologiaCentro tipologiaSelezionata = TipologiaCentro.getValue(((RadioButton) tg_tipologia.getSelectedToggle()).getText());
                        centroVaccinale = new CentroVaccinale(nomeCentro, new Indirizzo(cb_qualificatore.getValue(), nomeIndirizzo, civico, comune, provincia, Integer.parseInt(cap)), tipologiaSelezionata);
                        CentriVaccinali.scene.setCursor(Cursor.WAIT);
                        client.registraCentroVaccinale(this, centroVaccinale);
                }
        }

        /**
         *
         */
        @FXML
        private void backTo() {
                CentriVaccinali.setRoot("CV_home");
                client.stopOperation();
        }

        /**
         *
         */
        @FXML
        private void chiudiApp() {
                super.closeApp();
        }
}