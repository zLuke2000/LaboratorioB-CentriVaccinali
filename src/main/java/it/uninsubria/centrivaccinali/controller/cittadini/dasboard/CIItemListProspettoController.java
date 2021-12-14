package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.enumerator.Vaccino;
import it.uninsubria.centrivaccinali.models.EventoAvverso;
import it.uninsubria.centrivaccinali.models.Result;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class CIItemListProspettoController extends Controller {


    @FXML public Text CI_noteOpzionaliItemProspetto;
    @FXML public Label CI_eventoItemProspetto;
    @FXML public Label CI_tipologiaItemProspetto;
    @FXML public Label CI_severitaitemProspetto;

    @Override
    public void initParameter(ClientCV client) { }

    @Override
    public void notifyController(Result result) { }
    
    public void setData(EventoAvverso ea) {
        this.CI_tipologiaItemProspetto.setText(ea.getTipoVac().toString());
        this.CI_eventoItemProspetto.setText(ea.getEvento());
        this.CI_severitaitemProspetto.setText(String.valueOf(ea.getSeverita()));
        this.CI_noteOpzionaliItemProspetto.setText(ea.getNote());
    }
}
