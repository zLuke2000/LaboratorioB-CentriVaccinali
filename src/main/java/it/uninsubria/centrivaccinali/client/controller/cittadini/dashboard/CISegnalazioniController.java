//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.client.controller.cittadini.dashboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.client.controller.Controller;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.EventoAvverso;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;


/**
 * Controller per l'interfaccia delle segnalazioni dei cittadini.
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele 742495
 * @author Pintonello Christian 741112
 */
public class CISegnalazioniController extends Controller {


    /**
     * <code>VBox</code> in cui andra popolata con l'inserimento degli item di segnalazione.
     */
    @FXML private VBox vb_lista_segnalazioni;


    /**
     * Riferimento al client su cui si sta eseguendo l'applicazione.
     * @see ClientCV
     */
    private final ClientCV client = CentriVaccinali.client;


    /**
     *  <code>Button</code> button che permette di caricare altri item.
     */
    private final Button btnCarica = new Button("Carica altro");


    /**
     * Riferimento al centro vaccinale selezionato.
     * @see CentroVaccinale
     */
    private CentroVaccinale centro;


    /**
     * Indice di limit per la ricerca di item du database.
     */
    private int limit;


    /**
     * Indice di offset per la ricerca di itme su database
     */
    private int offset;


    /**
     * Metodo per inizializzare l'interfaccia.
     */
    @FXML
    private void initialize() {
        limit = 50;
        offset = 0;
        btnCarica.getStyleClass().add("button-preset-1");
        btnCarica.setOnAction(e -> client.leggiSegnalazioni(this, centro.getNome(), limit, offset));
    }


    /**
     * Notifica l'interfaccia dopo aver completato la lettura degli eventi avversi registrati.
     * @param result l'operazione appena completata.
     */
    @Override
    public void notifyController(Result result) {
        if (result != null && result.getResult() && result.getOpType() == Result.Operation.LEGGI_EVENTI_AVVERSI) {
            List<EventoAvverso> listaEA = result.getList(EventoAvverso.class);
            if (!listaEA.isEmpty()) {
                Platform.runLater(() -> vb_lista_segnalazioni.getChildren().remove(btnCarica));
                for (EventoAvverso ea: listaEA) {
                    FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/prospetto/itemListProspetto.fxml"));
                    try {
                        GridPane gp_item = fxmlLoader.load();
                        CIItemListProspettoController itemController = fxmlLoader.getController();
                        itemController.setData(ea);
                        Platform.runLater(() -> vb_lista_segnalazioni.getChildren().add(gp_item));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (listaEA.size() < limit) {
                    Platform.runLater(() -> vb_lista_segnalazioni.getChildren().remove(btnCarica));
                } else {
                    Platform.runLater(() -> vb_lista_segnalazioni.getChildren().add(btnCarica));
                    limit = 20;
                }
                offset += listaEA.size();
            } else {
                Platform.runLater(() -> vb_lista_segnalazioni.getChildren().remove(btnCarica));
            }
        }
    }


    /**
     * Legge le segnalazioni per un dato centro vaccinale.
     * @param cv il centro di cui si vogliono leggere le segnalazioni.
     */
    public void setData(CentroVaccinale cv) {
        client.leggiSegnalazioni(this, cv.getNome(), limit, offset);
        centro = cv;
    }
}
