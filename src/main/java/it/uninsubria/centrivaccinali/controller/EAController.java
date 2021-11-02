package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.enumerator.EventiAvversi;
import it.uninsubria.centrivaccinali.models.EventoAvverso;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.CssHelper;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    private CssHelper css = CssHelper.getInstance();
    private ControlloParametri cp = ControlloParametri.getInstance();
    private DialogHelper dh;
    private RadioButton[] rbArray;
    private ClientCV client;

    @FXML
    void initialize() {
        rbArray = new RadioButton[]{rb_ea1, rb_ea2, rb_ea3, rb_ea4, rb_ea5, rb_ea6};
        for(int i=0; i<rbArray.length; i++) {
            rbArray[i].setText(String.valueOf(EventiAvversi.values()[i]));
        }
        // Imposto l'handler per la colorazione dello slider
        s_severita.valueProperty().addListener( e -> s_severita.lookup(".track").setStyle("-fx-background-color: hsb( " + (120-s_severita.getValue()*120/5) + ", 75%, 100%);"));
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
    boolean controlloLunghezza() {
        l_ea_caratteri.setText(ta_ea_note.getText().trim().length() + "/256");
        if(ta_ea_note.getText().trim().length() > 256) {
            css.toError(ta_ea_note, new Tooltip("Massimo 256 caratteri"));
            return false;
        } else {
            css.toDefault(ta_ea_note);
            return true;
        }
    }

    @FXML
    void salva() {
        if(tg_ea_gruppo.getSelectedToggle() != null) {
            saveToDB(true);
        } else {
            if(cp.testoSempliceSenzaNumeri(tf_ea_altro, 1, 30)) {
                saveToDB(false);
            } else {
                dh = new DialogHelper("ATTENZIONE", "Selezionare un evento avverso\noppure immetterne uno personalizzato", DialogHelper.Type.WARNING);
                dh.display(null); //TODO da sistemato con il root della dashboard
            }
        }

    }

    private void saveToDB(boolean evento) {
        if(controlloLunghezza()) {
            EventoAvverso eaCorrente = new EventoAvverso("", (int) s_severita.getValue(), ta_ea_note.getText().trim());
            if(evento) {
                eaCorrente.setEvento(((RadioButton) tg_ea_gruppo.getSelectedToggle()).getText());
            } else {
                eaCorrente.setEvento(tf_ea_altro.getText().trim());
            }
            client.registraEventoAvverso(this, eaCorrente);
        }
    }

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
    }

    @Override
    public void notifyController(Result result) {
        if (result.getResult()){
            dh = new DialogHelper("EVVIVA", "Registrazione OK", DialogHelper.Type.INFO);
            dh.display(null); //TODO da sistemato con il root della dashboard
            System.out.println("OK");
        }
        else{
            dh = new DialogHelper("ATTENZIONE", "Registrazione OK", DialogHelper.Type.INFO);
            dh.display(null); //TODO da sistemato con il root della dashboard
        }
    }
}
