package it.uninsubria.centrivaccinali.controller.centri;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.Window;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.enumerator.Vaccino;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.models.Vaccinato;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.CssHelper;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller dell'interfaccia di registrazione di un nuovo cittadino vaccinato
 */
public class CVRegistraCittadinoController extends Controller {
    /**
     * Riferimento alla <code>TextField</code> che permette di inserire la provincia del centro presso cui il cittadino &amp;egrave stato vaccinato.
     */
    @FXML private TextField tf_selezionaProvincia;


    /**
     * Riferimento alla <code>ComboBox</code> che permette di selezionare il comune del centro presso cui il cittadino &amp;egrave stato vaccianto.
     */
    @FXML private ComboBox<String> cb_selezionaComune;


    /**
     * Riferimento alla <code>ComboBox</code> che permette di selezionare il centro presso cui il cittadino &amp;egrave stato vaccianto.
     */
    @FXML private ComboBox<String> cb_selezionaCentro;


    /**
     * Riferimento alla <code>Label</code> in cui sono presenti le informazioni principali del centro vaccianle selezionato.
     */
    @FXML private Label l_infoCentro;


    /**
     * Riferimento alla <code>TextField</code> in cui inserire il nome del cittadino vaccinato.
     */
    @FXML private TextField tf_nomeCittadino;


    /**
     * Riferimento alla <code>TextField</code> in cui inserire il cognome del cittadino vaccinato.
     */
    @FXML private TextField tf_cognomeCittadino;


    /**
     * Riferimento alla <code>TextField</code> in cui inserire il codice fiscale del cittadino vaccinato.
     */
    @FXML private TextField tf_cfCittadino;


    /**
     * Riferimento alla <code>TextField</code> in cui &amp;egrave presente l'id di vaccinazione del cittadino vaccinato.
     */
    @FXML private TextField tf_idVaccino;


    /**
     * Riferimento al <code>RadioButton</code> per selezionare "pfizer".
     */
    @FXML private RadioButton rb_pfizer;


    /**
     * Riferimento al <code>ToggleGroup</code> per selezionare la tipologia di vaccino.
     */
    @FXML private ToggleGroup tg_vaccino;


    /**
     * Rifermimento al <code>DatePicker</code> per selezionare la data della vaccinazione.
     */
    @FXML private DatePicker dp_dataVaccino;


    /**
     * Singleton di <code>ControlloParametri</code> per controllare che i dati inseriti siano corretti.
     * @see Controller
     */
    private final ControlloParametri cp = ControlloParametri.getInstance();


    /**
     * Singleton di <code>CssHelper</code> per gestire gli stili delle varie componenti grafiche.
     * @see CssHelper
     */
    private final CssHelper cssHelper = CssHelper.getInstance();


    /**
     * Riferimento al client su cui si sta eseguendo l'applicazione.
     * @see ClientCV
     */
    private final ClientCV client = CentriVaccinali.client;


    /**
     * Lista dei centri vaccinali presenti nel comune selezionato.
     */
    private List<CentroVaccinale> listaCentri = new ArrayList<>();


    /**
     * Centro vaccinale selezionato.
     */
    private CentroVaccinale selectedCV;


    /**
     * Id della vaccinazione che viene generato.
     */
    private long idVac = 0L;


    /**
     * Inizializza l'interfaccia.
     */
    @FXML
    private void initialize() {
        generaIdVaccino();
    }


