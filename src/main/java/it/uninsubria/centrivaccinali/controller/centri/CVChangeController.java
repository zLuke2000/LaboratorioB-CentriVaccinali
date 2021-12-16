package it.uninsubria.centrivaccinali.controller.centri;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.controller.dialog.GenericDialogController;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class CVChangeController extends Controller {

    @FXML public AnchorPane ap_root;

    /**
     * Metodo per avviare l'interfaccia di registrazione di un cittadino vaccinato
     */
    @FXML public void RegistraVaccinato() {
        System.out.println("Interfaccia per registrazione di un vaccinato");
        CentriVaccinali.setRoot("CV_registraVaccinato");
    }

    /**
     * Metodo per avviare l'interfaccia di registrazione di un centro vaccinale
     *
     */
    @FXML public void RegistraCentroVaccinale() {
        System.out.println("Interfaccia per registrazione di centro vaccinale");
        CentriVaccinali.setRoot("CV_registraCentroVaccinale");
    }

    /**
     * Metodo per tornare all'interfaccia precedente
     */
    @FXML public void backTo() {
        DialogHelper dh = new DialogHelper("ATTENZIONE","Vuoi eseguire il logout?", DialogHelper.Type.WARNING);
        Button bs = new Button("SI");
        bs.setOnAction(actionEvent -> {
            CentriVaccinali.setRoot("Avvio");
            dh.close();
        });
        dh.addButton(bs);
        dh.display(ap_root);

    }

    @FXML void chiudi() {
        super.closeApp(null);
    }


    @Override public void notifyController(Result result) {  }
}
