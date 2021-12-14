package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.EventoAvverso;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.w3c.dom.events.Event;

import java.io.IOException;

public class CISegnalazioniController extends Controller {

    private ClientCV client = CentriVaccinali.client;
    private CIDashboardController parent;

    @FXML public VBox vb_lista_segnalazioni;

    @Override
    public void initParameter(ClientCV client) {  }

    @Override
    public void notifyController(Result result) {
        if (result != null && result.getResult() && result.getOpType() == Result.Operation.LEGGI_EVENTI_AVVERSI) {
            System.out.println(result.getListaEA().isEmpty());
            if (!result.getListaEA().isEmpty()) {
                //TODO controllo su dim lista
                // dopo prima lettura, metto limit 20
                if (result.getListaEA().size() < 50) {
                    //TODO disabilita btn carica altro
                }
                for (EventoAvverso ea: result.getListaEA()) {
                    FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/itemListProspetto.fxml"));
                    try {
                        GridPane gp_item = fxmlLoader.load();
                        CIItemListProspettoController itemController = fxmlLoader.getController();
                        itemController.setData(ea);
                        Platform.runLater(() -> vb_lista_segnalazioni.getChildren().add(gp_item));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //TODO aggiungere btn carica altro
            }
        }
    }

    public void setData(CentroVaccinale cv) {
        client.leggiSegnalazioni(this, cv.getNome(), 50, 0);
    }

    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }
}
