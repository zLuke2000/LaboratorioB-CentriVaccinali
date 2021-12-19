package it.uninsubria.centrivaccinali.controller.cittadini.dashboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 *
 */
public class CIInfoCittadinoController extends Controller {
    /**
     *
     */
    @FXML private AnchorPane ap_root;
    /**
     *
     */
    @FXML private Label l_mail;
    /**
     *
     */
    @FXML private Label l_username;
    /**
     *
     */
    @FXML private Label l_nome;
    /**
     *
     */
    @FXML private Label l_cognome;
    /**
     *
     */
    @FXML private Label l_codiceFiscale;
    /**
     *
     */
    @FXML private Label l_idVaccinazione;
    /**
     *
     */
    @FXML private PasswordField pf_vecchiaPassword;
    /**
     *
     */
    @FXML private TextField tf_vecchiaPassword;
    /**
     *
     */
    @FXML private PasswordField pf_nuovaPassword;
    /**
     *
     */
    @FXML private PasswordField pf_confNuovaPassword;
    /**
     *
     */
    @FXML private HBox hb_textField;
    /**
     *
     */
    @FXML private HBox hb_passwordField;
    /**
     *
     */
    private final ClientCV client = CentriVaccinali.client;
    /**
     *
     */
    private final ControlloParametri cp = ControlloParametri.getInstance();
    /**
     *
     */
    private CIDashboardController parent;
    /**
     *
     */
    private DialogHelper dh;

    /**
     *
     */
    @FXML
    private void initialize() {
        Cittadino c = this.client.getUtenteLoggato();
        l_mail.setText(c.getEmail());
        l_username.setText(c.getUserid());
        l_nome.setText(c.getNome());
        l_cognome.setText(c.getCognome());
        l_codiceFiscale.setText(c.getCodice_fiscale());
        l_idVaccinazione.setText(String.valueOf(c.getId_vaccinazione()));
    }

    /**
     *
     * @param c
     */
    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }

    /**
     *
     * @param result
     */
    @Override
    public void notifyController(Result result) {
        Platform.runLater(() -> {
            if (result.getResult()) {
                dh = new DialogHelper("Password aggiornata", "La tua password e' stata aggiornata correttamente", DialogHelper.Type.INFO);
            } else {
                dh = new DialogHelper("Attenzione", "La vecchia password immessa non e' corretta", DialogHelper.Type.WARNING);
            }
            dh.display();
        });
    }

    /**
     *
     */
    @FXML
    private void chiudiFragment() {
        parent.rimuoviFragment(ap_root);
    }

    /**
     *
     */
    @FXML
    private void aggiornaPassword() {
        if(hb_textField.isVisible()) {
            nascondiPassword();
        }
        if((cp.password(pf_vecchiaPassword) || cp.password(tf_vecchiaPassword)) && cp.password(pf_nuovaPassword) && cp.password(pf_confNuovaPassword)) {
            if(cp.checkSamePassword(pf_nuovaPassword, pf_confNuovaPassword)) {
                client.aggiornaPassword(this, client.getUtenteLoggato().getUserid(), cp.encryptPassword(pf_vecchiaPassword.getText().trim()), cp.encryptPassword(pf_confNuovaPassword.getText().trim()));
            }
        }
    }

    /**
     *
     */
    @FXML
    private void mostraPassword() {
        tf_vecchiaPassword.setText(pf_vecchiaPassword.getText());
        hb_textField.setVisible(true);
        hb_passwordField.setVisible(false);
    }

    /**
     *
     */
    @FXML
    private void nascondiPassword() {
        pf_vecchiaPassword.setText(tf_vecchiaPassword.getText());
        hb_passwordField.setVisible(true);
        hb_textField.setVisible(false);
    }
}
