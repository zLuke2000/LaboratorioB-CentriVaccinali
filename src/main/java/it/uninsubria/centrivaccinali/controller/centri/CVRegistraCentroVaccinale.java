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
 * Controller per l'interfaccia di registrazione di nuovi centri vaccinali.
 * @author ...
 */
public class CVRegistraCentroVaccinale extends Controller {
        /**
         * Riferimento alla <code>TextField</code> in cui inserire il nome del nuovo centro.
         */
        @FXML private TextField tf_nome;


        /**
         * Riferimeto al <code>ComboBox</code> che permette di selezionare il qualificatore per l'indirizzo del nuovo centro.
         * @see Qualificatore
         */
        @FXML private ComboBox<Qualificatore> cb_qualificatore;


        /**
         * Riferimento alla <code>TextField</code> in cui inserire l'indirizzo del nuovo centro.
         */
        @FXML private TextField tf_indirizzo;


        /**
         * Riferimento alla <code>TextField</code> in cui inserire il civico del nuovo centro.
         */
        @FXML private TextField tf_civico;


        /**
         * Riferimento alla <code>TextField</code> in cui inserire il comune del nuovo centro.
         */
        @FXML private TextField tf_comune;


        /**
         * Riferimento alla <code>TextField</code> in cui inserire la provincia del nuovo centro.
         */
        @FXML private TextField tf_provincia;


        /**
         * Riferimento alla <code>TextField</code> in cui inserire il CAP del nuovo centro.
         */
        @FXML private TextField tf_cap;


        /**
         * Riferimento al <code>ToggleGroup</code> che permette di selezionare la tipologia del nuovo centro.
         */
        @FXML private ToggleGroup tg_tipologia;


        /**
         * Singleton di <code>ControlloParametri</code> che permette di controllare che le credenziali inserite rispettino i requisiti richiesti.
         * @see ControlloParametri
         */
        private final ControlloParametri cp = ControlloParametri.getInstance();


        /**
         * Riferimento al client su cui si sta eseguendo l'applicazione.
         * @see ClientCV
         */
        private final ClientCV client = CentriVaccinali.client;


        /**
         * Permette di inizializzare l'interfaccia.
         */
        @FXML
        private void initialize() {
                cb_qualificatore.getItems().addAll(Qualificatore.values());
                cb_qualificatore.setValue(Qualificatore.VIA);
        }


        /**
         * Permette di notificare l'interfaccia quando una operazione di registrazione di un nuovo centro e' stata completata.
         * @param result rappresenta l'operazione completata.
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
         * Permette di controllare a real-time la correttezza delle credenziali attualmente inserite.
         * @param ke l'evento sollevato inserendo da tastiera le credenziali.
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
         * Effettua una chiamata al server per registrare un nuovo centro vaccinale.
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
         * Metodo per tornare all'interfaccia precedente.
         */
        @FXML
        private void backTo() {
                CentriVaccinali.setRoot("CV_home");
                client.stopOperation();
        }


        /**
         * Permette la chiusura dell'applicazione tramite la chiamata alla superclasse.
         * @see Controller
         */
        @FXML
        private void chiudiApp() {
                super.closeApp();
        }
}