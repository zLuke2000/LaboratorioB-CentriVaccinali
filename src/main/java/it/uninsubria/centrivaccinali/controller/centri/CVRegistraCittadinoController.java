package it.uninsubria.centrivaccinali.controller.centri;

import it.uninsubria.centrivaccinali.CentriVaccinali;
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

public class CVRegistraCittadinoController extends Controller {
    // TextFiled
    @FXML private TextField tf_selezionaProvincia;
    @FXML private TextField tf_nomeCittadino;
    @FXML private TextField tf_cognomeCittadino;
    @FXML private TextField tf_cfCittadino;
    @FXML private TextField tf_idVaccino;
    // ComboBox
    @FXML private ComboBox<String> cb_selezionaComune;
    @FXML private ComboBox<String> cb_selezionaCentro;
    // Label
    @FXML private Label l_infoCentro;
    // RadioButton
    @FXML private RadioButton rb_pfizer;
    // ToggleGroup (RadioButton)
    @FXML private ToggleGroup tg_vaccino;
    // DatePicker
    @FXML private DatePicker dp_dataVaccino;

    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final CssHelper cssHelper = CssHelper.getInstance();
    private final ClientCV client = CentriVaccinali.client;
    private List<CentroVaccinale> listaCentri = new ArrayList<>();
    private CentroVaccinale selectedCV;
    private long idVac = 0L;

    @FXML
    private void initialize() {
        generaIdVaccino();
    }

    /**
     *
     */
    private void generaIdVaccino() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String stringID = sdf.format(new java.util.Date());
        stringID=stringID.substring(0, 16);
        idVac = Long.parseLong(stringID);
        tf_idVaccino.setText(stringID);
    }

    /**
     *
     * @param result
     */
    @Override
    public void notifyController(Result result) {
        switch (result.getOpType()){
            case RISULTATO_COMUNI:
                if (result.getList(String.class) != null) {
                    Platform.runLater(() -> {
                        cb_selezionaComune.getItems().clear();
                        cb_selezionaComune.getItems().addAll(result.getList(String.class));
                        cb_selezionaComune.getSelectionModel().selectFirst();
                    });
                }
                break;
            case RISULTATO_CENTRI:
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
                CentriVaccinali.scene.setCursor(Cursor.DEFAULT);
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
     *
     * @param ke
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
     *
     * @param e
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

    /**
     *
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
            CentriVaccinali.scene.setCursor(Cursor.WAIT);
            client.registraVaccinato(this, nuovoVaccinato);
        }
    }

    /**
     *
     * @return
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
