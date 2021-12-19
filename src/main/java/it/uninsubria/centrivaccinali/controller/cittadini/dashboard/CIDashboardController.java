package it.uninsubria.centrivaccinali.controller.cittadini.dashboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
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
 *
 */
public class CIDashboardController extends Controller {
    /**
     *
     */
    @FXML private MenuButton mb_utente;
    /**
     *
     */
    @FXML private Pane p_container;
    /**
     *
     */
    private final ClientCV client = CentriVaccinali.client;
    /**
     *
     */
    private Cittadino cittadinoConnesso = null;

    /**
     *
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

    /**
     *
     * @param result
     */
    @Override
    public void notifyController(Result result) {  }

    /**
     *
     * @param cv
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
     *
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
     *
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
     *
     * @param p
     */
    public void rimuoviFragment(Pane p) {
        p_container.getChildren().remove(p);
        p_container.getChildren().get(p_container.getChildren().size() - 1).setVisible(true);
    }

    /**
     *
     */
    @FXML
    private void logout() {
        mb_utente.hide();
        CentriVaccinali.setRoot("CI_home");
        client.LogoutUtente();
    }

    /**
     *
     */
    @FXML
    private void backTo() {
        if (cittadinoConnesso != null) {
            CentriVaccinali.setRoot("Avvio");
        } else {
            CentriVaccinali.setRoot("CI_home");
        }
        client.stopOperation();
    }

    /**
     *
     */
    @FXML
    private void chiudiApp() {
        super.closeApp();
    }

}