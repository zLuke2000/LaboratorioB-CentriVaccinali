package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CIItemListController extends Controller{

    @FXML
    private ImageView ci_iv_tipologiaItem;

    @FXML
    public Label ci_l_nomeCVItem;

    @FXML
    public Label ci_l_indirizzoItem;

    @FXML
    private Label ci_l_tipologiaItem;


    @Override
    public void initParameter(ClientCV client) {

    }

    @Override
    public void notifyController(Result result) {

    }

    public void setData(CentroVaccinale cv) {
        switch (cv.getTipologia()){
            case HUB:
                ci_iv_tipologiaItem.setImage(new Image(String.valueOf(CentriVaccinali.class.getResource("Image/Hub.png"))));
                break;
            case AZIENDALE:
                ci_iv_tipologiaItem.setImage(new Image(String.valueOf(CentriVaccinali.class.getResource("Image/Aziendale.png"))));
                break;
            case OSPEDALIERO:
                ci_iv_tipologiaItem.setImage(new Image(String.valueOf(CentriVaccinali.class.getResource("Image/Ospedaliero.png"))));
                break;
        }
        ci_l_nomeCVItem.setText(cv.getNome());
        ci_l_indirizzoItem.setText(String.valueOf(cv.getIndirizzo()));
        ci_l_tipologiaItem.setText(String.valueOf(cv.getTipologia()));
    }
}
