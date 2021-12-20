package it.uninsubria.centrivaccinali.controller.cittadini.dashboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 *
 */
public class CIItemListController extends Controller {
    /**
     *
     */
    @FXML private HBox hb_aggiungi_e_visualizza;
    /**
     *
     */
    @FXML private HBox hb_visualizza;
    /**
     *
     */
    @FXML private FontIcon fi_iconaCentro;
    /**
     *
     */
    @FXML private Label l_nomeCentro;
    /**
     *
     */
    @FXML private Label l_indirizzoCentro;
    /**
     *
     */
    @FXML private Label l_tipologiaCentro;
    /**
     *
     */
    private final ClientCV client = CentriVaccinali.client;
    /**
     *
     */
    private CIDashboardController parent;
    /**
     *
     */
    private CentroVaccinale item;

    /**
     *
     * @param result
     */
    @Override
    public void notifyController(Result result) {  }

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
     *
     */
    @FXML
    private void aggiungiEvento() {
        parent.aggiungiEvento();
    }

    /**
     *
     */
    @FXML
    private void visualizzaInfo() {
        parent.visualizzaInfoCentro(item);
    }

    /**
     *
     * @param c
     */
    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }
}
