package it.uninsubria.centrivaccinali.controller.cittadini.dashboard;

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

/**
 * Controller della interfaccia di ricerca dei centri vaccinali.
 * @author ...
 */
public class CIRicercaResultController extends Controller {
    /**
     * <code>Label</code> per indicare che non ci sono risultati in base alla ricerca effettuata.
     * @see Label
     */
    @FXML private Label l_noResult;
    /**
     * Contenitore della lista dei risultati di ricerca.
     */
    @FXML private VBox vb_risultati;
    /**
     * <code>TextField</code> per inserire il nome del centro da cercare.
     * @see TextField
     */
    @FXML private TextField tf_ricercaNomeCentro;
    /**
     * <code>ComboBox</code> per selezionare la tipologia di ricerca che si vuole effettuare.
     * @see ComboBox
     */
    @FXML private ComboBox<String> cb_sceltaRicerca;
    /**
     * <code>ComboBox</code> per selezionare la tipologia di centro vaccinale che si vuole cercare.
     * @see ComboBox
     */
    @FXML private ComboBox<TipologiaCentro> cb_sceltaTipologia;
    /**
     * <code>TextField</code> per inserire il comune che si vuole cercare.
     * @see TextField
     */
    @FXML private TextField tf_ricercaComune;
    /**
     * Contatore per il numero di risultati di tipo "ospedaliero".
     */
    @FXML private Label l_countOspedaliero;
    /**
     * Contatore per il numero di risultati di tipo "hub".
     */
    @FXML private Label l_countHub;
    /**
     * Contatore per il numero di risultati di tipo "aziendale".
     */
    @FXML private Label l_countAziendale;
    /**
     * Riferimento al client su cui si sta eseguendo l'applicazione.
     * @see ClientCV
     */
    private final ClientCV client = CentriVaccinali.client;
    /**
     * Riferimento alla dashboard che contiene questa interfaccia.
     * @see CIDashboardController
     */
    private CIDashboardController parent;

    /**
     * Metodo per inizializzare l'interfaccia.
     */
    @FXML
    private void initialize () {
        this.cb_sceltaRicerca.getItems().addAll("Per nome", "Per comune e tipologia");
        this.cb_sceltaRicerca.getSelectionModel().selectFirst();
        this.cb_sceltaTipologia.getItems().addAll(TipologiaCentro.values());
        this.cb_sceltaTipologia.getSelectionModel().selectFirst();
    }

    /**
     * Notifica l'interfaccia a seguito di una operazione di ricerca.
     * @param result l'operazione appena completata.
     */
    @Override
    public void notifyController(Result result) {
        if (result != null && result.getResult() && result.getOpType() == Result.Operation.RICERCA_CENTRO) {
            setData(result.getList(CentroVaccinale.class));
        }
        CentriVaccinali.scene.setCursor(Cursor.DEFAULT);
    }

    /**
     * Setta il riferimento alla dashboard che contiene la seguente interfaccia.
     * @see CIDashboardController
     * @param parent controller dell'interfaccia contenitore.
     */
    public void setParent(CIDashboardController parent) {
        this.parent = parent;
    }

    /**
     * Effettua una chiamata al server per fare la ricerca.
     */
    @FXML
    private void cercaCentroVaccinale() {
        CentriVaccinali.scene.setCursor(Cursor.WAIT);
        if (cb_sceltaRicerca.getValue().equals("Per nome") && !tf_ricercaNomeCentro.getText().isBlank()) {
            client.ricercaPerNome(this, tf_ricercaNomeCentro.getText());
        }
        else if (cb_sceltaRicerca.getValue().equals("Per comune e tipologia") && !tf_ricercaComune.getText().isBlank()) {
            client.ricercaPerComuneTipologia(this, tf_ricercaComune.getText(), cb_sceltaTipologia.getValue());
        }
    }

    /**
     * Popola la lista dei risultati di ricerca.
     * @param list lista ottenuta completando la ricerca.
     */
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
                        switch (cv.getTipologia()){
                            case OSPEDALIERO:
                                itemController.setData(cv, "mdi2h-hospital-building:32:#3456e3");
                                countOspedaliero++;
                                break;
                            case HUB:
                                itemController.setData(cv, "mdi2h-hospital-marker:32:#c148eb");
                                countHub++;
                                break;
                            case AZIENDALE:
                                itemController.setData(cv, "mdi2f-factory:32:#323232");
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

    /**
     * Permette di cambiare la modalit&agrave di ricerca.
     */
    @FXML
    private void cambiaRicerca() {
        if (cb_sceltaRicerca.getValue().equals("Per nome")) {
            tf_ricercaNomeCentro.clear();
            tf_ricercaNomeCentro.setVisible(true);
            cb_sceltaTipologia.setVisible(false);
            tf_ricercaComune.setVisible(false);
        } else if (cb_sceltaRicerca.getValue().equals("Per comune e tipologia")) {
            cb_sceltaTipologia.getSelectionModel().selectFirst();
            tf_ricercaComune.clear();
            tf_ricercaNomeCentro.setVisible(false);
            cb_sceltaTipologia.setVisible(true);
            tf_ricercaComune.setVisible(true);
        }
    }
}
