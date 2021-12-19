package it.uninsubria.centrivaccinali.controller.cittadini.dashboard;

import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.EventoAvverso;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CIItemListProspettoController extends Controller {

    @FXML private Label l_evento;
    @FXML private Label l_tipologia;
    @FXML private Label l_severita;
    @FXML private Label l_note;

    private EventoAvverso evento;

    @Override
    public void notifyController(Result result) { }

    public void setData(EventoAvverso ea) {
        this.evento = ea;
        l_tipologia.setText(ea.getTipoVac().toString());
        l_evento.setText(ea.getEvento());
        l_severita.setText(String.valueOf(ea.getSeverita()));
        if (ea.getNote().length() == 0) {
            l_note.setDisable(true);
            l_note.setText("Nessuna Nota ...");
        }
    }

    @FXML
    private void apri() {
        new DialogHelper("NOTE OPZIONALI", evento.getNote(), DialogHelper.Type.INFO).display();
    }
}
