package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

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

public class CIItemListController extends Controller {

    @FXML private HBox hb_aggiungi_e_visualizza;
    @FXML private HBox hb_visualizza;
    @FXML private FontIcon ci_fi_image;
    @FXML private Label ci_tf_nomeCVItem;
    @FXML private Label ci_tf_indirizzoItem;
    @FXML private Label ci_l_tipologiaItem;

    private CIDashboardController parent;
    private CentroVaccinale item;
    private ClientCV client = CentriVaccinali.client;

    @Override
    public void notifyController(Result result) {  }

    public void setData(CentroVaccinale cv, String iconLiteral, String color) {
        item = cv;
        ci_fi_image.setIconLiteral(iconLiteral);
        ci_fi_image.setIconColor(Paint.valueOf(color));
        ci_tf_nomeCVItem.setText(cv.getNome());
        ci_tf_indirizzoItem.setText(String.valueOf(cv.getIndirizzo()));
        ci_l_tipologiaItem.setText(String.valueOf(cv.getTipologia()));
        System.out.println(cv.getNome());
         if(client.getCentroCittadino().equals(cv.getNome())) {
             hb_aggiungi_e_visualizza.setVisible(true);
             hb_visualizza.setVisible(false);
         }
    }

    @FXML
    public void aggiungiEvento() {
        parent.aggiungiEvento();
    }

    @FXML
    public void visualizzaInfo() {
        parent.visualizzaInfo(item);
    }

    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }
}
