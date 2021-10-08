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
    void notifyLogin(boolean ritorno, Cittadino c) throws RemoteException;
    void risultato(List<String> resultComuni, List<CentroVaccinale> resultCentri, int resultRegistrazione) throws RemoteException;
}
