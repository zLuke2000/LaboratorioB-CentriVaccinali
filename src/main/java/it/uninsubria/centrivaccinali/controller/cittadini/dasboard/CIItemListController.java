package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.kordamp.ikonli.javafx.FontIcon;

public class CIItemListController extends Controller {

    private CIDashboardController parent;
    private CentroVaccinale item;

    @FXML
    private FontIcon ci_fi_image;

    @FXML
    public Label ci_tf_nomeCVItem;

    @FXML
    public Label ci_tf_indirizzoItem;

    @FXML
    private Label ci_l_tipologiaItem;

    @Override
    public void initParameter(ClientCV client) {  }

    @Override
    public void notifyController(Result result) {  }

    public void setData(CentroVaccinale cv, String iconLiteral, String color) {
        item = cv;
        ci_fi_image.setIconLiteral(iconLiteral);
        ci_fi_image.setIconColor(Paint.valueOf(color));
        ci_tf_nomeCVItem.setText(cv.getNome());
        ci_tf_indirizzoItem.setText(String.valueOf(cv.getIndirizzo()));
        ci_l_tipologiaItem.setText(String.valueOf(cv.getTipologia()));
    }

    @FXML
    public void aggiungiEvento() {

    }

    @FXML
    public void visualizzaInfo() {
        parent.visualizzaInfo(item);
    }

    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }
}
