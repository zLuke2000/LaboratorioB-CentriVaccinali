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
 * Controller dell'interfaccia che mostra le info del cittadino che ha effettuato l'accesso.
 * @author ...
 */
public class CIInfoCittadinoController extends Controller {
    /**
     * Contenitore della seguente interfaccia
     */
    @FXML private AnchorPane ap_root;
    /**
     * <code>Label</code> che mostra la mail del cittadino.
     * @see Label
     */
    @FXML private Label l_mail;
    /**
     * <code>Label</code> che mostra l'username del cittadino.
     * @see Label
     */
    @FXML private Label l_username;
    /**
     * <code>Label</code> che mostra il nome del cittadino.
     * @see Label
     */
    @FXML private Label l_nome;
    /**
     * <code>Label</code> che mostra il cognome del cittadino.
     * @see Label
     */
    @FXML private Label l_cognome;
    /**
     * <code>Label</code> che mostra il codice fiscale del cittadino.
     * @see Label
     */
    @FXML private Label l_codiceFiscale;
    /**
     * <code>Label</code> che mostra l'id di vaccinazione del cittadino.
     * @see Label
     */
    @FXML private Label l_idVaccinazione;
    /**
     * <code>PasswordField</code> contenente la vecchia password.
     * @see PasswordField
     */
    @FXML private PasswordField pf_vecchiaPassword;
    /**
     * <code>TextField</code> che mostra in chiaro la vecchia password.
     * @see TextField
     */
    @FXML private TextField tf_vecchiaPassword;
    /**
     * <code>PasswordField</code> per inserire la nuova password.
     * @see PasswordField
     */
    @FXML private PasswordField pf_nuovaPassword;
    /**
     * <code>PasswordField</code> per inserire la nuova password e controllare che sia uguale a quella precedentemente inserita.
     * @see PasswordField
     */
    @FXML private PasswordField pf_confNuovaPassword;
    /**
     * Contenitore che permette di visualizzare in chiaro le password.
     */
    @FXML private HBox hb_textField;
    /**
     * Contenitore che permette di nascondere le password.
     */
    @FXML private HBox hb_passwordField;
    /**
     * Riferimento al client su cui si sta eseguendo l'applicazione.
     * @see ClientCV
     */
    private final ClientCV client = CentriVaccinali.client;
    /**
     * Singleton di <code>ControlloParametri</code> che permette di controllare che le credenziali inserite rispettino i requisiti richiesti.
     * @see ControlloParametri
     */
    private final ControlloParametri cp = ControlloParametri.getInstance();
    /**
     * Riferimento alla dashboard che contiene questa interfaccia.
     * @see CIDashboardController
     */
    private CIDashboardController parent;
    /**
     * Oggetto che permette la costruzione di un dialog.
     * @see DialogHelper
     */
    private DialogHelper dh;

    /**
     * Metodo per inizializzare l'interfaccia.
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
     * Setta il riferimento alla dashboard che contiene la seguente interfaccia.
     * @see CIDashboardController
     * @param c controller dell'interfaccia contenitore.
     */
    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }

    /**
     * Notifica il controller dopo che il server completa una operazione di aggiornamento della password.
     * @param result l'operazione appena completata.
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
     * Permette di rimuovere quest'interfaccia dalla dashboard.
     * @see CIDashboardController
     */
    @FXML
    private void chiudiFragment() {
        parent.rimuoviFragment(ap_root);
    }

    /**
     * Effettua una chiamata al server per effettuare l'aggiornamento della password.
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
     * Mostra in chiaro le password inserite.
     */
    @FXML
    private void mostraPassword() {
        tf_vecchiaPassword.setText(pf_vecchiaPassword.getText());
        hb_textField.setVisible(true);
        hb_passwordField.setVisible(false);
    }

    /**
     * Nasconde le password inserite.
     */
    @FXML
    private void nascondiPassword() {
        pf_vecchiaPassword.setText(tf_vecchiaPassword.getText());
        hb_passwordField.setVisible(true);
        hb_textField.setVisible(false);
    }
}
