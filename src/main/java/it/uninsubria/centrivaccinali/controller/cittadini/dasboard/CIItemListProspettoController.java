package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.Result;

public class CIItemListProspettoController extends Controller {


    @FXML public Text CI_noteOpzionaliItemProspetto;
    @FXML public Label CI_eventoItemProspetto;
    @FXML public Label CI_tipologiaItemProspetto;
    @FXML public Label CI_severitaitemProspetto;

    @Override
    public void initParameter(ClientCV client) { }

    @Override
    public void notifyController(Result result) { }
}
