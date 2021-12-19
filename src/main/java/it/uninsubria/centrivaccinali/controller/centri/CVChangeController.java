package it.uninsubria.centrivaccinali.controller.centri;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.controller.Controller;
import it.uninsubria.centrivaccinali.models.Result;
import it.uninsubria.centrivaccinali.util.DialogHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 *
 */
public class CVChangeController extends Controller {

    /**
     * Metodo per avviare l'interfaccia di registrazione di un cittadino vaccinato
     */
    @FXML
    private void RegistraVaccinato() {
        System.out.println("Interfaccia per registrazione di un vaccinato");
        CentriVaccinali.setRoot("CV_registraVaccinato");
    }

    /**
     * Metodo per avviare l'interfaccia di registrazione di un centro vaccinale
     *
     */
    @FXML
    private void RegistraCentroVaccinale() {
        System.out.println("Interfaccia per registrazione di centro vaccinale");
        CentriVaccinali.setRoot("CV_registraCentroVaccinale");
    }

    /**
     * Metodo per tornare all'interfaccia precedente
     */
    @FXML
    private void backTo() {
        DialogHelper dh = new DialogHelper("ATTENZIONE","Vuoi eseguire il logout?", DialogHelper.Type.WARNING);
        Button bs = new Button("SI");
        bs.setOnAction(actionEvent -> {
            CentriVaccinali.setRoot("Avvio");
            dh.close();
        });
        dh.addButton(bs);
        dh.display();

    }

    /**
     *
     */
    @FXML
    private void chiudiApp() {
        super.closeApp();
    }

    /**
     *
     * @param result
     */
    @Override
    public void notifyController(Result result) {  }
}
