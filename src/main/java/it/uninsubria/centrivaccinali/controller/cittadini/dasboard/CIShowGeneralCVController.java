package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

/*
    Grafico su quanti hanno usato quale vaccino
 */

public class CIShowGeneralCVController extends Controller {

    private CentroVaccinale cv;
    private CIDashboardController parent;

    @FXML
    private AnchorPane ap_info;

    @FXML
    private ImageView CI_IV_image;

    @FXML
    private Label CI_L_tipologia;

    @FXML
    private Label CI_L_nomeCV;

    @FXML
    private Label CI_L_indirizzoCV;

    @FXML
    private AnchorPane CI_AP_container;

    @FXML void initialize() {  }

    @Override
    public void initParameter(ClientCV client) {  }

    @Override
    public void notifyController(Result result) {  }

    public void setData(CentroVaccinale cv) {
        this.cv = cv;
        String tipologia =cv.getTipologia().toString();
        String img = "";
        if (tipologia.equals("ospedaliero"))
            img = "Ospedaliero.png";
        if (tipologia.equals("aziendale"))
            img = "Aziendale.jpeg";
        if (tipologia.equals("hub"))
            img = "Hub";
        CI_IV_image.setImage(new Image(CentriVaccinali.class.getResourceAsStream("image/" + img)));
        this.CI_L_tipologia.setText(tipologia);
        this.CI_L_nomeCV.setText(cv.getNome());
        this.CI_L_indirizzoCV.setText(cv.getIndirizzo().toString());
    }

    @FXML
    public void showProspetto() {
        FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/Fragments/FragmentProspetto/CI_F_grafici.fxml"));
        try {
            AnchorPane ap_chart = fxmlLoader.load();
            CI_AP_container.getChildren().add(ap_chart);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML public void chiudiInfo() {
        System.out.println("Parent " + parent);
        parent.rimuoviInfo(ap_info);
    }

    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }
}
