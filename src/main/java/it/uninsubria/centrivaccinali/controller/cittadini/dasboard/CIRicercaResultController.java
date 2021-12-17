package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import java.io.IOException;
import java.util.List;

public class CIRicercaResultController extends Controller {

    @FXML private AnchorPane ap_result;
    @FXML private Label l_noResult;
    @FXML private VBox vb_risultati;
    @FXML private TextField ci_tf_ricercaNomeCV;
    @FXML private ComboBox<String> ci_cb_sceltaRicerca;
    @FXML private ComboBox<TipologiaCentro> ci_cb_sceltaTipologia;
    @FXML private TextField ci_tf_ricercaComune;
    @FXML private Label l_countOspedaliero;
    @FXML private Label l_countHub;
    @FXML private Label l_countAziendale;

    private ClientCV client = CentriVaccinali.client;
    private CIDashboardController parent;

    @FXML
    private void initialize () {
        this.ci_cb_sceltaRicerca.getItems().addAll("Per nome", "Per comune e tipologia");
        this.ci_cb_sceltaRicerca.getSelectionModel().selectFirst();
        this.ci_cb_sceltaTipologia.getItems().addAll(TipologiaCentro.values());
        this.ci_cb_sceltaTipologia.getSelectionModel().selectFirst();
    }

    @Override
    public void notifyController(Result result) {
        if (result != null && result.getResult() && result.getOpType() == Result.Operation.RICERCA_CENTRO) {
            setData(result.getResultCentri());
        }
        CentriVaccinali.scene.setCursor(Cursor.DEFAULT);
    }

    public void setParent(CIDashboardController parent) {
        this.parent = parent;
    }

    @FXML
    private void cercaCentroVaccinale() {
        CentriVaccinali.scene.setCursor(Cursor.WAIT);
        if (ci_cb_sceltaRicerca.getValue().equals("Per nome") && !ci_tf_ricercaNomeCV.getText().isBlank()) {
            client.ricercaPerNome(this, ci_tf_ricercaNomeCV.getText());
        }
        else if (ci_cb_sceltaRicerca.getValue().equals("Per comune e tipologia") && !ci_tf_ricercaComune.getText().isBlank()) {
            client.ricercaPerComuneTipologia(this, ci_tf_ricercaComune.getText(), ci_cb_sceltaTipologia.getValue());
        }
    }

    private void setData(List<CentroVaccinale> list) {
        Platform.runLater(() -> {
            vb_risultati.getChildren().clear();
            if (list.isEmpty()) {
                l_noResult.setVisible(true);
                l_countOspedaliero.setText("0");
                l_countHub.setText("0");
                l_countAziendale.setText("0");
            }
            else {
                int countOspedaliero = 0;
                int countHub = 0;
                int countAziendale = 0;
                l_noResult.setVisible(false);
                for (CentroVaccinale cv : list) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/dashboard/ItemListRicerca.fxml"));
                        GridPane item = fxmlLoader.load();
                        CIItemListController itemController = fxmlLoader.getController();
                        itemController.setParent(parent);
                        //itemController.initParameter(client);
                        switch (cv.getTipologia()){
                            case OSPEDALIERO:
                                itemController.setData(cv, "mdi2h-hospital-building", "#3456e3");
                                countOspedaliero++;
                                break;
                            case HUB:
                                itemController.setData(cv, "mdi2h-hospital-marker", "#c148eb");
                                countHub++;
                                break;
                            case AZIENDALE:
                                itemController.setData(cv, "mdi2f-factory", "#323232");
                                countAziendale++;
                                break;

                        }
                        vb_risultati.getChildren().add(item);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                l_countOspedaliero.setText(String.valueOf(countOspedaliero));
                l_countHub.setText(String.valueOf(countHub));
                l_countAziendale.setText(String.valueOf(countAziendale));
            }
        });
    }

    @FXML
    private void cambiaRicerca() {
        if (ci_cb_sceltaRicerca.getValue().equals("Per nome")) {
            ci_tf_ricercaNomeCV.clear();
            ci_tf_ricercaNomeCV.setVisible(true);
            ci_cb_sceltaTipologia.setVisible(false);
            ci_tf_ricercaComune.setVisible(false);
        } else if (ci_cb_sceltaRicerca.getValue().equals("Per comune e tipologia")) {
            ci_cb_sceltaTipologia.getSelectionModel().selectFirst();
            ci_tf_ricercaComune.clear();
            ci_tf_ricercaNomeCV.setVisible(false);
            ci_cb_sceltaTipologia.setVisible(true);
            ci_tf_ricercaComune.setVisible(true);
        }
    }
}
