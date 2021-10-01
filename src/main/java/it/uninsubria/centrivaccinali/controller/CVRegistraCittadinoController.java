package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.CssHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.textfield.TextFields;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CVRegistraCittadinoController {

    // TextFiled
    @FXML private TextField TF_CV_centroVaccinale;
    @FXML private TextField TF_CV_nomeCittadino;
    @FXML private TextField TF_CV_cognomeCittadino;
    @FXML private TextField TF_CV_cfCittadino;
    @FXML private TextField TF_CV_idVaccino;
    //RadioButton
    @FXML private RadioButton RB_CV_pfizer;
    @FXML private RadioButton RB_CV_astrazeneca;
    @FXML private RadioButton RB_CV_moderna;
    @FXML private RadioButton RB_CV_jj;
    //DatePicker
    @FXML private DatePicker DP_CV_datavaccino;
    //Button
    @FXML private Button  B_CV_registraCittadino;

    private final ControlloParametri cp = ControlloParametri.getInstance();
    private final CssHelper cssHelper = CssHelper.getInstance();
    private ClientCV client;
    private List<CentroVaccinale> listaCentri = new ArrayList<>();
    private List<String> infoCentri = new ArrayList<>();
    private long idVac = 0L;

    @FXML void initialize() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmssSS");
        String stringID = sdf.format(new Date());
        TF_CV_idVaccino.setText(stringID);
        idVac = Long.parseLong(stringID);
    }

    public void risultato(List<CentroVaccinale> result) {
        listaCentri.clear();
        infoCentri.clear();
        listaCentri = result;
        for(CentroVaccinale centro: result) {
            infoCentri.add(centro.getNome() + " - " + centro.getIndirizzo());
        }
        TextFields.bindAutoCompletion(TF_CV_centroVaccinale, infoCentri);
    }

    public void initParameter(ClientCV client) {
        this.client = client;
    }

    @FXML void realtimeCheck(KeyEvent ke) {
        if(ke.getSource().equals(TF_CV_centroVaccinale)) {
            String testo = TF_CV_centroVaccinale.getText().trim();
            if(testo.length() > 2) {
                cssHelper.toDefault(TF_CV_centroVaccinale);
                try {
                    client.cercaCentrovaccinale(this, testo);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                cssHelper.toError(TF_CV_centroVaccinale, new Tooltip("Inserire almeno 3 caratteri per la ricerca"));
            }
        }
        else if (ke.getSource().equals(TF_CV_nomeCittadino)) {
            cp.testoSempliceSenzaNumeri(TF_CV_nomeCittadino,2, 50 );
        }
        else if (ke.getSource().equals(TF_CV_cognomeCittadino)){
            cp.testoSempliceSenzaNumeri(TF_CV_cognomeCittadino, 2, 50);
        }
        else if (ke.getSource().equals(TF_CV_cfCittadino)){
            cp.codiceFiscale(TF_CV_cfCittadino);
        }
    }

    @FXML void BackTo() {
        CentriVaccinali.setRoot("CV_change");
    }
}
