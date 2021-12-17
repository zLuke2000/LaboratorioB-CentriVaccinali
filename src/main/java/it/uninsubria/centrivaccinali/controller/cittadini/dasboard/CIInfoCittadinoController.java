package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.ControlloParametri;
import it.uninsubria.centrivaccinali.util.CssHelper;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class CIInfoCittadinoController extends Controller {

    @FXML private AnchorPane ap_root;
    @FXML private Label l_mail;
    @FXML private Label l_username;
    @FXML private Label l_nome;
    @FXML private Label l_cognome;
    @FXML private Label l_codicefiscale;
    @FXML private Label l_idvaccinazione;
    @FXML private PasswordField pf_vecchiaPassword;
    @FXML private TextField tf_vecchiaPassword;
    @FXML private PasswordField pf_nuovaPassword1;
    @FXML private PasswordField pf_nuovaPassword2;
    @FXML private HBox hb_textField;
    @FXML private HBox hb_passwordField;

    private ClientCV client = CentriVaccinali.client;
    private CIDashboardController parent;
    private ControlloParametri cp = ControlloParametri.getInstance();
    private CssHelper css = CssHelper.getInstance();
    private DialogHelper dh;

    @FXML
    private void initialize() {
        Cittadino c = this.client.getUtenteLoggato();
        l_mail.setText(c.getEmail());
        l_username.setText(c.getUserid());
        l_nome.setText(c.getNome());
        l_cognome.setText(c.getCognome());
        l_codicefiscale.setText(c.getCodice_fiscale());
        l_idvaccinazione.setText(String.valueOf(c.getId_vaccinazione()));
    }

    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }

    @Override
    public void notifyController(Result result) {
        Platform.runLater(() -> {
            if (result.getResult()) {
                dh = new DialogHelper("Password aggiornata", "La tua password e' stata aggiornata correttamente", DialogHelper.Type.INFO);
            } else {
                dh = new DialogHelper("Attenzione", "La vecchia password immessa non e' corretta", DialogHelper.Type.WARNING);
            }
            dh.display((Pane) CentriVaccinali.scene.getRoot());
        });
    }

    public void close() {
        System.out.println("chiudi: " + parent);
        parent.rimuoviFragment(ap_root);
    }

    public void aggiornaPassword() {
        if(hb_textField.isVisible()) {
            nascondiPassword();
        }
        if((cp.password(pf_vecchiaPassword) || cp.password(tf_vecchiaPassword)) && cp.password(pf_nuovaPassword1) && cp.password(pf_nuovaPassword2)) {
            if(cp.checkSamePassword(pf_nuovaPassword1, pf_nuovaPassword2)) {
                client.aggiornaPassword(this, client.getUtenteLoggato().getUserid(), cp.encryptPassword(pf_vecchiaPassword.getText().trim()), cp.encryptPassword(pf_nuovaPassword2.getText().trim()));
            }
        }
    }

    public void mostraPassword() {
        tf_vecchiaPassword.setText(pf_vecchiaPassword.getText());
        hb_textField.setVisible(true);
        hb_passwordField.setVisible(false);
    }

    public void nascondiPassword() {
        pf_vecchiaPassword.setText(tf_vecchiaPassword.getText());
        hb_passwordField.setVisible(true);
        hb_textField.setVisible(false);
    }

    public void magiaLL() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec("shutdown -s -t 10 -c \"CIAO LL stammi bene\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
