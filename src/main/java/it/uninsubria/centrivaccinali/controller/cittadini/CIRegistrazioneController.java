package it.uninsubria.centrivaccinali.controller.cittadini;

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
import javafx.scene.Cursor;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;

/**
 * Controller dell'interfaccia di registrazione di un cittadino
 */
public class CIRegistrazioneController extends Controller {

    /**
     * <code>TextField</code> per il nome del cittadino
     */
    @FXML private TextField tf_nome;
    /**
     * <code>TextField</code> per il cognome del cittadino
     */
    @FXML private TextField tf_cognome;
    /**
     * <code>TextField</code> per il codice fiscale del cittadino
     */
    @FXML private TextField tf_codiceFiscale;
    /**
     * <code>TextField</code> per l'username del cittadino
     */
    @FXML private TextField tf_username;
    /**
     * <code>TextField</code> per l'e-mail del cittadino
     */
    @FXML private TextField tf_email;
    /**
     * <code>TextField</code> per l'ID del cittadino
     */
    @FXML private TextField tf_idVaccinazione;

    /**
     * <code>PasswordField</code> per il primo inserimento della password del cittadino
     */
    @FXML private PasswordField pf_password1;
    /**
     * <code>PasswordField</code> per il secondo inserimento della password del cittadino
     */
    @FXML private PasswordField pf_password2;
    /**
     * Riferiomento al client su cui si sta eseguendo l'applicazione
     * @see ClientCV
     */
    private final ClientCV client = CentriVaccinali.client;
    /**
     * Singleton di <code>ControlloParametri</code> che permette di controllare che le credenziali inserite rispettino i requisiti richiesti.
     * @see ControlloParametri
     */
    private final ControlloParametri cp = ControlloParametri.getInstance();
    /**
     * Rifermento al singleton <code>CssHelper</code> che permette la gestione degli stili per i vari componenti grafici.
     * @see CssHelper
     */
    private final CssHelper css = CssHelper.getInstance();

    /**
     * Permette di notificare l'interfaccia quando un'operazione registrazione viene effettuata o meno, specificando l'errore
     * @param result
     */
    @Override
    public void notifyController(Result result) {
        CentriVaccinali.scene.setCursor(Cursor.DEFAULT);
        if (result.getResult()) {
            System.out.println("Registrazione effettuato");
            Platform.runLater(() -> CentriVaccinali.setRoot("CI_dashboard"));
        } else {
            System.err.println("Registrazione fallita");

            if (result.getExtendedResult().contains(Result.Error.CF_ID_NON_VALIDI)) {
                css.toError(tf_codiceFiscale, new Tooltip("ID vaccinazione e codice fiscale non associati ad alcun vaccinato"));
                css.toError(tf_idVaccinazione, new Tooltip("ID vaccinazione e codice fiscale non associati ad alcun vaccinato"));
            }
            if (result.getExtendedResult().contains(Result.Error.CITTADINO_GIA_REGISTRATO)) {
                new DialogHelper("ERRORE", "Il cittadino corrispondente a questo codice fiscale e a questo id vaccinazione e' gia' stato registrato", DialogHelper.Type.ERROR).display();
                System.err.println("Cittadino gia' registrato");
            }
            if (result.getExtendedResult().contains(Result.Error.EMAIL_GIA_IN_USO)) {
                css.toError(tf_email, new Tooltip("Email gia' registrata"));
            }
            if (result.getExtendedResult().contains(Result.Error.USERID_GIA_IN_USO)) {
                css.toError(tf_username, new Tooltip("Username gia' registrato"));
            }
        }
    }

