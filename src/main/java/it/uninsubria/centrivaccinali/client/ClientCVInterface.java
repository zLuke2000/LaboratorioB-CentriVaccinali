package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.models.Result;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 */
public interface ClientCVInterface extends Remote {
    /**
     *
     * @param ritorno
     * @throws RemoteException
     */
    void notifyStatus(Result ritorno) throws RemoteException;
}
