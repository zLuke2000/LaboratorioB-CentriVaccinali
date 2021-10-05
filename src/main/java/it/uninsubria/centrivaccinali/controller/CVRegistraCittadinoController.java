package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CVRegistraCittadinoController {

    // TextFiled
    @FXML private TextField TF_CV_selezionaProvincia;
    @FXML private TextField TF_CV_nomeCittadino;
    @FXML private TextField TF_CV_cognomeCittadino;
    @FXML private TextField TF_CV_cfCittadino;
    @FXML private TextField TF_CV_idVaccino;
    // ComboBox
    @FXML private ChoiceBox<String> CB_CV_selezionaComune;
    @FXML private ChoiceBox<String> CB_CV_selezionaCentro;
    // Label
    @FXML private Label L_CV_infoCentro;
    // RadioButton
    @FXML private RadioButton RB_CV_pfizer;
    @FXML private RadioButton RB_CV_astrazeneca;
    @FXML private RadioButton RB_CV_moderna;
    @FXML private RadioButton RB_CV_jj;
    // DatePicker
    @FXML private DatePicker DP_CV_datavaccino;
    // Button
    @FXML private Button  B_CV_registraCittadino;

    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final CssHelper cssHelper = CssHelper.getInstance();
    private ClientCV client;
    private List<CentroVaccinale> listaCentri = new ArrayList<>();
    private CentroVaccinale selectedCV;
    private long idVac = 0L;

    @FXML void initialize() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String stringID = sdf.format(new Date());
        stringID = stringID.substring(0, 16);
        idVac = Long.parseLong(stringID);
        TF_CV_idVaccino.setText(stringID);
    }

    public void initParameter(ClientCV client) {
        this.client = client;
    }

    @FXML void realtimeCheck(KeyEvent ke) {
        if (ke.getSource().equals(TF_CV_selezionaProvincia)) {
            if(cp.provincia(TF_CV_selezionaProvincia)) {
                client.getComuni(this, TF_CV_selezionaProvincia.getText().trim());
            } else {
                CB_CV_selezionaComune.getItems().clear();
                CB_CV_selezionaCentro.getItems().clear();
            }
        } else if (ke.getSource().equals(TF_CV_nomeCittadino)) {
            cp.testoSempliceSenzaNumeri(TF_CV_nomeCittadino,2, 50 );
        } else if (ke.getSource().equals(TF_CV_cognomeCittadino)){
            cp.testoSempliceSenzaNumeri(TF_CV_cognomeCittadino, 2, 50);
        } else if (ke.getSource().equals(TF_CV_cfCittadino)){
            cp.codiceFiscale(TF_CV_cfCittadino);
        }
    }

    @FXML void cbChange(Event e) {
        System.out.println(e);
        if(e.getSource().equals(CB_CV_selezionaComune)) {
            if(CB_CV_selezionaComune.getSelectionModel().getSelectedItem() != null) {
                client.getCentri(this, CB_CV_selezionaComune.getSelectionModel().getSelectedItem());
            }
        } else if(e.getSource().equals(CB_CV_selezionaCentro)) {
            if(CB_CV_selezionaCentro.getSelectionModel().getSelectedItem() != null) {
                selectedCV = listaCentri.get(CB_CV_selezionaCentro.getSelectionModel().getSelectedIndex());
                L_CV_infoCentro.setText(selectedCV.toString());
                L_CV_infoCentro.setVisible(true);
                statoSelezione();
            }
        }
    }

    @FXML void BackTo() { CentriVaccinali.setRoot("CV_change"); }

    public void risultatoComuni(List<String> resultComuni) {
        Platform.runLater(() -> {
            CB_CV_selezionaComune.getItems().clear();
            CB_CV_selezionaComune.getItems().addAll(resultComuni);
            CB_CV_selezionaComune.getSelectionModel().selectFirst();
        });
    }

    public void risultatoCentri(List<CentroVaccinale> resultCentri) {
        Platform.runLater(() -> {
            listaCentri.clear();
            listaCentri = resultCentri;
            CB_CV_selezionaCentro.getItems().clear();
            for (CentroVaccinale centro : resultCentri) {
                CB_CV_selezionaCentro.getItems().add(centro.getNome());
                CB_CV_selezionaCentro.getSelectionModel().selectFirst();
            }
        });
    }

    @FXML void registraVaccinato() {
        if(cp.testoSempliceSenzaNumeri(TF_CV_nomeCittadino,2, 50 ) & cp.testoSempliceSenzaNumeri(TF_CV_cognomeCittadino, 2, 50) & cp.codiceFiscale(TF_CV_cfCittadino) & cp.data(DP_CV_datavaccino) & statoSelezione()) {
        }
    }

    private boolean statoSelezione() {
        if(selectedCV != null) {
            cssHelper.toValid(TF_CV_selezionaProvincia);
            cssHelper.toValid(CB_CV_selezionaComune);
            cssHelper.toValid(CB_CV_selezionaCentro);
            return true;
        } else {
            cssHelper.toError(TF_CV_selezionaProvincia, null);
            cssHelper.toError(CB_CV_selezionaComune, null);
            cssHelper.toError(CB_CV_selezionaCentro, null);
            return false;
        }
    }

    public void risultatoRegistrazione(int resultRegistrazione) {
        // TODO risultato registrazione cittadino vaccinato
    }
}
