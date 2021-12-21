package it.uninsubria.centrivaccinali.controller.cittadini.dashboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * Item della lista della ricerca di un centro vaccinale
 * @author ...
 */
public class CIItemListController extends Controller {
    /**
     * <code>HBox</code> visualizzato quando l'utente ha la possibilit&agrave di inserire gli eventi avversi
     * per il centro vaccinale oltre alla visualizzazione delle informaione di esso.
     */
    @FXML private HBox hb_aggiungi_e_visualizza;
    /**
     * <code>HBox</code> visualizzato quando l'utente ha la sola possibilit&agrave di visualizzare le informaizone del
     * centro vaccinale.
     */
    @FXML private HBox hb_visualizza;
    /**
     * <code>FontIcon</code> Icone che mostra la tipologia del centro.
     */
    @FXML private FontIcon fi_iconaCentro;
    /**
     * <code>Label</code> per il nome del centro vaccinale.
     */
    @FXML private Label l_nomeCentro;
    /**
     * <code>Label</code> per l'indirizzo del centro vaccinale.
     */
    @FXML private Label l_indirizzoCentro;
    /**
     * <code>Label</code> per la tipologia del centro vaccinale.
     */
    @FXML private Label l_tipologiaCentro;
    /**
     * Riferimento al client su cui si sta eseguendo l'applicazione.
     * @see ClientCV
     */
    private final ClientCV client = CentriVaccinali.client;
    /**
     * Riferimento al controller della dashboard per la possibilt&agrave di chiamare i suoi metodi
     * @see CIDashboardController
     */
    private CIDashboardController parent;
    /**
     * Centro vaccinale di questo item della lista.
     * @see CentroVaccinale
     */
    private CentroVaccinale item;

    //Metodo ereditato dalla superclasse
    @Override
    public void notifyController(Result result) {  }

    /**
     * Metodo per l'inserimento dei dati del centro vaccinale nell'item.
     * @param cv Centro  vaccinale da cui prendere i dati.
     * @param iconLiteral stringa per il codice della FontIcon da usare in base alla tipologia.
     */
    public void setData(CentroVaccinale cv, String iconLiteral) {
        item = cv;
        fi_iconaCentro.setIconLiteral(iconLiteral);
        l_nomeCentro.setText(cv.getNome());
        l_indirizzoCentro.setText(String.valueOf(cv.getIndirizzo()));
        l_tipologiaCentro.setText(String.valueOf(cv.getTipologia()));
         if(client.getCentroCittadino().equals(cv.getNome())) {
             hb_aggiungi_e_visualizza.setVisible(true);
             hb_visualizza.setVisible(false);
         }
    }

    /**
     * Metodo associato al button per l'inserimento degli eventi avversi chimando il metodo del controller della
     * dasboard.
     * @see CIDashboardController
     */
    @FXML
    private void aggiungiEvento() {
        parent.aggiungiEvento();
    }

    /**
     * Metodo associato al button per la visualizzazione delle informazione del centro vaccinale chimando il metodo
     * del comtroller della dashboard.
     * @see CIDashboardController
     */
    @FXML
    private void visualizzaInfo() {
        parent.visualizzaInfoCentro(item);
    }

    /**
     * Mettodo per settare la variabile <code>parent</code>.
     * @param c controller che setter&agrave la variabile.
     */
    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }
}