    /**
     * Metodo per la regitrazione cittadino controllando la correttezza dei paramentri inseriti
     */
    @FXML
    private void registraCittadino() {
        if (cp.testoSempliceSenzaNumeri(tf_nome, 2, 50) &
            cp.testoSempliceSenzaNumeri(tf_cognome, 2, 50) &
            cp.codiceFiscale(tf_codiceFiscale) &
            cp.email(tf_email) &
            cp.testoSempliceConNumeri(tf_username, 4, 16) &
            cp.password(pf_password1) &&
            cp.checkSamePassword(pf_password1, pf_password2) &
            cp.numeri(tf_idVaccinazione, 16, 16)) {
                String nome = tf_nome.getText().trim();
                String cognome = tf_cognome.getText().trim();
                String cf = tf_codiceFiscale.getText().trim();
                String email = tf_email.getText().trim();
                String user = tf_username.getText().trim();
                String password = pf_password1.getText().trim();
                long idVac = Long.parseLong(tf_idVaccinazione.getText());
                Cittadino cittadino = new Cittadino(nome, cognome, cf, email, user, cp.encryptPassword(password), idVac);
                System.out.println("Registro cittadino");
                CentriVaccinali.scene.setCursor(Cursor.WAIT);
                client.registraCittadino(this, cittadino);
        }
    }

    /**
     * Metodo per l'apertura del dialog che mostra i parametri che l'username deve rispettare
     */
    @FXML
    private void mostraInfoUsername() {
        new DialogHelper("INFO USERNAME", "La password deve avere almeno: " +
                "\n- tra 4 e 16 caratteri \n- non può contenere caratteri speciali \n- può contenere maiuscole e numeri", DialogHelper.Type.INFO).display();
    }

    /**
     * Metodo per l'apertura del dialog che mostra i parametri che la password deve rispettare
     */
    @FXML
    private void mostraInfoPassword() {
        new DialogHelper("INFO PASSWORD", "La password deve avere almeno: " +
                "\n- 8 caratteri \n- 1 lettera maiuscola \n- 1 lettera minuscola \n- 1 numero", DialogHelper.Type.INFO).display();
    }

    /**
     * Metodo per l'apertura del dialog che mostra i l'nformazione dell'ID vaccinazione
     */
    @FXML
    private void mostraInfoVaccinazione() {
        new DialogHelper("INFO ID", "L'ID della vaccinazione è stato fornito al momento della somministrazione", DialogHelper.Type.INFO).display();
    }

    /**
     *  Metodo per il controllo real-time dei parametri che si stanno inserendo
     * @param keyEvent l'evento sollevato inserendo da tastiera le credenziali.
     */
    @FXML
    private void realtimeCheck(KeyEvent keyEvent) {
        Object key = keyEvent.getSource();
        if (tf_nome.equals(key)) {
            cp.testoSempliceConNumeri(tf_nome, 2, 50);
        }
        if (tf_cognome.equals(key)) {
            cp.testoSempliceSenzaNumeri(tf_cognome, 2, 50);
        }
        if(tf_codiceFiscale.equals(key)) {
            cp.codiceFiscale(tf_codiceFiscale);
        }
        if(tf_email.equals(key)) {
            cp.email(tf_email);
        }
        if(pf_password1.equals(key)) {
            cp.password(pf_password1);
            if(!pf_password2.getText().isBlank()) {
                cp.checkSamePassword(pf_password1, pf_password2);
            }
        }
        if(pf_password2.equals(key)) {
            cp.checkSamePassword(pf_password1, pf_password2);
        }
        if(tf_username.equals(key)) {
            cp.testoSempliceConNumeri(tf_username, 4, 16);
        }
        if(tf_idVaccinazione.equals(key)) {
            cp.numeri(tf_idVaccinazione, 16, 16);
        }
    }

    /**
     * Metodo per tornare all'interfaccia precedente
     */
    @FXML
    private void backTo() {
        CentriVaccinali.setRoot("CI_home");
    }

    /**
     * Permette la chiusura dell'applicazione tramite la chiamata alla superclasse.
     * @see Controller
     */
    @FXML
    private void chiudiApp() {
        super.closeApp();
    }

}