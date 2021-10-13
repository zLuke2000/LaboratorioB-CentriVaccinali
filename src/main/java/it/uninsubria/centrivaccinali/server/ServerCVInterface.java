package it.uninsubria.centrivaccinali.server;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.client.ClientCVInterface;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.Vaccinato;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerCVInterface extends Remote{
    // Metodi operatore sanitario
    void authOperatore(ClientCVInterface clientCV, String username, String password) throws RemoteException;
    void registraCentro(ClientCVInterface client, CentroVaccinale cv) throws RemoteException;
    void registraCittadino(ClientCVInterface client, Cittadino cittadino) throws RemoteException;
    // Metodi cittadino
    void registraVaccinato(ClientCVInterface client, Vaccinato vaccinato) throws RemoteException;
    void loginUtente(ClientCVInterface clientCV, String username, String password) throws RemoteException;
    // Metodi in comune
    void getComuni(ClientCVInterface client, String provincia) throws RemoteException;
    void getCentri(ClientCVInterface client, String comune) throws RemoteException;
    void stopThread() throws RemoteException;
}
