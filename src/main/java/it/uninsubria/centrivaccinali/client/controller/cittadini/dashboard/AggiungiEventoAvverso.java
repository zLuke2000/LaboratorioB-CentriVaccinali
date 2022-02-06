//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.client.controller.cittadini.dashboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.client.controller.Controller;
import it.uninsubria.centrivaccinali.models.EventoAvverso;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.CssHelper;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller per l'interfaccia per inserire un nuovo evento avverso.
 * @author ...
 */
public class AggiungiEventoAvverso extends Controller {
    /**
     * <code>ToggleGroup</code> per selezionare il tipo di evento avverso.
     */
    @FXML private ToggleGroup tg_eventiAvversi;


    /**
     * <code>TextField</code> per inserire un'altra tipologia di evento avverso.
     */
    @FXML private TextField tf_altro;


    /**
     * <code>Slider</code> per selezionare la severit&amp;agrave
     */
    @FXML private Slider s_severita;


    /**
     * <code>TextArea</code> per inserire note opzionali.
     */
    @FXML private TextArea ta_note;


    /**
     * <code>Label</code> che mostra a real-time il numero di caratteri attualmente inseriti nelle note opzionali.
     */
    @FXML private Label l_caratteri;


    /**
     * <code>GridPane</code> contenitore dell'interfaccia.
     */
    @FXML private GridPane gp_root;


    /**
     * Riferimento al client su cui si sta eseguendo l'applicazione.
     * @see ClientCV
     */
    private final ClientCV client = CentriVaccinali.client;


    /**
     * Rifermento al singleton <code>CssHelper</code> che permette la gestione degli stili per i vari componenti grafici.
     * @see CssHelper
     */
    private final CssHelper css = CssHelper.getInstance();


    /**
     * Riferimento alla dashboard che contiene questa interfaccia.
     * @see CIDashboardController
     */
    private CIDashboardController parent;


    /**
     * Metodo per inizializzare l'interfaccia.
     */
    @FXML
    private void initialize() {
        // Imposto l'handler per la colorazione dello slider
        s_severita.valueProperty().addListener( e -> s_severita.lookup(".track").setStyle("-fx-background-color: hsb( " + Math.round((-15*s_severita.getValue())+75) + ", 75%, 100%);"));
    }


    /**
     * Gestisce la selezione della tipologia di evento.
     * @param e l'evento sollevato.
     */
    @FXML
    private void selezioneEvento(Event e) {
        if(e.getSource().equals(tf_altro)) {
            tg_eventiAvversi.selectToggle(null);
        } else {
            tf_altro.setText("");
        }
    }


    /**
     * Controlla a real-time la lunghezza del testo inserito nelle note opzionali.
     */
    @FXML
    private void controlloLunghezza() {
        if(ta_note.getText().trim().length() > 256) {
            ta_note.setText(ta_note.getText().substring(0, 256));
            ta_note.positionCaret(ta_note.getText().length());
        }
        l_caratteri.setText(ta_note.getText().trim().length() + "/256");
    }


    /**
     * Controlla a real-time il testo inserito nella <code>TextField</code> per inserire un'altro tipo di evento.
     */
    @FXML
    private void controlloTestoEvento() {
        Pattern pattern = Pattern.compile("[^a-zA-Z\\s]");
        Matcher matcher = pattern.matcher(tf_altro.getText());
        if (matcher.find()) {
            tf_altro.setText(tf_altro.getText().replaceAll("[^a-zA-Z\\s]", ""));
            tf_altro.positionCaret(tf_altro.getText().length());
        }
        if(tf_altro.getText().trim().length() > 30){
            css.toError(tf_altro, new Tooltip("Massimo 30 caratteri"));
        }else{
            css.toDefault(tf_altro);
        }
    }


    /**
     * Controlla la correttezza dei dati inseriti ed effettua una chiama al server per registrare l'evento.
     */
    @FXML
    private void salva() {
        String evento;
        if (tg_eventiAvversi.getSelectedToggle() != null) {
            evento = ((RadioButton) tg_eventiAvversi.getSelectedToggle()).getText().toLowerCase();
        } else if (!tf_altro.getText().isBlank()){
            evento = tf_altro.getText().trim();
        } else {
            new DialogHelper("ERRORE", "E' necessario inserire un tipo di evento", DialogHelper.Type.ERROR).display();
            return;
        }
        int severita = (int) s_severita.getValue();
        String note = ta_note.getText().trim();
        client.registraEventoAvverso(this, new EventoAvverso(client.getUtenteLoggato().getId_vaccinazione(), evento, severita, note));
    }


    /**
     * Notifica l'interfaccia quando viene completata una operazione di registrazione di evento avverso.
     * @param result l'operazione appena completata.
     */
    @Override
    public void notifyController(Result result) {
        if (result.getResult()){
            Platform.runLater(() -> {
                new DialogHelper("EVENTO AVVERSO REGISTRATO", "L'evento avverso e' stato registrato", DialogHelper.Type.INFO).display();
                // RESET INTERFACCIA
                tg_eventiAvversi.selectToggle(null);
                tf_altro.setText("");
                s_severita.setValue(1);
                ta_note.setText("");
                l_caratteri.setText("0/256");
            });
        }
        else{
            Platform.runLater(() -> new DialogHelper("ATTENZIONE", "Registrazione fallita.\nQuesto evento avverso e' gia' stato inserito", DialogHelper.Type.ERROR).display());
        }
    }


    /**
     * Permette di rimuovere quest'interfaccia dalla dashboard.
     * @see CIDashboardController
     */
    @FXML
    private void chiudiFragment() {
        parent.rimuoviFragment(gp_root);
    }


    /**
     * Setta il riferimento alla dashboard che contiene la seguente interfaccia.
     * @see CIDashboardController
     * @param c controller dell'interfaccia contenitore.
     */
    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }
}
