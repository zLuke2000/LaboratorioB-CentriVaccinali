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
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.w3c.dom.events.Event;

import java.io.IOException;

public class CISegnalazioniController extends Controller {

    private ClientCV client = CentriVaccinali.client;
    private CIInfoCentroController parent;
    private int limit;
    private int offset;
    private CentroVaccinale centro;

    @FXML public VBox vb_lista_segnalazioni;
    private Button btnCarica = new Button("Carica altro");

    @FXML void initialize() {
        limit = 50;
        offset = 0;
        btnCarica.getStyleClass().add("button-preset-1");
        btnCarica.setOnAction(e -> {
            System.out.println("on action");
            client.leggiSegnalazioni(this, centro.getNome(), limit, offset);
        });
    }

    @Override
    public void initParameter(ClientCV client) {  }

    @Override
    public void notifyController(Result result) {
        if (result != null && result.getResult() && result.getOpType() == Result.Operation.LEGGI_EVENTI_AVVERSI) {
            System.out.println(result.getListaEA().isEmpty());
            if (!result.getListaEA().isEmpty()) {
                Platform.runLater(() -> vb_lista_segnalazioni.getChildren().remove(btnCarica));
                for (EventoAvverso ea: result.getListaEA()) {
                    FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/dashboard/itemListProspetto.fxml"));
                    try {
                        GridPane gp_item = fxmlLoader.load();
                        CIItemListProspettoController itemController = fxmlLoader.getController();
                        itemController.setData(ea);
                        Platform.runLater(() -> vb_lista_segnalazioni.getChildren().add(gp_item));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //TODO dopo prima lettura, metto limit 20
                if (result.getListaEA().size() < limit) {
                    Platform.runLater(() -> vb_lista_segnalazioni.getChildren().remove(btnCarica));
                } else {
                    Platform.runLater(() -> vb_lista_segnalazioni.getChildren().add(btnCarica));
                    limit = 20;
                }
                offset += result.getListaEA().size();
            } else {
                Platform.runLater(() -> vb_lista_segnalazioni.getChildren().remove(btnCarica));
            }
        }
    }

    public void setData(CentroVaccinale cv) {
        client.leggiSegnalazioni(this, cv.getNome(), limit, offset);
        centro = cv;
    }

    public void setParent(Controller c) {
        parent = (CIInfoCentroController) c;
    }
}
