package it.uninsubria.centrivaccinali.controller.cittadini.dasboard;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.enumerator.Vaccino;
import it.uninsubria.centrivaccinali.models.EventoAvverso;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class CIItemListProspettoController extends Controller {
    
    private EventoAvverso ea;

    @FXML public Label l_evento;
    @FXML public Label l_tipologia;
    @FXML public Label l_severita;
    @FXML public Label l_note;


    @Override
    public void notifyController(Result result) { }

    public void setData(EventoAvverso ea) {
        this.ea = ea;
        l_tipologia.setText(ea.getTipoVac().toString());
        l_evento.setText(ea.getEvento());
        l_severita.setText(String.valueOf(ea.getSeverita()));
        if (ea.getNote().length() == 0) {
            l_note.setDisable(true);
        }
    }

    public void apri() {
        DialogHelper dh = new DialogHelper("NOTE OPZIONALI", ea.getNote(), DialogHelper.Type.INFO);
        dh.display(null);
    }
}
