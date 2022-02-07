//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.models.Result;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaccia dell'oggetto remoto <code>ClientCV</code>.
 * @see ClientCV
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele 742495
 * @author Pintonello Christian 741112
 */
public interface ClientCVInterface extends Remote {


    /**
     * Permette di notificare l'interfaccia utente.
     * @param ritorno l'operazione appena completata dal server
     * @throws RemoteException eccezione rmi.
     */
    void notifyStatus(Result ritorno) throws RemoteException;
}
