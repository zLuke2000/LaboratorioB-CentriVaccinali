package it.uninsubria.centrivaccinali.controller.cittadini.dashboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.Window;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.io.IOException;

/**
 * Controller per l'interfaccia della dashboard che gestisce l'aprtura delle varie interfacce.
 * @author ...
 */
public class CIDashboardController extends Controller {


    /**
     * <code>MenuButton</code> contenente l'username del cittadino connesso,
     * con la possibilita di fare il logout e la visualizzazione delle sue informazioni.
     */
    @FXML private MenuButton mb_utente;


    /**
     * <code>Pane</code> container su cui andra popolato con le interfaccie.
     */
    @FXML private Pane p_container;


    /**
     * Riferimento al client su cui si sta eseguendo l'applicazione.
     * @see ClientCV
     */
    private final ClientCV client = CentriVaccinali.client;


    /**
     * Riferiemento al cittadino al momento connesso.
     * @see Cittadino
     */
    private Cittadino cittadinoConnesso = null;


    /**
     * Metodo per inizializzare l'interfaccia.
     */
    @FXML
    private void initialize() {
        cittadinoConnesso = client.getUtenteLoggato();
        if(cittadinoConnesso != null) {
            mb_utente.setVisible(true);
            mb_utente.setText(cittadinoConnesso.getUserid());
        } else {
            mb_utente.setVisible(false);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/dashboard/RicercaCentri.fxml"));
        try {
            AnchorPane ap = fxmlLoader.load();
            p_container.getChildren().add(ap);
            CIRicercaResultController ricercaController = fxmlLoader.getController();
            ricercaController.setParent(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Metodo ereditato dalla superclasse
    @Override
    public void notifyController(Result result) {  }


    /**
     * Metodo per la visualizzazione dell'inforrmazioni del centro vaccinale selezionato.
     * @param cv centro vaccinale selezinato.
     * @see CentroVaccinale
     */
    public void visualizzaInfoCentro(CentroVaccinale cv) {
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/dashboard/InformazioniCentro.fxml"));
        try {
            AnchorPane ap = fxmlLoader.load();
            CIInfoCentroController controller = fxmlLoader.getController();
            controller.setData(cv);
            controller.setParent(this);
            p_container.getChildren().get(p_container.getChildren().size() - 1).setVisible(false);
            p_container.getChildren().add(ap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Metodo per l'apertura dell'interfaccia per l'inserimento degli eventi avversi, del centro vaccinale
     * su cui è possibile inserirli.
     */
    public void aggiungiEvento() {
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/dashboard/RegistraEventoAvverso.fxml"));
        try {
            GridPane gp = fxmlLoader.load();
            AggiungiEventoAvverso controller = fxmlLoader.getController();
            controller.setParent(this);
            p_container.getChildren().get(p_container.getChildren().size() - 1).setVisible(false);
            p_container.getChildren().add(gp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Metodo per l'apertura dell'interfaccia che mostra le insfromazioni del cittadino connesso.
     */
    @FXML
    private void infoCittadino() {
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/dashboard/InformazioniCittadino.fxml"));
        try {
            AnchorPane ap = fxmlLoader.load();
            Platform.runLater(() -> {
                p_container.getChildren().get(p_container.getChildren().size() - 1).setVisible(false);
                p_container.getChildren().add(ap);
            });
            ((CIInfoCittadinoController) fxmlLoader.getController()).setParent(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Metodo per la rimozione del fragment passato come paramentro, e la visualizzazione del fragment precedente.
     * @param p Pane da rimuovere.
     */
    public void rimuoviFragment(Pane p) {
        p_container.getChildren().remove(p);
        p_container.getChildren().get(p_container.getChildren().size() - 1).setVisible(true);
    }


    /**
     * Metodo per fare il logout da parte del cittadino connesso.
     */
    @FXML
    private void logout() {
        mb_utente.hide();
        Window.setRoot("CI_home");
        client.LogoutUtente();
    }


    /**
     * Metodo per tornare all'interfaccia precedente.
     */
    @FXML
    private void backTo() {
        if (cittadinoConnesso != null) {
            Window.setRoot("Avvio");
        } else {
            Window.setRoot("CI_home");
        }
        client.stopOperation();
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