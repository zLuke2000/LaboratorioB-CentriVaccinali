package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.models.CentroVaccinale;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientCVInterface extends Remote {

    /**
     * @param ritorno
     * @throws RemoteException
     */
    void notifyStatus(boolean ritorno) throws RemoteException;
    void risultato(List<CentroVaccinale> listaCentri) throws RemoteException;
}
