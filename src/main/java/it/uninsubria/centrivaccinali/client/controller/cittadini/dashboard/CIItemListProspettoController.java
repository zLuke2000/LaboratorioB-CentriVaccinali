//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.client.controller.cittadini.dashboard;

import it.uninsubria.centrivaccinali.client.controller.Controller;
import it.uninsubria.centrivaccinali.models.EventoAvverso;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Item della lista delle segnalazioni per un dato centro vaccinale.
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele 742495
 * @author Pintonello Christian 741112
 */
public class CIItemListProspettoController extends Controller {


    /**
     * <code>Label</code> contenente la tipologia di evento avverso registrato.
     * @see Label
     */
    @FXML private Label l_evento;


    /**
     * <code>Label</code> contenente la tipologia del vaccino somministrato.
     * @see Label
     */
    @FXML private Label l_tipologia;


    /**
     * <code>Label</code> contenente la severit&amp;agrave dell'evento avverso registrato.
     * @see Label
     */
    @FXML private Label l_severita;


    /**
     * <code>Label</code> contenente le note opzionali dell'evento avverso registrato.
     * @see Label
     */
    @FXML private Label l_note;


    /**
     * L'evento avverso di questo item della lista.
     */
    private EventoAvverso evento;


    //Metodo ereditato dalla superclasse
    @Override
    public void notifyController(Result result) { }


    /**
     * Setta nell'item le informazioni di un dato evento avverso.
     * @param ea l'evento avverso di cui mostrare le informazioni.
     */
    public void setData(EventoAvverso ea) {
        this.evento = ea;
        l_tipologia.setText(ea.getTipoVac().toString());
        l_evento.setText(ea.getTipologiaEvento());
        l_severita.setText(String.valueOf(ea.getSeverita()));
        if (ea.getNote().length() == 0) {
            l_note.setDisable(true);
            l_note.setText("Nessuna Nota ...");
        }
    }


    /**
     * Vengono mostrare le note opzionali, se presenti.
     */
    @FXML
    private void apri() {
        new DialogHelper("NOTE OPZIONALI", evento.getNote(), DialogHelper.Type.INFO).display();
    }
}
