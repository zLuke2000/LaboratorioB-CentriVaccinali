package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.enumerator.Vaccino;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.models.Vaccinato;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CVRegistraCittadinoController extends Controller {

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
    // ToggleGroup (RadioButton)
    @FXML private ToggleGroup RadioGroup1;
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
        String stringID = sdf.format(new java.util.Date());
        stringID=stringID.substring(0, 16);
        idVac = Long.parseLong(stringID);
        TF_CV_idVaccino.setText(stringID);
    }

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
    }

    @Override
    public void notifyController(Result result) {
        switch (result.getOpType()){
            case Result.RISULTATO_COMUNI:
                if (result.getResultComuni() != null) {
                    Platform.runLater(() -> {
                        CB_CV_selezionaComune.getItems().clear();
                        CB_CV_selezionaComune.getItems().addAll(result.getResultComuni());
                        CB_CV_selezionaComune.getSelectionModel().selectFirst();
                    });
                }
                break;
            case Result.RISULTATO_CENTRI:
                if (result.getResultCentri() != null){
                    Platform.runLater(() -> {
                        listaCentri.clear();
                        listaCentri = result.getResultCentri();
                        CB_CV_selezionaCentro.getItems().clear();
                        for (CentroVaccinale centro : listaCentri) {
                            CB_CV_selezionaCentro.getItems().add(centro.getNome());
                            CB_CV_selezionaCentro.getSelectionModel().selectFirst();
                        }
                    });
                }
                break;
            case Result.REGISTRAZIONE_VACCINATO:
                if (result.getResult()) {
                    System.out.println("Registrazione effettuata");
                }
                //TODO mostrare errori
                break;
        }
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

    @FXML void BackTo() {
        CentriVaccinali.setRoot("CV_change");
        client.stopOperation();
    }

    @FXML void registraVaccinato() {
        if(cp.testoSempliceSenzaNumeri(TF_CV_nomeCittadino,2, 50 ) & cp.testoSempliceSenzaNumeri(TF_CV_cognomeCittadino, 2, 50) & cp.codiceFiscale(TF_CV_cfCittadino) & cp.data(DP_CV_datavaccino) & statoSelezione()) {
            String nomeCentro=CB_CV_selezionaCentro.getSelectionModel().getSelectedItem();
            String nome=TF_CV_nomeCittadino.getText();
            String cognome=TF_CV_cognomeCittadino.getText();
            String cf=TF_CV_cfCittadino.getText();
            java.sql.Date data=java.sql.Date.valueOf(DP_CV_datavaccino.getValue());
            Vaccino tipoVaccino;
            Toggle selectedBtn=RadioGroup1.getSelectedToggle();
            if (RB_CV_astrazeneca==selectedBtn) {
                tipoVaccino = Vaccino.ASTRAZENECA;
            } else if (RB_CV_jj==selectedBtn) {
                tipoVaccino=Vaccino.JNJ;
            } else if (RB_CV_moderna==selectedBtn) {
                tipoVaccino=Vaccino.MODERNA;
            } else {
                tipoVaccino=Vaccino.PFIZER;
            }
            Vaccinato nuovoVaccinato=new Vaccinato(nomeCentro, nome, cognome, cf, data, tipoVaccino, idVac);
            client.registraVaccinato(this, nuovoVaccinato);
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
}
