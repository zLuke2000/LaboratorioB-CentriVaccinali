package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;


public class CIShowGeneralCVController extends Controller {

    private CentroVaccinale cv;
    private CIDashboardController parent;

    @FXML private AnchorPane ap_info;
    @FXML private ImageView CI_IV_image;
    @FXML private Label CI_L_tipologia;
    @FXML private Label CI_L_nomeCV;
    @FXML private Label CI_L_indirizzoCV;
    @FXML private AnchorPane CI_AP_container;

    @FXML void initialize() {

    }

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
            img = "Hub.png";
        CI_IV_image.setImage(new Image(CentriVaccinali.class.getResourceAsStream("image/" + img)));
        this.CI_L_tipologia.setText(tipologia);
        this.CI_L_nomeCV.setText(cv.getNome());
        this.CI_L_indirizzoCV.setText(cv.getIndirizzo().toString());
    }

    @FXML
    public void mostraGrafico() {
        if (ap_segnalazioni != null)
            ap_segnalazioni.setVisible(false);
        if (ap_grafico == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/prospetto/CI_F_grafici.fxml"));
            try {
                AnchorPane ap_grafico = fxmlLoader.load();
                CIGraficiController controller = fxmlLoader.getController();
                controller.setParent(parent);
                controller.setData(cv);
                this.ap_grafico = ap_grafico;
                CI_AP_container.getChildren().add(ap_grafico);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            ap_grafico.setVisible(true);
    }

    public void mostraSegnalazioni() {
        if (ap_grafico != null)
            ap_grafico.setVisible(false);
        if (ap_segnalazioni == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(CentriVaccinali.class.getResource("fxml/fragments/prospetto/CI_F_segnalazioni.fxml"));
            try {
                AnchorPane ap_segnalazioni = fxmlLoader.load();
                CISegnalazioniController controller = fxmlLoader.getController();
                controller.setParent(parent);
                controller.setData(cv);
                this.ap_segnalazioni = ap_segnalazioni;
                CI_AP_container.getChildren().add(ap_segnalazioni);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            ap_segnalazioni.setVisible(true);
    }

    @FXML public void chiudiInfo() {
        System.out.println("Parent " + parent);
        parent.rimuoviInfo(ap_info);
    }

    public void setParent(Controller c) {
        parent = (CIDashboardController) c;
    }
}
