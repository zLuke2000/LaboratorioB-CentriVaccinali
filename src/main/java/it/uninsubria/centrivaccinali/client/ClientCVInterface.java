package it.uninsubria.centrivaccinali.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCVInterface extends Remote {

    /**
     * @param ritorno
     * @throws RemoteException
     */
    void notifyStatus(boolean ritorno) throws RemoteException;
}
