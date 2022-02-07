//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.client.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.centrivaccinali.models.Result;


/**
 * Superclasse astratta per tutti i controller dell'applicazione.
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele 742495
 * @author Pintonello Christian 741112
 */
public abstract class Controller {

    /**
     * Permette di notificare un dato controller, a seguito del completamento di una operazione da parte del server.
     * @param result rappresenta l'operazione appena eseguita.
     */
    public abstract void notifyController(Result result);


    /**
     * Permette la chiusura dell'applicazione.
     */
    public void closeApp() {
        new Thread(() -> {
            CentriVaccinali.client.stopOperation();
            CentriVaccinali.client.disconnetti();
            System.exit(0);
        }).start();
    }
}
