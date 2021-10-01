package it.uninsubria.centrivaccinali.server;

import it.uninsubria.centrivaccinali.client.ClientCVInterface;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Cittadino;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerCVInterface extends Remote{
    void authOperatore(ClientCVInterface clientCV, String username, String password) throws RemoteException;
    int registraCentro(CentroVaccinale cv) throws RemoteException;
    void registraCittadino(Cittadino cittadino) throws RemoteException;
    void cercaCentroVaccinale(ClientCVInterface client, String testo) throws RemoteException;
}
