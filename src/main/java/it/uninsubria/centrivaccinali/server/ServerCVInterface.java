package it.uninsubria.centrivaccinali.server;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.client.ClientCVInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerCVInterface extends Remote{
    // void registraVaccinato(Vaccinato cittadinovaccinato) throws RemoteException;
    // void registraCentroVaccinale(CentroVaccinale) throws RemoteException;

    boolean autenticaOperatore(String username, String password) throws RemoteException;
}
