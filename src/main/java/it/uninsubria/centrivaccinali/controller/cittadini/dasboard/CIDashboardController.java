package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

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

public class CIDashboardController extends Controller {

    private Cittadino cittadinoConesso = null;
    private ClientCV client;
    private Controller c = null;
    private CIRicercaResultController resultController = null;

    @FXML private AnchorPane ap_root;
    @FXML private MenuButton mb_utente;
    //Container
    @FXML private Pane p_container;

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
            p_container.getChildren().add(ap);
            CIRicercaResultController c = fxmlLoader.getController();
            c.setParent(this);
            c.initParameter(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initParameter(ClientCV client) {
        /*this.client =  client;
        cittadinoConesso = client.getUtenteLoggato();
        if(cittadinoConesso != null) {
            mb_utente.setVisible(true);
            vb_free.setVisible(false);
            mb_utente.setText(cittadinoConesso.getUserid());
        } else {
            mb_utente.setVisible(false);
            vb_free.setVisible(true);
        }
        //TODO sistemare magari fuori da initParameter
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class
                .getResource("fxml/fragments/fragmentDashboard/F_CI_ricercaHome.fxml"));
        try {
            AnchorPane ap = fxmlLoader.load();
            p_container.getChildren().add(ap);
            CIRicercaHomeController c = fxmlLoader.getController();
            c.setParent(this);
            c.initParameter(client);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void notifyController(Result result) {
        switch (result.getOpType()) {
            case RICERCA_CENTRO:
                if (resultController == null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/dashboard/RicercaCentri.fxml"));
                    try {
                        AnchorPane ap = fxmlLoader.load();
                        Platform.runLater(() -> {
                            p_container.getChildren().clear();
                            p_container.getChildren().add(ap);
                        });
                        resultController = fxmlLoader.getController();
                        resultController.setParent(this);
                        resultController.initParameter(client);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                resultController.setData(result.getResultCentri());
                break;
            default:
                break;
        }
    }

    public void visualizzaInfo(CentroVaccinale cv) {
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/dashboard/InformazioniCentro.fxml"));
        try {
            AnchorPane ap = fxmlLoader.load();
            CIInfoCentroController controller = fxmlLoader.getController();
            controller.setData(cv);
            controller.setParent(this);
            resultController.getPane().setVisible(false);
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
            controller.initParameter(client);
            resultController.getPane().setVisible(false);
            p_container.getChildren().add(gp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void rimuoviInfo(AnchorPane p){
        p_container.getChildren().remove(p);
        resultController.getPane().setVisible(true);
    }

    @FXML
    public void mostraInfo() {
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/dashboard/InformazioniCittadino.fxml"));
        try {
            AnchorPane ap = fxmlLoader.load();
            Platform.runLater(() -> {
                p_container.getChildren().clear();
                p_container.getChildren().add(ap);
            });
            c = fxmlLoader.getController();
            System.out.println(client);
            c.initParameter(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void logoutInfoMB() {
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

    public void rimuovi(GridPane gp_ea) {
        p_container.getChildren().remove(gp_ea);
        resultController.getPane().setVisible(true);
    }
}

