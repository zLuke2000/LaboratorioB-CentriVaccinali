package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class CIDashboardController extends Controller {

    private Cittadino cittadinoConesso = null;
    private ClientCV client;
    private CIRicercaResultController ricercaController = null;

    @FXML private AnchorPane ap_root;
    @FXML private MenuButton mb_utente;
    @FXML private Pane p_container;

    private AnchorPane ap_ricerca;

    @FXML void initialize() {
        this.client = CentriVaccinali.client;
        cittadinoConesso = client.getUtenteLoggato();
        if(cittadinoConesso != null) {
            mb_utente.setVisible(true);
            mb_utente.setText(cittadinoConesso.getUserid());
        } else {
            mb_utente.setVisible(false);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/dashboard/RicercaCentri.fxml"));
        try {
            AnchorPane ap = fxmlLoader.load();
            ap_ricerca = ap;
            p_container.getChildren().add(ap);
            ricercaController = fxmlLoader.getController();
            ricercaController.setParent(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyController(Result result) { }

    public void visualizzaInfoCentro(CentroVaccinale cv) {
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/dashboard/InformazioniCentro.fxml"));
        try {
            AnchorPane ap = fxmlLoader.load();
            CIInfoCentroController controller = fxmlLoader.getController();
            controller.setData(cv);
            controller.setParent(this);
            ap_ricerca.setVisible(false);
            p_container.getChildren().add(ap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void aggiungiEvento() {
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/dashboard/RegistraEventoAvverso.fxml"));
        try {
            GridPane gp = fxmlLoader.load();
            AggiungiEventoAvverso controller = fxmlLoader.getController();
            controller.setParent(this, ap_root);
            ap_ricerca.setVisible(false);
            p_container.getChildren().add(gp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void infoCittadino() {
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

    public void rimuoviFragment(Pane p) {
        p_container.getChildren().remove(p);
        p_container.getChildren().get(p_container.getChildren().size() - 1).setVisible(true);
    }

    @FXML
    public void logout() {
        mb_utente.hide();
        CentriVaccinali.setRoot("CI_home");
        client.LogoutUtente();
    }

    @FXML
    public void backTo() {
        if (cittadinoConesso != null) {
            CentriVaccinali.setRoot("Avvio");
        } else {
            CentriVaccinali.setRoot("CI_home");
        }
        client.stopOperation();
    }

    public void chiudi() {
        super.closeApp(client);
    }

}

