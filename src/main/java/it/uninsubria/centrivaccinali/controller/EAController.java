package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.EventoAvverso;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EAController extends Controller{

    public RadioButton rb_ea1;
    public RadioButton rb_ea2;
    public RadioButton rb_ea3;
    public RadioButton rb_ea4;
    public RadioButton rb_ea5;
    public RadioButton rb_ea6;
    public ToggleGroup tg_ea_gruppo;
    public TextField tf_ea_altro;
    public Slider s_severita;
    public TextArea ta_ea_note;
    public Label l_ea_caratteri;

    private DialogHelper dh;
    private ClientCV client;

    @FXML
    void initialize() {
        // Imposto l'handler per la colorazione dello slider
        s_severita.valueProperty().addListener( e -> {
            s_severita.lookup(".track").setStyle("-fx-background-color: hsb( " + Math.round((-15*s_severita.getValue())+75) + ", 75%, 100%);");
        });
    }


    @FXML
    void selezioneEvento(Event e) {
        if(e.getSource().equals(tf_ea_altro)) {
            tg_ea_gruppo.selectToggle(null);
        } else {
            tf_ea_altro.setText("");
        }
    }

    @FXML
    void controlloLunghezza() {
        if(ta_ea_note.getText().trim().length() > 256) {
            ta_ea_note.setText(ta_ea_note.getText().substring(0, 256));
            ta_ea_note.positionCaret(ta_ea_note.getText().length());
        }
        l_ea_caratteri.setText(ta_ea_note.getText().trim().length() + "/256");
    }

    @FXML
    void controlloTestoEvento() {
        Pattern pattern = Pattern.compile("[^a-zA-Z\\s]");
        Matcher matcher = pattern.matcher(tf_ea_altro.getText());
        if (matcher.find()) {
            tf_ea_altro.setText(tf_ea_altro.getText().replaceAll("[^a-zA-Z\\s]", ""));
            tf_ea_altro.positionCaret(tf_ea_altro.getText().length());
        }
    }

    @FXML
    private void salvaEA() {
        String evento;
        if (tg_ea_gruppo.getSelectedToggle() != null) {
            evento = ((RadioButton) tg_ea_gruppo.getSelectedToggle()).getText();
        } else if (!tf_ea_altro.getText().isBlank()){
            evento = tf_ea_altro.getText().trim();
        } else {
            dh = new DialogHelper("ERRORE", "E' necessario inserire un tipo di evento", DialogHelper.Type.ERROR);
            dh.display(null); //TODO da sistemato con il root della dashboard
            return;
        }
        int severita = (int) s_severita.getValue();
        String note = ta_ea_note.getText().trim();
        client.registraEventoAvverso(this, new EventoAvverso(evento, severita, note, null));
    }

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
    }

    @Override
    public void notifyController(Result result) {
        if (result.getResult()){
            Platform.runLater(() -> {
                dh = new DialogHelper("EVENTO AVVERSO REGISTRATO", "L'evento avverso e' stato registrato", DialogHelper.Type.INFO);
                dh.display(null); //TODO da sistemato con il root della dashboard
                // RESET INTERFACCIA
                tg_ea_gruppo.selectToggle(null);
                tf_ea_altro.setText("");
                s_severita.setValue(1);
                ta_ea_note.setText("");
                l_ea_caratteri.setText("0/256");
            });
        }
        else{
            Platform.runLater(() -> {
                dh = new DialogHelper("ATTENZIONE", "Registrazione fallita.\nQuesto evento avverso e' gia' stato inserito", DialogHelper.Type.ERROR);
                dh.display(null); //TODO da sistemato con il root della dashboard
            });

        }
    }
}
