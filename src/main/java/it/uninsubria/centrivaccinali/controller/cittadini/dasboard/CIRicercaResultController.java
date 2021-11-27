package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.controller.cittadini.dasboard.CIDashboardController;
import it.uninsubria.centrivaccinali.controller.cittadini.dasboard.CIItemListController;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CIRicercaResultController extends Controller {

    private ClientCV client;

    private CIDashboardController parent;

    @FXML public VBox vb_risultati;

    @FXML private TextField ci_tf_ricercaNomeCV;

    @FXML private ComboBox<String> ci_cb_sceltaRicerca;

    @FXML private AnchorPane ci_ap_tc;

    @FXML private ComboBox<TipologiaCentro> ci_cb_sceltaTipologia;

    @FXML private TextField ci_tf_ricercaComune;

    private Image imgHub;
    private Image imgAziendale;
    private Image imgOspedaliero;

    //TODO settare la giusta ricerca e il testo della ricerca
    @FXML void initialize () {
        this.client = CentriVaccinali.client;

        this.ci_cb_sceltaRicerca.getItems().addAll("Per nome", "Per comune e tipologia");
        this.ci_cb_sceltaRicerca.getSelectionModel().selectFirst();
        this.ci_cb_sceltaTipologia.getItems().addAll(TipologiaCentro.values());
        this.ci_cb_sceltaTipologia.getSelectionModel().selectFirst();

        //imgHub = new Image(new FileInputStream(Objects.requireNonNull(CentriVaccinali.class.getResource("image/Hub.png")).toString()));
        imgHub = new Image(CentriVaccinali.class.getResourceAsStream("image/Hub.png"));
        imgAziendale = new Image(CentriVaccinali.class.getResourceAsStream("image/Aziendale.jpeg"));
        imgOspedaliero = new Image(CentriVaccinali.class.getResourceAsStream("image/Ospedaliero.png"));
        //imgAziendale = new Image(new FileInputStream(Objects.requireNonNull(CentriVaccinali.class.getResource("image/Aziendale.jpeg")).toString()));
        //imgOspedaliero = new Image(new FileInputStream(Objects.requireNonNull(CentriVaccinali.class.getResource("image/Ospedaliero.png")).toString()));
    }

    @Override
    public void initParameter(ClientCV client) {
        /*this.client = client;*/
    }

    @Override
    public void notifyController(Result result) { }

    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }

    @FXML
    public void cercaCentroVaccinale() {
        if (ci_cb_sceltaRicerca.getValue().equals("Per nome") && !ci_tf_ricercaNomeCV.getText().isBlank()) {
            client.ricercaPerNome(parent, ci_tf_ricercaNomeCV.getText());
        }
        else if (ci_cb_sceltaRicerca.getValue().equals("Per comune e tipologia") && !ci_tf_ricercaComune.getText().isBlank()) {
            client.ricercaPerComuneTipologia(parent, ci_tf_ricercaComune.getText(), ci_cb_sceltaTipologia.getValue());
        }
    }

    public void setData(List<CentroVaccinale> list) {
        Platform.runLater(() -> {
            vb_risultati.getChildren().clear();
            if (list.isEmpty()) {
                System.out.println("Nessun risultato");
                //TODO label nessun risultato
            }
            else {
                for (CentroVaccinale cv : list) {
                    FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/itemListResearch.fxml"));
                    try {
                        GridPane item = fxmlLoader.load();
                        CIItemListController itemController = fxmlLoader.getController();
                        switch (cv.getTipologia()){
                            case HUB:
                                itemController.setData(cv, imgHub);
                                break;
                            case AZIENDALE:
                                itemController.setData(cv, imgAziendale);
                                break;
                            case OSPEDALIERO:
                                itemController.setData(cv, imgOspedaliero);
                                break;
                        }
                        vb_risultati.getChildren().add(item);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @FXML
    private void cambiaRicerca() {
        if (ci_cb_sceltaRicerca.getValue().equals("Per nome")) {
            ci_tf_ricercaNomeCV.clear();
            ci_tf_ricercaNomeCV.setVisible(true);
            ci_ap_tc.setVisible(false);
        } else if (ci_cb_sceltaRicerca.getValue().equals("Per comune e tipologia")) {
            ci_tf_ricercaNomeCV.setVisible(false);
            ci_cb_sceltaTipologia.getSelectionModel().selectFirst();
            ci_tf_ricercaComune.clear();
            ci_ap_tc.setVisible(true);
        }
    }
}
