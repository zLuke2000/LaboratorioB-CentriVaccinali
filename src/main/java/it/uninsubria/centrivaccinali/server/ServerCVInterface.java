package it.uninsubria.centrivaccinali.server;

import it.uninsubria.centrivaccinali.client.ClientCV;
import it.uninsubria.centrivaccinali.client.ClientCVInterface;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Cittadino;
import it.uninsubria.centrivaccinali.models.EventoAvverso;
import it.uninsubria.centrivaccinali.models.Vaccinato;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerCVInterface extends Remote{
    // Metodi operatore sanitario
    void authOperatore(ClientCVInterface client, String username, String password) throws RemoteException;
    void registraCentro(ClientCVInterface client, CentroVaccinale cv) throws RemoteException;
    void registraCittadino(ClientCVInterface client, Cittadino cittadino) throws RemoteException;
    // Metodi cittadino
    void registraVaccinato(ClientCVInterface client, Vaccinato vaccinato) throws RemoteException;
    void loginUtente(ClientCVInterface client, String username, String password) throws RemoteException;
    void ricercaCentroPerNome(ClientCVInterface client, String nomeCentro) throws RemoteException;
    void ricercaCentroPerComuneTipologia(ClientCVInterface clientCV, String comune, TipologiaCentro tipologia) throws RemoteException;
    void registraEventoAvverso(ClientCVInterface clientCV, EventoAvverso ea) throws RemoteException;
    // Metodi in comune
    void getComuni(ClientCVInterface client, String provincia) throws RemoteException;
    void getCentri(ClientCVInterface client, String comune) throws RemoteException;
    void stopThread() throws RemoteException;

}
