package it.uninsubria.centrivaccinali.client;

import it.uninsubria.centrivaccinali.models.CentroVaccinale;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCVInterface extends Remote {
    void notifyStatus(boolean ritorno) throws RemoteException;
}