    /**
     * Genera l'id della vaccinazione in base a varie informazioni.
     */
    private void generaIdVaccino() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String stringID = sdf.format(new java.util.Date());
        stringID=stringID.substring(0, 16);
        idVac = Long.parseLong(stringID);
        tf_idVaccino.setText(stringID);
    }


    /**
     * Notifica l'interfaccia quando viene completata una operazione da parte del server.
     * In particolare notifica a seguito di una operazione di registrazione o di una operazione di ricerca di un centro.
     * @param result rappresenta l'operazione appena completata.
     */
    @Override
    public void notifyController(Result result) {
        switch (result.getOpType()){
            case RICERCA_COMUNI:
                if (result.getList(String.class) != null) {
                    Platform.runLater(() -> {
                        cb_selezionaComune.getItems().clear();
                        cb_selezionaComune.getItems().addAll(result.getList(String.class));
                        cb_selezionaComune.getSelectionModel().selectFirst();
                    });
                }
                break;
            case RICERCA_CENTRI:
                if (result.getList(CentroVaccinale.class) != null){
                    Platform.runLater(() -> {
                        listaCentri.clear();
                        listaCentri = result.getList(CentroVaccinale.class);
                        cb_selezionaCentro.getItems().clear();
                        for (CentroVaccinale cv : listaCentri) {
                            cb_selezionaCentro.getItems().add(cv.getNome());
                        }
                        cb_selezionaCentro.getSelectionModel().selectFirst();
                    });
                } else {
                    System.err.println("ERRORE");
                }
                break;
            case REGISTRAZIONE_VACCINATO:
                Window.scene.setCursor(Cursor.DEFAULT);
                if (result.getResult()) {
                    System.out.println("Registrazione effettuata");
                    Platform.runLater(() -> {
                        DialogHelper dh = new DialogHelper("REGISTRAZIONE EFFETTUATA", "Il cittadino vaccinato e' stato registrato con successo", DialogHelper.Type.INFO);
                        dh.display();
                        //reset interfaccia
                        tf_nomeCittadino.setText("");
                        tf_cognomeCittadino.setText("");
                        tf_cfCittadino.setText("");
                        dp_dataVaccino.setValue(null);
                        tg_vaccino.selectToggle(rb_pfizer);
                        generaIdVaccino();
                    });
                } else {
                    DialogHelper dh = new DialogHelper("REGISTRAZIONE FALLITA", "Non e' stato possibile registrare correttamente il cittadino", DialogHelper.Type.ERROR);
                    dh.display();
                    if(result.getExtendedResult().contains(Result.Error.CF_GIA_IN_USO)) {
                        cssHelper.toError(tf_cfCittadino, new Tooltip("Codice fiscale gia' associato ad un vaccinato"));
                        System.err.println("Codice fiscale gia' associato ad un vaccinato");
                    }
                    if(result.getExtendedResult().contains(Result.Error.IDVAC_GIA_IN_USO)) {
                        cssHelper.toError(tf_idVaccino, new Tooltip("Id vaccinazione gia' associato ad un vaccinato"));
                        System.err.println("Id vaccinazione gia' associato ad un vaccinato");
                    }
                }
                break;
        }
    }


    /**
     * Controllo real-time della compilazione dei campi per l'inserimento
     * dinamico dei comuni da database.
     * @param ke evento sollevato al seguito dell'isnerimento da tastiera dei dati.
     */
    @FXML
    private void realtimeCheck(KeyEvent ke) {
        if (ke.getSource().equals(tf_selezionaProvincia)) {
            if(cp.provincia(tf_selezionaProvincia)) {
                client.getComuni(this, tf_selezionaProvincia.getText().trim());
            } else {
                cb_selezionaComune.getItems().clear();
                cb_selezionaCentro.getItems().clear();
            }
        } else if (ke.getSource().equals(tf_nomeCittadino)) {
            cp.testoSempliceSenzaNumeri(tf_nomeCittadino,2, 50 );
        } else if (ke.getSource().equals(tf_cognomeCittadino)){
            cp.testoSempliceSenzaNumeri(tf_cognomeCittadino, 2, 50);
        } else if (ke.getSource().equals(tf_cfCittadino)){
            cp.codiceFiscale(tf_cfCittadino);
        }
    }


    /**
     * Effettua una chiamata al server per recuperare i centri in base al comune selezionato.
     * @param e evento di selezione di un item della <code>ComboBox</code>.
     */
    @FXML
    private void cbChange(Event e) {
        if(e.getSource().equals(cb_selezionaComune)) {
            if(cb_selezionaComune.getSelectionModel().getSelectedItem() != null) {
                client.getCentri(this, cb_selezionaComune.getSelectionModel().getSelectedItem());
            }
        } else if(e.getSource().equals(cb_selezionaCentro)) {
            if(cb_selezionaCentro.getSelectionModel().getSelectedItem() != null) {
                selectedCV = listaCentri.get(cb_selezionaCentro.getSelectionModel().getSelectedIndex());
                l_infoCentro.setText(selectedCV.toString());
                l_infoCentro.setVisible(true);
                statoSelezione();
            }
        }
    }


    /**
     * Metedo per ritornare all'interfaccia precedente.
     */
    @FXML
    private void backTo() {
        Window.setRoot("CV_home");
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


    /**
     * Effettua una chiamata al server per registrare un nuovo vaccinato.
     */
    @FXML
    private void registraVaccinato() {
        if(cp.testoSempliceSenzaNumeri(tf_nomeCittadino,2, 50 ) & cp.testoSempliceSenzaNumeri(tf_cognomeCittadino, 2, 50) & cp.codiceFiscale(tf_cfCittadino) & cp.data(dp_dataVaccino) & statoSelezione()) {
            String nomeCentro= cb_selezionaCentro.getSelectionModel().getSelectedItem();
            String nome= tf_nomeCittadino.getText();
            String cognome= tf_cognomeCittadino.getText();
            String cf= tf_cfCittadino.getText();
            java.sql.Date data=java.sql.Date.valueOf(dp_dataVaccino.getValue());
            Vaccino tipoVaccino = Vaccino.getValue(((RadioButton) tg_vaccino.getSelectedToggle()).getText());
            Vaccinato nuovoVaccinato=new Vaccinato(nomeCentro, nome, cognome, cf, data, tipoVaccino, idVac);
            Window.scene.setCursor(Cursor.WAIT);
            client.registraVaccinato(this, nuovoVaccinato);
        }
    }


    /**
     * Controlla che sia stato selezionato correttamente un centro vaccinale.
     * @return un booleano che rappresenta se &amp;egrave stato selezionato o meno un centro.
     */
    private boolean statoSelezione() {
        if(selectedCV != null) {
            cssHelper.toValid(tf_selezionaProvincia);
            cssHelper.toValid(cb_selezionaComune);
            cssHelper.toValid(cb_selezionaCentro);
            return true;
        } else {
            cssHelper.toError(tf_selezionaProvincia, null);
            cssHelper.toError(cb_selezionaComune, null);
            cssHelper.toError(cb_selezionaCentro, null);
            return false;
        }
    }
}
