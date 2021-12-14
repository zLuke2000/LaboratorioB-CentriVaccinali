package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class CIInfoCittadinoController extends Controller {
    @FXML private Label l_mail;
    @FXML private Label l_username;
    @FXML private Label l_nome;
    @FXML private Label l_cognome;
    @FXML private Label l_codicefiscale;
    @FXML private Label l_idvaccinazione;
    @FXML private PasswordField pf_nuovaPassword2;
    @FXML private PasswordField pf_nuovaPassword1;
    @FXML private TextField tf_vecchiaPassword;
    @FXML private HBox hb_textField;
    @FXML private PasswordField pf_vecchiaPassword;
    @FXML private HBox hb_passwordField;

    private ClientCV client;
    private CIDashboardController parent;

    @Override
    public void initParameter(ClientCV client) {
        this.client = client;
        Cittadino c = this.client.getUtenteLoggato();
        l_mail.setText(c.getEmail());
        l_username.setText(c.getUserid());
        l_nome.setText(c.getNome());
        l_cognome.setText(c.getCognome());
        l_codicefiscale.setText(c.getCodice_fiscale());
        l_idvaccinazione.setText(String.valueOf(c.getId_vaccino()));
    }

    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }

    @Override
    public void notifyController(Result result) {}

    public void close() {

    }

    public void aggiornaPassword() {
    }

    public void mostraPassword() {
        System.out.println("nascondo password");
        hb_textField.setVisible(true);
        hb_passwordField.setVisible(false);
    }

    public void nascondiPassword() {
        System.out.println("mostro password");
        hb_passwordField.setVisible(true);
        hb_textField.setVisible(false);
    }

}
