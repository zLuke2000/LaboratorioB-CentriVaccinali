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
import javafx.scene.layout.AnchorPane;

import java.text.SimpleDateFormat;
import java.util.*;

public class CVRegistraCittadinoController extends Controller {

    @FXML public AnchorPane ap_registraVaccinato;
    // TextFiled
    @FXML private TextField tf_cv_selezionaProvincia;
    @FXML private TextField tf_cv_nomeCittadino;
    @FXML private TextField tf_cv_cognomeCittadino;
    @FXML private TextField tf_cv_cfCittadino;
    @FXML private TextField tf_cv_idVaccino;
    // ComboBox
    @FXML private ChoiceBox<String> cb_cv_selezionaComune;
    @FXML private ChoiceBox<String> cb_cv_selezionaCentro;
    // Label
    @FXML private Label l_cv_infoCentro;
    // RadioButton
    @FXML private RadioButton rb_cv_pfizer;
    @FXML private RadioButton rb_cv_astrazeneca;
    @FXML private RadioButton rb_cv_moderna;
    @FXML private RadioButton rb_cv_jj;
    // ToggleGroup (RadioButton)
    @FXML private ToggleGroup radioGroup1;
    // DatePicker
    @FXML private DatePicker dp_cv_datavaccino;

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
        tf_cv_idVaccino.setText(stringID);
    }

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
    }

    @Override
    public void notifyController(Result result) {
        switch (result.getOpType()){
            case RISULTATO_COMUNI:
                if (result.getResultComuni() != null) {
                    Platform.runLater(() -> {
                        cb_cv_selezionaComune.getItems().clear();
                        cb_cv_selezionaComune.getItems().addAll(result.getResultComuni());
                        cb_cv_selezionaComune.getSelectionModel().selectFirst();
                    });
                }
                break;
            case RISULTATO_CENTRI:
                if (result.getResultCentri() != null){
                    Platform.runLater(() -> {
                        listaCentri.clear();
                        listaCentri = result.getResultCentri();
                        cb_cv_selezionaCentro.getItems().clear();
                        for (CentroVaccinale centro : listaCentri) {
                            cb_cv_selezionaCentro.getItems().add(centro.getNome());
                            cb_cv_selezionaCentro.getSelectionModel().selectFirst();
                        }
                    });
                }
                break;
            case REGISTRAZIONE_VACCINATO:
                if (result.getResult()) {
                    System.out.println("Registrazione effettuata");
                } else {
                    if(result.getExtendedResult().contains(Result.Error.CF_GIA_IN_USO)) {
                        //TODO popup
                        System.err.println("Codice fiscale gia' associato ad un vaccinato");
                    }
                    if(result.getExtendedResult().contains(Result.Error.IDVAC_GIA_IN_USO)) {
                        //TODO popup
                        System.err.println("Id vaccinazione gia' associato ad un vaccinato");
                    }
                }
                break;
        }
    }

    @FXML void realtimeCheck(KeyEvent ke) {
        if (ke.getSource().equals(tf_cv_selezionaProvincia)) {
            if(cp.provincia(tf_cv_selezionaProvincia)) {
                client.getComuni(this, tf_cv_selezionaProvincia.getText().trim());
            } else {
                cb_cv_selezionaComune.getItems().clear();
                cb_cv_selezionaCentro.getItems().clear();
            }
        } else if (ke.getSource().equals(tf_cv_nomeCittadino)) {
            cp.testoSempliceSenzaNumeri(tf_cv_nomeCittadino,2, 50 );
        } else if (ke.getSource().equals(tf_cv_cognomeCittadino)){
            cp.testoSempliceSenzaNumeri(tf_cv_cognomeCittadino, 2, 50);
        } else if (ke.getSource().equals(tf_cv_cfCittadino)){
            cp.codiceFiscale(tf_cv_cfCittadino);
        }
    }

    @FXML void cbChange(Event e) {
        System.out.println(e);
        if(e.getSource().equals(cb_cv_selezionaComune)) {
            if(cb_cv_selezionaComune.getSelectionModel().getSelectedItem() != null) {
                client.getCentri(this, cb_cv_selezionaComune.getSelectionModel().getSelectedItem());
            }
        } else if(e.getSource().equals(cb_cv_selezionaCentro)) {
            if(cb_cv_selezionaCentro.getSelectionModel().getSelectedItem() != null) {
                selectedCV = listaCentri.get(cb_cv_selezionaCentro.getSelectionModel().getSelectedIndex());
                l_cv_infoCentro.setText(selectedCV.toString());
                l_cv_infoCentro.setVisible(true);
                statoSelezione();
            }
        }
    }

    @FXML void BackTo() {
        CentriVaccinali.setRoot("CV_change");
        client.stopOperation();
    }

    @FXML void registraVaccinato() {
        if(cp.testoSempliceSenzaNumeri(tf_cv_nomeCittadino,2, 50 ) & cp.testoSempliceSenzaNumeri(tf_cv_cognomeCittadino, 2, 50) & cp.codiceFiscale(tf_cv_cfCittadino) & cp.data(dp_cv_datavaccino) & statoSelezione()) {
            String nomeCentro= cb_cv_selezionaCentro.getSelectionModel().getSelectedItem();
            String nome= tf_cv_nomeCittadino.getText();
            String cognome= tf_cv_cognomeCittadino.getText();
            String cf= tf_cv_cfCittadino.getText();
            java.sql.Date data=java.sql.Date.valueOf(dp_cv_datavaccino.getValue());
            Vaccino tipoVaccino;
            Toggle selectedBtn= radioGroup1.getSelectedToggle();
            if (rb_cv_astrazeneca ==selectedBtn) {
                tipoVaccino = Vaccino.ASTRAZENECA;
            } else if (rb_cv_jj ==selectedBtn) {
                tipoVaccino=Vaccino.JNJ;
            } else if (rb_cv_moderna ==selectedBtn) {
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
            cssHelper.toValid(tf_cv_selezionaProvincia);
            cssHelper.toValid(cb_cv_selezionaComune);
            cssHelper.toValid(cb_cv_selezionaCentro);
            return true;
        } else {
            cssHelper.toError(tf_cv_selezionaProvincia, null);
            cssHelper.toError(cb_cv_selezionaComune, null);
            cssHelper.toError(cb_cv_selezionaCentro, null);
            return false;
        }
    }
}
