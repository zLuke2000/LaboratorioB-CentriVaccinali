package it.uninsubria.centrivaccinali.controller.cittadini.dashboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
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

public class AggiungiEventoAvverso extends Controller {

    @FXML private ToggleGroup tg_eventiAvversi;
    @FXML private TextField tf_altro;
    @FXML private Slider s_severita;
    @FXML private TextArea ta_note;
    @FXML private Label l_caratteri;
    @FXML private GridPane gp_root;

    private final ClientCV client = CentriVaccinali.client;
    private final CssHelper css = CssHelper.getInstance();
    private CIDashboardController parent;

    @FXML
    private void initialize() {
        // Imposto l'handler per la colorazione dello slider
        s_severita.valueProperty().addListener( e -> s_severita.lookup(".track").setStyle("-fx-background-color: hsb( " + Math.round((-15*s_severita.getValue())+75) + ", 75%, 100%);"));
    }


    @FXML
    private void selezioneEvento(Event e) {
        if(e.getSource().equals(tf_altro)) {
            tg_eventiAvversi.selectToggle(null);
        } else {
            tf_altro.setText("");
        }
    }

    @FXML
    private void controlloLunghezza() {
        if(ta_note.getText().trim().length() > 256) {
            ta_note.setText(ta_note.getText().substring(0, 256));
            ta_note.positionCaret(ta_note.getText().length());
        }
        l_caratteri.setText(ta_note.getText().trim().length() + "/256");
    }

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

    @FXML
    private void chiudiFragment() {
        parent.rimuoviFragment(gp_root);
    }

    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }
}
