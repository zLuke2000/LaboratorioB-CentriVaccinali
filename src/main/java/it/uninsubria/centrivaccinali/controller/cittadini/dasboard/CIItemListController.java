package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class CIItemListController extends Controller {

    private CIDashboardController parent;
    private CentroVaccinale item;

    @FXML public HBox ci_accesibile;
    @FXML public HBox ci_noAccessibile;
    @FXML private ImageView ci_iv_imageItem;
    @FXML public Label ci_tf_nomeCVItem;
    @FXML public Label ci_tf_indirizzoItem;
    @FXML private Label ci_l_tipologiaItem;

    @Override
    public void initParameter(ClientCV client) {  }

    @Override
    public void notifyController(Result result) {  }

    public void setData(CentroVaccinale cv, Image img) {
        item = cv;
        ci_accesibile.setVisible(true);
        ci_noAccessibile.setVisible(false);
        /*
        if (item.getNome().equals(CentriVaccinali.client.getCentroCittadino())) {
            ci_accesibile.setVisible(true);
            ci_noAccessibile.setVisible(false);
        }
        */
        ci_iv_imageItem.setImage(img);
        ci_tf_nomeCVItem.setText(cv.getNome());
        ci_tf_indirizzoItem.setText(String.valueOf(cv.getIndirizzo()));
        ci_l_tipologiaItem.setText(String.valueOf(cv.getTipologia()));
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
